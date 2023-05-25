package com.project.sbt.repository;

import com.project.sbt.model.dto.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository  extends JpaRepository<AdminUser, Long> {

    AdminUser findByUsername(String username);


}
