package services;

import models.Assessment;
import models.Grade;
import models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.AssessmentRepository;
import repositories.GradeRepository;
import repositories.StudentRepository;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    // Méthode pour créer un nouvel Assessment
    @Transactional
    public Assessment createAssessment(Long courseId, String name) {
        // Vous pouvez récupérer le Course via son ID si nécessaire (par exemple avec un `CourseRepository`)
        // Mais dans cet exemple, l'ID du cours n'est pas utilisé directement.

        // Création d'un nouvel Assessment
        Assessment assessment = new Assessment();
        assessment.setName(name);

        // Sauvegarde de l'évaluation dans la base de données
        return assessmentRepository.save(assessment);
    }

    // Méthode pour créer une note (Grade) pour un étudiant dans une évaluation
    @Transactional
    public Grade createGrade(Integer studentId, Integer assessmentId, float gradeValue) {
        // Récupérer l'étudiant et l'évaluation à partir de leurs IDs
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

        // Création de la note
        Grade grade = new Grade();
        grade.setGrade(gradeValue);
        grade.setAssessment(assessment);
        grade.setStudent(student);

        // Sauvegarde de la note dans la base de données
        return gradeRepository.save(grade);
    }

    // Méthode pour calculer la moyenne des notes d'une évaluation
    @Transactional(readOnly = true)
    public double calculateAverageGrade(Integer assessmentId) {
        // Récupérer toutes les notes pour une évaluation donnée
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));

        // Calculer la moyenne des notes
        return assessment.getGradeList().stream()
                .mapToDouble(Grade::getGrade)
                .average()
                .orElse(0.0); // Retourne 0 si aucune note n'est présente
    }
}
