package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.model.dto.AbstractEntity;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public AbstractEntity updateStatus(AbstractEntity data, String action){

        if(data == null) return data;

        switch (action){
            case "reject":{
                data.setStatus(Constants.REJECTED_STATUS);
                break;
            }
            case "approve":{
                data.setStatus(Constants.APPROVED_STATUS);
                break;
            }
            case "save":
            default:
                data.setStatus(Constants.UNAPPROVED_STATUS);

        }

        return data;
    }
}
