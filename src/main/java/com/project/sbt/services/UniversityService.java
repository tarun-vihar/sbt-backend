package com.project.sbt.services;

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

    public  UniversityDTO saveUniversity(UniversityRequest universityRequest, String action){

        UniversityDTO university = getUnivesity(universityRequest);
        university = (UniversityDTO) commonService.updateStatus(university,action);



        return universityRepostiory.save(university);
    }

    public UniversityDTO getUnivesity(UniversityRequest universityRequest){

        UniversityDTO university = UniversityDTO.builder()
                .universityName(universityRequest.getUniversityName())
                .universityAddress(universityRequest.getUniversityAddress())
                .univesityCode(universityRequest.getUniversityCode())
                .walletId(universityRequest.getWalletAddres())
                .contactNumber(universityRequest.getContactNumber())
                .build();

        return university;
    }


    public UniversityDTO getUnivesityByWalletId(String wallterId) {
        return universityRepostiory.findByWalletId(wallterId);
    }
}
