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
    @JsonProperty("id")
    private String studentId;

    @NotNull(message = "Student Email cannot be empty")
    @JsonProperty("studentName")
    private String studentName;

    @NotNull(message = "Student Email cannot be empty")
    @JsonProperty("studentEmail")
    private String studentEmail;


    @JsonProperty("uID")
    private Integer universityId;

    @JsonProperty("accountAddress")
    private String walletAddres;

    private String error;
}
