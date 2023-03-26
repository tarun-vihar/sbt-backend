package com.project.sbt.controller;


import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.request.UniversityRequest;
import com.project.sbt.response.ServiceResponse;
import com.project.sbt.services.CommonService;
import com.project.sbt.services.UniversityService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityService univesityService;

    @Autowired
    CommonService commonService;

    @PostMapping("/signup")
    public ServiceResponse register(@RequestBody @NonNull final UniversityRequest universityRequest,
                                    @RequestParam(name = "action", required = false,defaultValue = "save") String action){

        UniversityDTO university =  univesityService.saveUniversity(universityRequest,action);

        return new ServiceResponse(university, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ServiceResponse getAllUniversites(){

        List<UniversityDTO> universityList = univesityService.getAllUniversities();

        return new ServiceResponse(universityList,HttpStatus.OK);

    }

    @PostMapping("/authenticate")
    public ServiceResponse getUniversityInfo( @RequestBody UniversityRequest universityRequest){

        String wallterId = universityRequest.getUniversityWalletAddress();
        UniversityDTO universityDTO = univesityService.getUnivesityByWalletId(wallterId);

        return new ServiceResponse(universityDTO,HttpStatus.OK);
    }







}