package com.project.sbt.model.request;

import lombok.Data;

@Data
public class EmailRequest {

    private String toAddress;
    private String subject;
    private String content;

}
