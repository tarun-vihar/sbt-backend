package com.project.sbt.model.dto.metadata;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FF_FIELDS_DETAILS")
public class FormFieldsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "component")
    private String component;

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private String label;

    @Column(name = "helpertext")
    private String helperText;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "feild_type")
    private String type;

    @Column(name = "IS_DISABLED")
    private boolean isSearchable;

    @Column(name = "IS_REQUIRED")
    private boolean isRequired;



}
