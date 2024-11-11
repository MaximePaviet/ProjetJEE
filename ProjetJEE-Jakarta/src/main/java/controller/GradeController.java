package controller;
import models.Grade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;


public class GradeController {
    // Méthode pour récupérer les notes similaires par score
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("YourPersistenceUnit");

    // Méthode pour récupérer les notes en fonction de l'étudiant
    public List<Grade> readGrades(models.Student student) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Grade> grades = new ArrayList<>();

        try {
            List<Integer> gradeIds = entityManager.createQuery(
                            "SELECT g.idGrade FROM Grade g WHERE g.idStudent = :idStudent", Integer.class)
                    .setParameter("idStudent", student.getIdStudent())
                    .getResultList(); // Exécute la requête avec le critère d'étudiant
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }
        return grades; // Retourne la liste des objets Grade correspondant à l'étudiant
    }
}

