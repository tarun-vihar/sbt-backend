package com.project.sbt.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UniversityRequest {

    @JsonProperty("universityCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityCode;

    @NotNull(message = "University name cannot  be empty")
    @JsonProperty("universityName")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityName;

    @JsonProperty("universityAddress")
    private String universityAddress;


    @NotNull(message = "University Email cannot  be empty")
    @JsonProperty("universityEmail")
    private String universityEmail;

    @NotNull(message = "Metamask Wallet address is mandatory ")
    @JsonProperty("universityWalletAddress")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityWalletAddress;
}
