package com.project.sbt.controller;

import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.request.StaffRequest;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.response.BaseMessageResponse;
import com.project.sbt.response.ServiceResponse;
import com.project.sbt.services.StaffService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StaffController {

    @Autowired
    StaffService staffService;

    @PostMapping("/staff/get-info")
    public ServiceResponse getStaffInfo(@RequestBody StaffRequest staffRequest){



        return null;

    }


    @PostMapping("/register-staff")
    public ServiceResponse postStaffData(@RequestBody @NonNull List<StaffRequest> staffRequestList,
                                           @RequestParam(name = "action", required = false,defaultValue = "validate") String action,
                                           @RequestParam(name = "university_id") Integer university_id){

        BaseMessageResponse staffUploadResponse = staffService.registerStaff(staffRequestList,action,university_id);

        return new ServiceResponse(staffUploadResponse, HttpStatus.OK);

    }

}
