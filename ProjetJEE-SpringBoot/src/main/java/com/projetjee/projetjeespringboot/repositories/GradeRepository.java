package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    @Query("SELECT g FROM Grade g WHERE g.student.idStudent = :idStudent AND g.assessment.course.idCourse = :idCourse")
    List<Grade> findByStudentAndCourse(@Param("idStudent") Integer idStudent, @Param("idCourse") Integer idCourse);

    @Query("SELECT g FROM Grade g WHERE g.student.idStudent = :idStudent")
    List<Grade> findByStudent(@Param("idStudent") Integer idStudent);

}