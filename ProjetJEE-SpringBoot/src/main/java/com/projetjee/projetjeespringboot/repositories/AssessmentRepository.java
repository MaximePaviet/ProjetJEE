package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Integer> {

    boolean existsByNameAndCourse_IdCourse(String name, int idCourse);

    @Query("SELECT a, g.grade FROM Assessment a LEFT JOIN Grade g " +
            "ON g.assessment.idAssessment = a.idAssessment AND g.student.idStudent = :studentId " +
            "WHERE a.course.idCourse = :courseId")
    List<Object[]> findAssessmentsWithGradesByCourseAndStudent(@Param("courseId") int courseId,
                                                               @Param("studentId") int studentId);
}