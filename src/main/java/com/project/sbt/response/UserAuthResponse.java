package com.project.sbt.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthResponse {

    @JsonProperty("isAuthValid")
    private Boolean isAuthValid;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
