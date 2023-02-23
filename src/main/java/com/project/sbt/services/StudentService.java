package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.request.CertificateRequest;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.repository.StudentRepository;
import com.project.sbt.response.BaseMessageResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        CommonService commonService;

        @Autowired
        Validator validator;

        @Autowired
        EmailService emailService;


        public List<StudentDTO> getAllUnregisteredStudentsForUniversity(Integer unversityId){

                UniversityDTO universityDTO = univesityService.getUniversityById(unversityId);
                return studentRepository.findByUniversityAndStatus(universityDTO, Constants.UNAPPROVED_STATUS);

        }

        public StudentDTO getStudentInfo(String walletId){
                return studentRepository.findByWalletId(walletId);
        }

        public StudentDTO saveStudent(StudentRequest studentRequest, String action){

                UniversityDTO university = univesityService.getUniversityById(studentRequest.getUniversityId());

                if(university != null){

                        StudentDTO studentDTO = getStudent(studentRequest);
                        studentDTO.setUniversity(university);
                        studentDTO = (StudentDTO) commonService.updateStatus(studentDTO,action);
                        return studentRepository.save(studentDTO);
                }

                return null;

        }

        public StudentDTO getStudent(StudentRequest studentRequest){

                StudentDTO studentDTO = StudentDTO.builder()
                        .studentId(studentRequest.getStudentId())
                        .studentName(studentRequest.getStudentName())
                        .studentEmail(studentRequest.getStudentEmail())
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
                                        studentRequest.setError(studentRequest.getError() + " - " + violation.getMessage());
                                        hadErrors.set(true);
                                }
                                return studentRequest;

                        })
                        .collect(Collectors.toList());

                if(action.equals("save") && !hadErrors.get()){

                      List<StudentDTO> studentDTOList =  studentRequestList.stream()
                                        .map(this::getStudent)
                                        .map(p -> {
                                                p.setEnabled(false);
                                                p.setStatus(Constants.UNAPPROVED_STATUS);
                                                p.setVerificationCode( UUID.randomUUID().toString());
                                                p.setUniversity(universityDTO);
                                                return  p;
                                        }).collect(Collectors.toList());

                        studentRepository.saveAll(studentDTOList);


                       return sendEmail(studentDTOList);
                }

                return new BaseMessageResponse("Unable to Save the Data", false, studentRequestList);
        }

        public BaseMessageResponse sendEmail(List<StudentDTO> studentDTOList){
            studentDTOList.forEach(studentDTO -> {
                try {
                    emailService.sendEmail(studentDTO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            return  new BaseMessageResponse("Uploaded Sucessfully, Verification emails are sent",true, null);
        }


    public BaseMessageResponse vefifyStudent(StudentRequest studentRequest, String verificationCode) {

            StudentDTO studentDTO = studentRepository
                    .findByStudentIdAndVerificationCode(studentRequest.getStudentId(),verificationCode);

            if(studentDTO == null)
                return new BaseMessageResponse("Verfication Code. Incorrect Username", false,null);

            studentDTO.setEnabled(true);
            studentDTO.setWalletId(studentRequest.getWalletAddres());// metamask address

            studentRepository.save(studentDTO);

            return new BaseMessageResponse("Successfully Verifcated", true, studentDTO);
    }




    public BaseMessageResponse verifyCertificate(List<CertificateRequest> certificateRequestList, String action, Integer university_id) {

        AtomicBoolean hadErrors = new AtomicBoolean(false);
        certificateRequestList.stream()
                .map( certificateInfo -> {


                    Set<ConstraintViolation<CertificateRequest>> violationSet = validator.validate(certificateInfo);
                    for (ConstraintViolation<CertificateRequest> violation : violationSet){
                        certificateInfo.setError(certificateInfo.getError() + " - " + violation.getMessage());
                        hadErrors.set(true);
                    }

                    if(hadErrors.get()){
                        StudentDTO studentDTO = studentRepository.findByStudentIdAndIsEnabled(certificateInfo.getStudentId(),true);

                        if(studentDTO == null){
                            hadErrors.set(true);
                            certificateInfo.setError(certificateInfo.getStudentId() + " is not registerd or authenicated");
                        }
                        else {
                            certificateInfo.setWalletAddres(studentDTO.getWalletId());
                        }
                    }
                    return certificateInfo;

                })
                .collect(Collectors.toList());

        return new BaseMessageResponse("", hadErrors.get(),certificateRequestList);

    }
}
