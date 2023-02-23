package com.project.sbt.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessageResponse {


    private Object message;
    private Boolean status;
    private Object data;
}
