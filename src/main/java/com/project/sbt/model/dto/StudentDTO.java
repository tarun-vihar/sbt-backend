package com.project.sbt.model.dto;

import com.project.sbt.model.keys.StudentPrimaryKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;




@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "FF_STUDENT")
public class StudentDTO extends AbstractEntity {

    @EmbeddedId
    private StudentPrimaryKey studentPrimaryKey;

    @NotNull(message = "Student Name cannot be empty")
    @Column(name = "student_name", nullable= false)
    private String studentName;



    @NotNull(message = "Student Email cannot be empty")
    @Column(name = "student_email", nullable = false)
    private String studentEmail;


    @Column(name = "wallet_id")
    private String walletId;


    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "is_enabled")
    private boolean isEnabled;



    @Transient
    private String errors;






}
