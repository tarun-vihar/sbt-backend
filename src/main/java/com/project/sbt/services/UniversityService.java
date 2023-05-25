package com.project.sbt.services;

import com.project.sbt.constants.Constants;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.request.UniversityRequest;
import com.project.sbt.repository.UniversityRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService {

    @Autowired
    UniversityRepostiory universityRepostiory;

    @Autowired
    CommonService commonService;

    public UniversityDTO getUniversityById(Integer universityId){

        return universityRepostiory.findById(universityId).orElse(null);

    }

    public List<UniversityDTO> getAllUniversities(){

        return universityRepostiory.findAll();
    }

    public  UniversityDTO saveUniversity(UniversityRequest universityRequest, String action,String universityId){

        UniversityDTO university = null;

        switch (action){

            case "save":
                university = getUnivesity(universityRequest);
                university.setStatus(Constants.UNAPPROVED_STATUS);
                break;
            case  "approve":

                university = getUniversityById(Integer.parseInt(universityId));
                university.setStatus(Constants.APPROVED_STATUS);
                break;
            case "reject":
                university = getUniversityById(Integer.parseInt(universityId));
                university.setStatus(Constants.REJECTED_STATUS);
                break;
            default:
                return null;

        }

        return universityRepostiory.save(university);
    }

    public UniversityDTO getUnivesity(UniversityRequest universityRequest){

        UniversityDTO university = UniversityDTO.builder()
                .universityName(universityRequest.getUniversityName())
                .universityAddress(universityRequest.getUniversityAddress())
                .univesityCode(universityRequest.getUniversityCode())
                .universityWalletAddress(universityRequest.getUniversityWalletAddress())
                .universityEmail(universityRequest.getUniversityEmail())
                .build();

        return university;
    }


    public UniversityDTO getUnivesityByWalletId(String wallterId) {
        return universityRepostiory.findByUniversityWalletAddress(wallterId);
    }

    public List<UniversityDTO> getAllUnapproved() {
        return universityRepostiory.findByStatus(Constants.UNAPPROVED_STATUS);
    }
}
