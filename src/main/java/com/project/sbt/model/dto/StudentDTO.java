package com.project.sbt.model.dto;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Student Name cannot be empty")
    @Column(name = "student_name", nullable= false)
    private String studentName;

    @NotNull(message = "Student Id cannot be empty")
    @Column(name = "student_id", nullable= false, unique = true)
    private String studentId;

    @NotNull(message = "Student Email cannot be empty")
    @Column(name = "student_email", nullable = false)
    private String studentEmail;


    @Column(name = "wallet_id")
    private String walletId;


    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "is_enabled")
    private boolean isEnabled;



    @ManyToOne
    private UniversityDTO university;

    @Transient
    private String errors;






}
