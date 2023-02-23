package com.project.sbt.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.sbt.model.dto.StudentDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StudentDetailsResponse {

    @JsonProperty("data")
    private List<StudentDTO> studentList;
}
