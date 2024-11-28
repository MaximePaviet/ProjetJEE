package services;

import jakarta.persistence.*;
import models.Assessment;
import models.Grade;
import models.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Course;

import org.hibernate.Session;
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
            transaction.begin();

            // Récupérer l'Assessment
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Récupérer l'étudiant
            Student persistedStudent = entityManager.find(Student.class, student.getIdStudent());
            if (persistedStudent == null) {
                throw new IllegalArgumentException("Student with ID " + student.getIdStudent() + " not found.");
            }

            // Créer le grade
            Grade grade = new Grade();
            grade.setStudent(persistedStudent);
            grade.setAssessment(assessment);
            grade.setGrade(gradeValue);

            // Ajouter le grade à l'évaluation
            assessment.getGradeList().add(grade);

            // Persister les objets
            entityManager.persist(grade);
            entityManager.merge(assessment);

            transaction.commit();

            // Mettre à jour la moyenne de l'évaluation
            updateAssessmentAverage(idAssessment); // Appel après la transaction pour recalculer la moyenne

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }


    public void updateAssessmentAverage(int idAssessment) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Récupérer l'Assessment
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Calculer la moyenne des grades associés
            List<Grade> gradeList = assessment.getGradeList();
            if (gradeList == null || gradeList.isEmpty()) {
                assessment.setAverage(0.0); // Aucune note, moyenne à 0
            } else {
                double average = gradeList.stream()
                        .mapToDouble(Grade::getGrade)
                        .average()
                        .orElse(0.0);
                assessment.setAverage(average); // Mettre à jour la moyenne
            }

            // Persister les changements
            entityManager.merge(assessment);
            transaction.commit();
            System.out.println("Moyenne mise à jour pour l'évaluation avec ID " + idAssessment);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    //Vérifie si il existe une évaluation ayant déjà ce nom dans le cours
    public boolean checkAssessmentExists(Course course, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        String queryStr = "SELECT COUNT(a) FROM Assessment a WHERE a.course.idCourse = :idCourse AND a.name = :name";
        Long count = (Long) entityManager.createQuery(queryStr)
                .setParameter("idCourse", course.getIdCourse())
                .setParameter("name", name)
                .getSingleResult();

        entityManager.close();

        return count > 0; // Retourne true si l'évaluation existe déjà
    }

    //Calcule la meilleure et la pire note
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

        return result; // Map contenant {idAssessment -> {"min" -> valeur, "max" -> valeur}}
    }

    public Map<Assessment, Double> getAssessmentsAndGradesByCourseAndStudent(int courseId, int studentId) {
        Map<Assessment, Double> assessmentsWithGrades = new HashMap<>();

        String hql = " SELECT a, g.grade FROM Assessment a LEFT JOIN Grade g ON g.assessment.idAssessment = a.idAssessment AND g.student.idStudent = :studentId WHERE a.course.idCourse = :courseId ";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.query.Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("courseId", courseId);
            query.setParameter("studentId", studentId);

            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                Assessment assessment = (Assessment) row[0];
                Double grade = (Double) row[1];
                assessmentsWithGrades.put(assessment, grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assessmentsWithGrades;
    }


    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }



}
