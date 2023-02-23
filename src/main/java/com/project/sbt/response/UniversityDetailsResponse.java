package com.project.sbt.response;




import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.sbt.model.dto.UniversityDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class UniversityDetailsResponse {

    @JsonProperty("data")
    private List<UniversityDTO> universityList;

}
