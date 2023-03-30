package com.project.sbt.repository;


import com.project.sbt.model.dto.StaffDTO;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.keys.StudentPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository  extends JpaRepository<StaffDTO, StaffRepository> {
}
