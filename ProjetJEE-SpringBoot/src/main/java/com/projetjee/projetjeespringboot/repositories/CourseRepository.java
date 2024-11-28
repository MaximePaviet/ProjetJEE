package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
