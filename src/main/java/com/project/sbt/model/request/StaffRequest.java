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
public class StaffRequest {

    @NotNull(message = " Staff ID cannot be empty")
    @JsonProperty("staffId")
    private String staffId;

    @NotNull(message = "Staff Name cannot be empty")
    @JsonProperty("staffName")
    private String staffName;

    @NotNull(message = "Staff Email cannot be empty")
    @JsonProperty("staffEmail")
    private String staffEmail;


    @JsonProperty("universityId")
    private Integer universityId;

    @JsonProperty("staffWalletAddress")
    private String staffWalletAddress;

    private String error;
}
