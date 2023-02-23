package com.project.sbt.repository;

import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.dto.UniversityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentDTO,Integer> {
//
    public StudentDTO findByWalletId(String walletId);

    public List<StudentDTO> findByUniversityAndStatus(UniversityDTO university, String status);

    StudentDTO findByStudentIdAndVerificationCode(String studentId, String verificationCode);

    StudentDTO findByStudentIdAndIsEnabled(String studentId, boolean isEnabled);
}