package com.project.sbt.model.request;

import jakarta.validation.constraints.NotNull;

public class StudentInfo {


    @NotNull
    private String student_name;

    @NotNull
    private String student_id;
    private String program;
    private String department;
    private String email;
    private String student_wallet_address;
}
