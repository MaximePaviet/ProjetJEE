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

    @Query("SELECT g.grade FROM Grade g WHERE g.assessment.course.idCourse = :courseId")
    List<Double> findGradesByCourseId(@Param("courseId") int courseId);

    @Query("SELECT g.grade FROM Grade g WHERE g.assessment.course.idCourse = :courseId AND g.student.idStudent = :studentId")
    List<Double> findGradesByCourseIdAndStudentId(@Param("courseId") int courseId, @Param("studentId") int studentId);

    // Requête pour récupérer les grades d'un étudiant dans un cours
    @Query("FROM Grade g WHERE g.student.idStudent = :idStudent AND g.assessment.course.idCourse = :idCourse")
    List<Grade> findGradesByStudentAndCourse(@Param("idStudent") int idStudent, @Param("idCourse") int idCourse);

    // Récupérer tous les cours associés à un étudiant
    @Query("SELECT c.idCourse FROM Course c JOIN c.studentList s WHERE s.idStudent = :idStudent")
    List<Integer> findCourseIdsByStudent(@Param("idStudent") int idStudent);

    List<Grade> findByAssessment_IdAssessment(int assessmentId);
}