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

    // Trouver les évaluations pour un cours donné
    @Query("SELECT a FROM Assessment a WHERE a.course.idCourse = :idCourse")
    List<Assessment> findByCourseId(@Param("idCourse") Integer idCourse);

    @Query("SELECT a, g.grade FROM Assessment a LEFT JOIN Grade g " +
            "ON g.assessment.idAssessment = a.idAssessment AND g.student.idStudent = :studentId " +
            "WHERE a.course.idCourse = :courseId")
    List<Object[]> findAssessmentsWithGradesByCourseAndStudent(@Param("courseId") int courseId,
                                                               @Param("studentId") int studentId);

    // Vérifier l'existence d'une évaluation dans un cours
    @Query("SELECT COUNT(a) > 0 FROM Assessment a WHERE a.course.idCourse = :idCourse AND a.name = :name")
    boolean existsByCourseAndName(@Param("idCourse") Integer idCourse, @Param("name") String name);

    @Query("SELECT a FROM Assessment a WHERE a.name = :name AND a.course.idCourse = :idCourse")
    Assessment findByNameAndCourseId(@Param("name") String name, @Param("idCourse") Integer idCourse);

    @Query("SELECT COUNT(a) > 0 FROM Assessment a WHERE a.course.idCourse = :idCourse AND a.name = :name")
    boolean existsByCourseIdAndName(@Param("idCourse") Integer idCourse, @Param("name") String name);


}