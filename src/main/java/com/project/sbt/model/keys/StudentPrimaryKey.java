package com.project.sbt.model.keys;

import com.project.sbt.model.dto.UniversityDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
public class StudentPrimaryKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Student Id cannot be empty")
    @Column(name = "student_id")
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private UniversityDTO university;


}
