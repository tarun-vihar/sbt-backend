package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.model.dto.StaffDTO;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.request.StaffRequest;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.repository.StaffRepository;
import com.project.sbt.response.BaseMessageResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return null;
    }

    private StaffDTO getStaff(StaffRequest staffRequest, UniversityDTO universityDTO) {

        return  null;
    }

}
