package com.project.sbt.repository;

import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import com.project.sbt.model.keys.StudentPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentDTO, StudentPrimaryKey> {
//
    public StudentDTO findByStudentWalletAddress(String walletId);

    List<StudentDTO> findAllByStudentPrimaryKeyUniversityAndIsEnabled(UniversityDTO university, boolean isEnabled);

    StudentDTO findByStudentPrimaryKeyAndVerificationCode(StudentPrimaryKey studentPrimaryKey, String verificationCode);
//
    StudentDTO findByStudentPrimaryKeyAndIsEnabled(StudentPrimaryKey studentPrimaryKey, boolean isEnabled);
}