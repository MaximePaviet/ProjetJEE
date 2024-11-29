package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Grade;
import com.projetjee.projetjeespringboot.models.Student;
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

    @Autowired
    public AssessmentService(AssessmentRepository assessmentRepository, GradeRepository gradeRepository) {
        this.assessmentRepository = assessmentRepository;
        this.gradeRepository = gradeRepository;
    }

    // Créer une évaluation
    public void createAssessment(Course course, String name) {
        if (assessmentRepository.existsByNameAndCourse_IdCourse(name, course.getIdCourse())) {
            throw new IllegalArgumentException("An assessment with this name already exists for the course.");
        }

        Assessment assessment = new Assessment();
        assessment.setCourse(course);
        assessment.setName(name);
        assessmentRepository.save(assessment);
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
}