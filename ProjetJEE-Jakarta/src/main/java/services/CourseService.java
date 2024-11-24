package services;

import jakarta.persistence.*;
import models.Course;
import org.hibernate.SessionFactory;

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
    public void updateCourse(Integer idCourse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Recherche d'étudiant par son ID
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                // Mise à jour des informations de l'étudiant
                course.setIdCourse(idCourse);
                

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
    /*
    public void assignmentStudentToCourse(Course course, Student student) {
        if (course == null || student == null) {
            throw new IllegalArgumentException("Course and Student cannot be null");
        }

        // Ouvrir une session Hibernate
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // Commencer une transaction
            transaction = session.beginTransaction();

            // Récupérer l'étudiant et le cours depuis la base de données
            Student existingStudent = session.get(Student.class, student.getIdStudent());
            if (existingStudent == null) {
                throw new IllegalArgumentException("Student not found with ID: " + student.getIdStudent());
            }

            Course existingCourse = session.get(Course.class, course.getIdCourse());
            if (existingCourse == null) {
                throw new IllegalArgumentException("Course not found with ID: " + course.getIdCourse());
            }

            // Vérifier si l'étudiant est déjà inscrit à ce cours
            if (!existingStudent.getCourseList().contains(existingCourse)) {
                // Ajouter le cours à la liste des cours de l'étudiant
                existingStudent.getCourseList().add(existingCourse);

                // Ajouter l'étudiant à la liste des étudiants du cours
                existingCourse.getStudentList().add(existingStudent);

                // Mettre à jour les entités dans la base de données
                session.update(existingStudent);
                session.update(existingCourse);
            } else {
                System.out.println("Student is already assigned to the course.");
            }

            // Commit de la transaction
            transaction.commit();
        } catch (Exception e) {
            // Si une erreur survient, rollback la transaction
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Fermer la session
            session.close();
        }
    }*/
}