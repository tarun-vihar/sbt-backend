package com.project.sbt.model.keys;

import com.project.sbt.model.dto.UniversityDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@ToString
public class StaffPrimaryKey {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Staff Id cannot be empty")
    @Column(name = "staff_id")
    private String staffId;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    private UniversityDTO university;
}
