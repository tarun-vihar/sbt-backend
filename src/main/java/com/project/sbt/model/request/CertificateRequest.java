package com.project.sbt.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequest extends StudentRequest {



    @NotNull(message = "CGPA cannot be empty")
    @JsonProperty("cgpa")
    private String cgpa;

    @NotNull(message = "Please enter student tenure info")
    @JsonProperty("tenure")
    private String tenure;


    @NotNull(message = "Please enter Issue Date")
    @JsonProperty("issueDate")
    private String issueDate;


    @NotNull(message = "Please enter Gradutation Date")
    @JsonProperty("graduationDate")
    private String graduationDate;


    @JsonProperty("certificateId")
    private Integer certificateId = 0;

    @JsonProperty("remarks")
    private String remarks;


    @JsonProperty("error")
    private String error;





}
