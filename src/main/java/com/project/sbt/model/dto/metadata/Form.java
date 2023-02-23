package com.project.sbt.model.dto.metadata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FF_FORMS")
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "title")
    private String description;

    @Column(name = "uri")
    private String uri;

    @Column(name = "EDIT_ONLY")
    private boolean editOnly;

    @Column(name = "can_delete")
    private boolean canDelete;

    @Column(name ="bulk_edit")
    private boolean bulkEdit;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "forms_id")
    @OrderBy("id asc")
    @BatchSize(size = 25)
    private Set<FormFieldsMaster> feilds;
}
