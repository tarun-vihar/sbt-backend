package com.project.sbt.controller;

import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.request.CertificateRequest;
import com.project.sbt.model.request.StudentRequest;
import com.project.sbt.response.BaseMessageResponse;
import com.project.sbt.response.ServiceResponse;
import com.project.sbt.services.StudentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
public class StudentController {


    @Autowired
    StudentService studentService;

    @PostMapping("/signup")
    public ServiceResponse addNewStudent(@RequestBody @NonNull final StudentRequest studentRequest,
                                         @RequestParam(name = "action", required = false,defaultValue = "save") String action){

        StudentDTO studentDTO = studentService.saveStudent(studentRequest,action);

        return new ServiceResponse(studentDTO, HttpStatus.OK);
    }



    @GetMapping("/get-unapproved-students")
    public ServiceResponse getAllUnapprovedStudents( @RequestParam(name = "university_id") Integer university_id){

         List<StudentDTO> studentDTOList = studentService.getAllUnregisteredStudentsForUniversity(university_id);

        return new ServiceResponse(studentDTOList, HttpStatus.OK);
    }


    @PostMapping("/student/get-info")
    public ServiceResponse getStudentInfo( @RequestBody String wallterId){

        StudentDTO studentDTO = studentService.getStudentInfo(wallterId);

        return new ServiceResponse(studentDTO,HttpStatus.OK);
    }

    @PostMapping("/register-students")
    public ServiceResponse postStudentData(@RequestBody @NonNull List<StudentRequest> studentRequestList,
                                    @RequestParam(name = "action", required = false,defaultValue = "validate") String action,
                                    @RequestParam(name = "university_id") Integer university_id){

        BaseMessageResponse studentUploadResponse = studentService.registerStudents(studentRequestList,action,university_id);

        return new ServiceResponse(studentUploadResponse, HttpStatus.OK);

    }

    @PostMapping("/authenticate")
    public ServiceResponse verify(@RequestBody @NonNull StudentRequest studentRequest,
                                    @RequestParam(name = "token") String uuid) throws UnsupportedEncodingException {

        String verificationCode  = URLDecoder.decode(uuid, StandardCharsets.UTF_8.toString());
        studentRequest.setStudentId(URLDecoder.decode(studentRequest.getStudentId(), StandardCharsets.UTF_8.toString()));
        BaseMessageResponse studentVerificationResponse = studentService.vefifyStudent(studentRequest,verificationCode);

        return new ServiceResponse(studentVerificationResponse, HttpStatus.OK);

    }

    @PostMapping("/certificates")
    public ServiceResponse postCertificateData(@RequestBody @NonNull List<CertificateRequest> certificateRequestList,
                                    @RequestParam(name = "action", required = false,defaultValue = "validate") String action,
                                    @RequestParam(name = "university_id") Integer university_id){

        BaseMessageResponse studentUploadResponse = studentService.verifyCertificate(certificateRequestList,action,university_id);

        return new ServiceResponse(studentUploadResponse, HttpStatus.OK);

    }







}
