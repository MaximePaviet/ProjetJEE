package services;

import jakarta.persistence.*;
import models.Assessment;
import models.Grade;
import java.util.List;

import org.hibernate.SessionFactory;

public class AssessmentService {

    // Constructeur pour initialiser l'EntityManagerFactory
    public AssessmentService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("models.Assessment");
    }


    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Méthode pour créer un nouvel Assessment
    public void createAssessment(int idCourse, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarrer une transaction

            // Création de l'objet Assessment
            Assessment assessment = new Assessment();
            assessment.setIdCourse(idCourse);
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

    public void createGrade(int idStudent, int idAssessment, float gradeValue) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarrer une transaction

            // Récupérer l'Assessment depuis la base de données
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Création de l'objet Grade
            Grade grade = new Grade();
            grade.setIdStudent(idStudent);  // Associer l'ID de l'étudiant
            grade.setAssessment(assessment);  // Associer l'Assessment existant
            grade.setGrade(gradeValue);  // Définir la valeur de la note
            grade.setIdCourse(assessment.getIdCourse()); // Associer le cours lié à l'Assessment

            // Ajouter la note au contrôle
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
