package com.project.sbt.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRequest extends StudentRequest{

    @NotNull(message = "CGPA  cannot be empty")
    @JsonProperty("cgpa")
    private String cgpa;


    @JsonProperty("tenure")
    private String tenure;

    @NotNull(message = "Please enter student graduation date")
    @JsonProperty("graduationDate")
    private String graduationDate;


    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("remarks")
    private String remarks;
}
