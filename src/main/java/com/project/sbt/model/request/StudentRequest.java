package com.project.sbt.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @NotNull(message = "Student ID cannot be empty")
    @JsonProperty("studentId")
    private String studentId;

    @NotNull(message = "Student Name cannot be empty")
    @JsonProperty("studentName")
    private String studentName;

    @NotNull(message = "Student Email cannot be empty")
    @JsonProperty("studentEmail")
    private String studentEmail;

    @NotNull(message = "Program name is mandatory")
    @JsonProperty("program")
    private String program;

    @NotNull(message = "Department is mandatory")
    @JsonProperty("department")
    private String department;


    @JsonProperty("universityId")
    private Integer universityId;

    @JsonProperty("studentWalletAddress")
    private String studentWalletAddress;


    private String error;
}
