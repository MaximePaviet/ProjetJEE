package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {
}