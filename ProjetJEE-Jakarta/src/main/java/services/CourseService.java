package services;

import jakarta.persistence.*;
import models.Assessment;
import models.Course;
import models.Grade;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;


public class CourseService {
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public CourseService() {
        try {
            // Initialisation de EntityManagerFactory pour JPA
            entityManagerFactory = Persistence.createEntityManagerFactory("default");

            // Initialisation de SessionFactory pour Hibernate
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure(); // Charge hibernate.cfg.xml
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation de l'EntityManagerFactory ou SessionFactory : " + e.getMessage());
        }
    }
    public void createCourse(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Création de l'objet Course
            Course course = new Course();
            course.setName(name);

            entityManager.persist(course);    // Persiste l'objet Course dans la base de données
            transaction.commit();              // Valide la transaction
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();        // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close();             // Ferme l'EntityManager pour libérer les ressources
        }
    }
    // Méthode pour mettre à jour les informations d'un cours avec des paramètres spécifiques
    public void updateCourse(Integer idCourse, String courseName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Recherche du cours par son ID
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                // Mise à jour des informations de l'étudiant
                course.setName(courseName);

                entityManager.merge(course);  // Met à jour l'objet Course dans la base de données
                transaction.commit();          // Valide la transaction
            } else {
                System.out.println("Course not found with ID: " + idCourse);
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }
    }
    // Méthode pour supprimer un cours par son identifiant
    public void deleteCourse(Integer idCourse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarre la transaction

            // Recherche de cours par son ID
            Course course= entityManager.find(Course.class, idCourse);
            if (course != null) {
                entityManager.remove(course); // Met à jour l'objet Course dans la base de données
                transaction.commit();          // Valide la transaction
                System.out.println("Course deleted with ID: " + idCourse);
            } else {
                System.out.println("Course not found with ID: " + idCourse);
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }
    }

    // Méthode pour récupérer les informations d'un cours par son identifiant
    public Course readCourse(int idCourse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Course course = null;

        try {
            course = entityManager.find(Course.class, idCourse); // Recherche le cours par ID
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }

        return course; // Retourne l'objet Course trouvé ou null si non trouvé
    }

    // Méthode pour récupérer tous les cours
    public List<Course> readCourseList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Course> courses = new ArrayList<>();

        try {
            // Utilise HQL pour récupérer tous les cours
            courses = entityManager.createQuery("FROM Course", Course.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }
        return courses;
    }

    //Calcule la moyenne totale du cours
    public double calculateCourseAverage(int courseId) {
        double total = 0;
        int count = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Requête pour obtenir les notes du cours
            String hql = "SELECT g.grade FROM Grade g WHERE g.assessment.course.idCourse = :courseId";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("courseId", courseId);

            List<Double> grades = query.getResultList();
            for (Double grade : grades) {
                total += grade;
                count++;
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        return count > 0 ? total / count : 0.0; // Retourne 0.0 si aucune note
    }

    // Calcule la moyenne d'un élève dans un cours
    public double calculateStudentAverageInCourse(int courseId, int studentId) {
        double total = 0;
        int count = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Requête pour obtenir les notes d'un étudiant spécifique dans un cours
            String hql = "SELECT g.grade FROM Grade g " +
                    "WHERE g.assessment.course.idCourse = :courseId " +
                    "AND g.student.idStudent = :studentId";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("courseId", courseId);
            query.setParameter("studentId", studentId);

            List<Double> grades = query.getResultList();
            for (Double grade : grades) {
                total += grade;
                count++;
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }

        return count > 0 ? total / count : 0.0; // Retourne 0.0 si aucune note
    }




    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}