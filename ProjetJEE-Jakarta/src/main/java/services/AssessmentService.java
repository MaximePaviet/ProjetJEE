package services;

import jakarta.persistence.*;
import models.Assessment;
import models.Grade;
import models.Student;
import java.util.List;
import models.Course;

import org.hibernate.SessionFactory;

public class AssessmentService {

    // Constructeur pour initialiser l'EntityManagerFactory
    public AssessmentService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }


    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Méthode pour créer un nouvel Assessment
    public void createAssessment(Course course, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarrer une transaction

            // Vérifier si le Course est valide
            if (course == null || course.getIdCourse() == 0) {
                System.out.println("Course is null or has idCourse = 0");
                throw new IllegalArgumentException("Invalid Course object provided.");
            }


            // Vérifier si le Course existe dans la base de données
            Course persistedCourse = entityManager.find(Course.class, course.getIdCourse());
            if (persistedCourse == null) {
                throw new IllegalArgumentException("Course with ID " + course.getIdCourse() + " not found in the database.");
            }

            // Création de l'objet Assessment
            Assessment assessment = new Assessment();
            assessment.setCourse(persistedCourse); // Associer l'objet Course
            assessment.setName(name);

            // Persiste l'objet Assessment dans la base de données
            entityManager.persist(assessment);
            transaction.commit(); // Valider la transaction
            System.out.println("Assessment created successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Annuler la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Fermer l'EntityManager
        }
    }


    public void createGrade(Student student, int idAssessment, float gradeValue) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarrer une transaction

            // Récupérer l'Assessment depuis la base de données
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Vérifier que l'étudiant existe en base de données
            Student persistedStudent = entityManager.find(Student.class, student.getIdStudent());
            if (persistedStudent == null) {
                throw new IllegalArgumentException("Student with ID " + student.getIdStudent() + " not found.");
            }

            // Création de l'objet Grade
            Grade grade = new Grade();
            grade.setStudent(persistedStudent);  // Associer l'étudiant existant
            grade.setAssessment(assessment);    // Associer l'Assessment existant
            grade.setGrade(gradeValue);         // Définir la valeur de la note

            // Ajouter la note à la liste des grades de l'évaluation
            assessment.getGradeList().add(grade);

            // Persister la note dans la base de données
            entityManager.persist(grade);
            transaction.commit(); // Valider la transaction
            System.out.println("Grade created successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Annuler la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Fermer l'EntityManager
        }
    }


    public void averageAssessmentClass(List<Grade> gradeList) {
        if (gradeList == null || gradeList.isEmpty()) {
            System.out.println("No grades available for this assessment.");
            return;
        }

        // Calculer la moyenne
        double average = gradeList.stream()
                .mapToDouble(Grade::getGrade) // Extraire la valeur de la note
                .average() // Calculer la moyenne
                .orElse(0.0); // Par défaut 0 si la liste est vide

        System.out.println("The average grade for the class is: " + average);
    }



}
