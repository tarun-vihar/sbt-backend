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

    @JsonProperty("uId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityCode;

    @NotNull(message = "University name cannot  be empty")
    @JsonProperty("uName")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityName;

    @JsonProperty("uAddress")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String universityAddress;

    @JsonProperty("uContactNumber")
    private String contactNumber;

    @JsonProperty("uEmail")
    private String universityEmail;

    @NotNull(message = "Wallet address is mandatory ")
    @JsonProperty("accountAddress")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String walletAddres;
}
