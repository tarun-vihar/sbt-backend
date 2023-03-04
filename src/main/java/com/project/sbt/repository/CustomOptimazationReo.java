package com.project.sbt.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomOptimazationReo<T> {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public void saveAll(List<T> data){

        for(T d : data){
            entityManager.persist(d);
        }
    }
}
