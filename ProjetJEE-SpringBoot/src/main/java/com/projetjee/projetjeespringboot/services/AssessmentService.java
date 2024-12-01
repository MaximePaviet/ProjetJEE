package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Grade;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.repositories.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.AssessmentRepository;
import com.projetjee.projetjeespringboot.repositories.GradeRepository;
import com.projetjee.projetjeespringboot.repositories.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;


    public AssessmentService(AssessmentRepository assessmentRepository, GradeRepository gradeRepository,CourseRepository courseRepository) {
        this.assessmentRepository = assessmentRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;

    }


    public List<Assessment> getAssessmentsByCourseId(Integer idCourse) {
        return assessmentRepository.findByCourseId(idCourse);
    }

    @Transactional
    public void createAssessment(Course course, String name) {
        // Check if the Course is valid
        if (course == null || course.getIdCourse() == 0) {
            throw new IllegalArgumentException("Invalid Course object provided.");
        }

        // Check if the Course exists in the database
        Course persistedCourse = courseRepository.findById(course.getIdCourse())
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + course.getIdCourse() + " not found in the database."));

        // Adding the assessment to the database
        Assessment assessment = new Assessment();
        assessment.setCourse(persistedCourse);
        assessment.setName(name);
//        assessment.setAverage(-1.0);

        System.out.println("Assessment : " + assessment);
        System.out.println("Course associé : " + assessment.getCourse());
        System.out.println("Course ID : " + assessment.getCourse().getIdCourse());


        assessmentRepository.save(assessment);

        System.out.println("Assessment created successfully!");
    }

    // Ajouter une note à une évaluation
    public void createGrade(Student student, int assessmentId, float gradeValue) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setAssessment(assessment);
        grade.setGrade(gradeValue);
        gradeRepository.save(grade);

        updateAssessmentAverage(assessmentId); // Met à jour la moyenne
    }

    // Met à jour la moyenne d'une évaluation
    public void updateAssessmentAverage(int assessmentId) {
        List<Grade> grades = gradeRepository.findByAssessment_IdAssessment(assessmentId);

        double average = grades.stream()
                .mapToDouble(Grade::getGrade)
                .average()
                .orElse(0.0);

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));
        assessment.setAverage(average);
        assessmentRepository.save(assessment);
    }

    // Récupérer les évaluations et les notes pour un étudiant dans un cours
    public Map<Assessment, Double> getAssessmentsAndGradesByCourseAndStudent(int courseId, int studentId) {
        List<Object[]> results = assessmentRepository.findAssessmentsWithGradesByCourseAndStudent(courseId, studentId);

        Map<Assessment, Double> assessmentsWithGrades = new HashMap<>();
        for (Object[] row : results) {
            Assessment assessment = (Assessment) row[0];
            Double grade = (Double) row[1];
            assessmentsWithGrades.put(assessment, grade);
        }
        return assessmentsWithGrades;
    }
    public Map<Integer, Map<String, Float>> calculateMinMaxGrades(List<Assessment> assessments) {
        Map<Integer, Map<String, Float>> result = new HashMap<>();

        for (Assessment assessment : assessments) {
            Map<String, Float> minMax = new HashMap<>();
            List<Grade> grades = assessment.getGradeList();

            if (grades == null || grades.isEmpty()) {
                minMax.put("min", null); // Aucune note
                minMax.put("max", null);
            } else {
                float min = (float) grades.stream().mapToDouble(Grade::getGrade).min().orElse(0.0);
                float max = (float) grades.stream().mapToDouble(Grade::getGrade).max().orElse(0.0);
                minMax.put("min", min);
                minMax.put("max", max);
            }
            result.put(assessment.getIdAssessment(), minMax);
        }
        return result;
    }

    // Vérifie si une évaluation existe déjà
    public boolean checkAssessmentExists(Course course, String name) {
        return assessmentRepository.existsByCourseIdAndName(course.getIdCourse(), name);
    }
}

