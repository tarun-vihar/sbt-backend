package com.project.sbt.model.dto;

import com.project.sbt.model.keys.StaffPrimaryKey;
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
@Table(name = "FF_STAFF")
public class StaffDTO {

    @EmbeddedId
    private StaffPrimaryKey staffPrimaryKey;

    @NotNull(message = "Staff Name cannot be empty")
    @Column(name = "staff_name", nullable= false)
    private String staffName;



    @NotNull(message = "Staff Email cannot be empty")
    @Column(name = "staff_email", nullable = false)
    private String staffEmail;


    @Column(name = "staffWalletAddress")
    private String staffWalletAddress;


    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "is_enabled")
    private boolean isEnabled;



    @Transient
    private String errors;

}
