package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.constants.EmailConstants;
import com.project.sbt.model.dto.StaffDTO;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.keys.StaffPrimaryKey;
import com.project.sbt.model.keys.StudentPrimaryKey;
import com.project.sbt.model.request.EmailRequest;
import com.project.sbt.model.request.StaffRequest;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.repository.StaffRepository;
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
public class StaffService {


    @Autowired
    UniversityService univesityService;

    @Autowired
    Validator validator;

    @Autowired
    EmailService emailService;
    
    @Autowired
    StaffRepository staffRepository;

    public BaseMessageResponse registerStaff(List<StaffRequest> staffRequestList, String action, Integer university_id) {

        UniversityDTO universityDTO = univesityService.getUniversityById(university_id);
        if(universityDTO == null)
            return  new BaseMessageResponse("Invalid University Name", false,null);


        AtomicBoolean hadErrors = new AtomicBoolean(false);

        staffRequestList = staffRequestList.stream()

                .map( staffRequest -> {

                    Set<ConstraintViolation<StaffRequest>> violationSet = validator.validate(staffRequest);
                    for (ConstraintViolation<StaffRequest> violation : violationSet){
                        staffRequest.setError((staffRequest.getError() == null ? "": staffRequest.getError())  + " - " + violation.getMessage());
                        hadErrors.set(true);
                    }
                    return staffRequest;

                })
                .collect(Collectors.toList());

        if(!hadErrors.get()){

            List<StaffDTO> staffDTOList =  staffRequestList.stream()
                    .map(staffRequest -> getStaff(staffRequest, universityDTO))
                    .map(p -> {

                        p.setEnabled(false);
                        p.setStatus(Constants.UNAPPROVED_STATUS);
                        p.setVerificationCode( UUID.randomUUID().toString());

                        return  p;
                    }).collect(Collectors.toList());

            staffRepository.saveAll(staffDTOList);


            return sendEmail(staffDTOList);
        }

        return new BaseMessageResponse("Unable to Save the Data", false, staffRequestList);
    }

    private BaseMessageResponse sendEmail(List<StaffDTO> staffDTOList) {

        List responeList = new ArrayList();
        staffDTOList.forEach(staffDTO -> {
            try {

                EmailRequest emailRequest = prepareStaffRequest(staffDTO);
                Response res = emailService.sendEmail(emailRequest);
                responeList.add(res);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return  new BaseMessageResponse(responeList,true, null);


    }

    private EmailRequest prepareStaffRequest(StaffDTO staffDTO) throws UnsupportedEncodingException {

        EmailRequest emailRequest = new EmailRequest();


        StaffPrimaryKey staffPrimaryKey = staffDTO.getStaffPrimaryKey();

        String encodeStudentId = URLEncoder.encode(staffPrimaryKey.getStaffId().toString(), StandardCharsets.UTF_8.toString());
        String encodedUniversityId = URLEncoder.encode(staffPrimaryKey.getUniversity().getId()+"",StandardCharsets.UTF_8.toString());
        String encodedToken = URLEncoder.encode(staffDTO.getVerificationCode(), StandardCharsets.UTF_8.toString());

        String content = "Dear " + staffDTO.getStaffName() + ", please click the link to verify your account: " + EmailConstants.redirectionURL
                + "&staffId=" + encodeStudentId + "&universityId=" + encodedUniversityId + "&token=" + encodedToken;


        emailRequest.setSubject(EmailConstants.subject);
        emailRequest.setToAddress(staffDTO.getStaffEmail());
        emailRequest.setContent(content);

        return emailRequest;

    }

    private StaffDTO getStaff(StaffRequest staffRequest, UniversityDTO universityDTO) {

        StaffPrimaryKey  staffPrimaryKey = new StaffPrimaryKey();
        staffPrimaryKey.setStaffId(staffRequest.getStaffId());
        staffPrimaryKey.setUniversity(universityDTO);

        StaffDTO staffDTO = StaffDTO.builder()
                .staffPrimaryKey(staffPrimaryKey)
                .staffEmail(staffRequest.getStaffEmail())
                .staffWalletAddress(staffRequest.getStaffWalletAddress())
                .staffName(staffRequest.getStaffName())
                .build();

        return staffDTO;
    }

    public StaffDTO getStudentById(StaffPrimaryKey staffPrimaryKey){
        return staffRepository.findById(staffRepository).orElse(null);
    }

}
