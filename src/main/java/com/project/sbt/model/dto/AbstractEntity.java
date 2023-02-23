package com.project.sbt.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {


    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;



    @Column(name = "status", nullable = false)
    private String status;


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = epochTimeNow();
    }

    @PrePersist
    protected void onCreate() {
        this.updatedAt = epochTimeNow();
        this.createdAt = epochTimeNow();
    }

    public static Long epochTimeNow() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
