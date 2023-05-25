package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.constants.EmailConstants;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.keys.StudentPrimaryKey;
import com.project.sbt.model.request.CertificateRequest;
import com.project.sbt.model.request.EmailRequest;
import com.project.sbt.model.request.StudentInfo;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.repository.StudentRepository;
import com.project.sbt.response.BaseMessageResponse;
import com.sendgrid.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class StudentService {

        @Autowired
        StudentRepository studentRepository;

        @Autowired
        UniversityService univesityService;



        @Autowired
        Validator validator;

        @Autowired
        EmailService emailService;


        public List<StudentDTO> getAllUnregisteredStudentsForUniversity(Integer unversityId){

                UniversityDTO universityDTO = univesityService.getUniversityById(unversityId);
                return studentRepository.findAll();

        }

        public StudentDTO getStudentInfo(String walletId){
                return studentRepository.findByStudentWalletAddress(walletId);
        }

        public StudentDTO saveStudent(StudentRequest studentRequest, String action){

                UniversityDTO university = univesityService.getUniversityById(studentRequest.getUniversityId());

                if(university != null){

                        StudentDTO studentDTO = getStudent(studentRequest, university);
                        return studentRepository.save(studentDTO);
                }

                return null;

        }

        public StudentDTO getStudent(StudentRequest studentRequest, UniversityDTO universityDTO){

            StudentPrimaryKey  studentPrimaryKey = new StudentPrimaryKey();
            studentPrimaryKey.setStudentId(studentRequest.getStudentId());
            studentPrimaryKey.setUniversity(universityDTO);

                StudentDTO studentDTO = StudentDTO.builder()
                        .studentName(studentRequest.getStudentName())
                        .studentEmail(studentRequest.getStudentEmail())
                        .program(studentRequest.getProgram())
                        .department(studentRequest.getDepartment())
                        .studentPrimaryKey(studentPrimaryKey)
                        .build();

                return studentDTO;
        }

        public BaseMessageResponse registerStudents(List<StudentRequest> studentRequestList, String action, Integer university_id) {

                UniversityDTO universityDTO = univesityService.getUniversityById(university_id);
                if(universityDTO == null)
                        return  new BaseMessageResponse("Invalid University Name", false,null);


                AtomicBoolean hadErrors = new AtomicBoolean(false);

                studentRequestList = studentRequestList.stream()

                        .map( studentRequest -> {

                                Set<ConstraintViolation<StudentRequest>> violationSet = validator.validate(studentRequest);
                                for (ConstraintViolation<StudentRequest> violation : violationSet){
                                        studentRequest.setError((studentRequest.getError() == null ? "": studentRequest.getError())  + " - " + violation.getMessage());
                                        hadErrors.set(true);
                                }
                                return studentRequest;

                        })
                        .collect(Collectors.toList());

                if(!hadErrors.get()){

                      List<StudentDTO> studentDTOList =  studentRequestList.stream()
                                        .map(studentRequest -> getStudent(studentRequest, universityDTO))
                                        .map(p -> {

                                            p.setEnabled(false);
                                            p.setStatus(Constants.UNAPPROVED_STATUS);
                                            p.setVerificationCode( UUID.randomUUID().toString());

                                            return  p;
                                        }).collect(Collectors.toList());

                        studentRepository.saveAll(studentDTOList);


                       return sendEmail(studentDTOList);
                }

                return new BaseMessageResponse("Unable to Save the Data", false, studentRequestList);
        }

        public BaseMessageResponse sendEmail(List<StudentDTO> studentDTOList){
            List responeList = new ArrayList();
            studentDTOList.forEach(studentDTO -> {
                try {

                    EmailRequest emailRequest = prepareStudentRequest(studentDTO);
                    Response res = emailService.sendEmail(emailRequest);
                    responeList.add(res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return  new BaseMessageResponse(responeList,true, null);
        }

    private EmailRequest prepareStudentRequest(StudentDTO studentDTO) throws UnsupportedEncodingException {

            EmailRequest emailRequest = new EmailRequest();


            StudentPrimaryKey studentPrimaryKey = studentDTO.getStudentPrimaryKey();

            String encodeStudentId = URLEncoder.encode(studentPrimaryKey.getStudentId().toString(), StandardCharsets.UTF_8.toString());
            String encodedUniversityId = URLEncoder.encode(studentPrimaryKey.getUniversity().getId()+"",StandardCharsets.UTF_8.toString());
            String encodedToken = URLEncoder.encode(studentDTO.getVerificationCode(), StandardCharsets.UTF_8.toString());

            String content = "Dear " + studentDTO.getStudentName() + ", please click the link to verify your account: " + EmailConstants.redirectionURL
                        + "&studentId=" + encodeStudentId + "&universityId=" + encodedUniversityId + "&token=" + encodedToken;


            emailRequest.setSubject(EmailConstants.subject);
            emailRequest.setToAddress(studentDTO.getStudentEmail());
            emailRequest.setContent(content);

        return emailRequest;

    }


    public BaseMessageResponse vefifyStudent(StudentRequest studentRequest, String verificationCode) {


            String walletAddress = studentRequest.getStudentWalletAddress();
            if(walletAddress == null){
                return new BaseMessageResponse(" Wallet Address is empty/incorrect format", false,null);
            }

            StudentPrimaryKey studentPrimaryKey = getStudentKey(studentRequest.getStudentId(), studentRequest.getUniversityId());

            StudentDTO studentDTO = studentRepository
                    .findByStudentPrimaryKeyAndVerificationCode(studentPrimaryKey,verificationCode);

            if(studentDTO == null)
                return new BaseMessageResponse("Incorrect Student Id or Invalid Verification Code", false,null);

            studentDTO.setEnabled(true);
            studentDTO.setStatus(Constants.APPROVED_STATUS);
            studentDTO.setStudentWalletAddress(studentRequest.getStudentWalletAddress());// metamask address

            studentRepository.save(studentDTO);

            return new BaseMessageResponse("Successfully Verified", true, studentDTO);
    }




    public BaseMessageResponse verifyCertificate(List<CertificateRequest> certificateRequestList, String action, Integer universityId) {


        certificateRequestList.stream()
                .map( certificateInfo -> {

                    boolean hadError = false;
                    StudentPrimaryKey studentPrimaryKey = getStudentKey(certificateInfo.getStudentId(), universityId);
                    StudentDTO studentDTO = getStudentById(studentPrimaryKey);
                    if(studentDTO == null ){
                        hadError = true;
                        certificateInfo.setError("Student is not registerd ");
                    } else if (!studentDTO.isEnabled()) {
                        hadError = true;
                        certificateInfo.setError("Student is not verified in the system");
                    } else {

                        certificateInfo.setDepartment(studentDTO.getDepartment());
                        certificateInfo.setProgram(studentDTO.getProgram());
                        certificateInfo.setStudentName(studentDTO.getStudentName());
                        certificateInfo.setStudentEmail(studentDTO.getStudentEmail());
                        certificateInfo.setStudentWalletAddress(studentDTO.getStudentWalletAddress());
                    }

                    if(!hadError){

                        Set<ConstraintViolation<CertificateRequest>> violationSet = validator.validate(certificateInfo);

                        for (ConstraintViolation<CertificateRequest> violation : violationSet){
                            certificateInfo.setError(certificateInfo.getError() + " - " + violation.getMessage());

                        }
                    }

                    return certificateInfo;

                })
                .collect(Collectors.toList());

        return new BaseMessageResponse("", true,certificateRequestList);

    }

    public StudentPrimaryKey getStudentKey(String studentId, Integer universityId){

        UniversityDTO universityDTO = univesityService.getUniversityById(universityId);

        StudentPrimaryKey studentPrimaryKey = new StudentPrimaryKey();

        studentPrimaryKey.setUniversity(universityDTO);
        studentPrimaryKey.setStudentId(studentId);

        return studentPrimaryKey;
    }

    public StudentDTO getStudentById(StudentPrimaryKey studentPrimaryKey){
            return studentRepository.findById(studentPrimaryKey).orElse(null);
    }


}
