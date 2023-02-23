package com.project.sbt.repository;

import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepostiory extends JpaRepository<UniversityDTO,Integer> {

    public UniversityDTO findByWalletId(String walletId);
}