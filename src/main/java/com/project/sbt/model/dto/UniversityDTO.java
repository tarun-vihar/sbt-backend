package com.project.sbt.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FF_UNIVERSITY")

public class UniversityDTO extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "university_code")
    private String univesityCode;

    @Column(name = "university_name", unique = true,nullable = false)
    private String universityName;

    @Column(name = "university_address")
    private String universityAddress;

    @Column(name = "univeristy_wallet_Address", unique = true, nullable = false)
    private String universityWalletAddress;

    @Column(name = "email", unique = true, nullable = false)
    private String universityEmail;



    @OneToMany(mappedBy = "studentPrimaryKey.university", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudentDTO> students;


}


