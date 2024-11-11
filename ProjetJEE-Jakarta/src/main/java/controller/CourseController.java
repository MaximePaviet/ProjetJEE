package controller;

import jakarta.persistence.*;
import models.Course;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


public class CourseController{
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public CourseController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit"); // à modifier par le nom de la mappe
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
    public void assignmentTeacher(Teacher teacher, Course course) {
        // Assurez-vous que 'teacher' et 'course' ne sont pas null
        if (teacher == null || course == null) {
            throw new IllegalArgumentException("Teacher and Course cannot be null");
        }
        // Ouvrir une session avec une SessionFactory (supposée être injectée ou définie)
        Session session = sessionFactory.openSession(); // sessionFactory est supposé être déjà défini dans ta classe
        Transaction transaction = null;

        try {
            // Commencer une transaction
            transaction = session.beginTransaction();

            // Récupérer l'enseignant en utilisant son idTeacher
            Teacher existingTeacher = session.get(Teacher.class, teacher.getIdTeacher());
            if (existingTeacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + teacher.getIdTeacher());
            }

            // Ajouter le cours à la liste des cours de l'enseignant
            if (existingTeacher.getCourseList() == null) {
                existingTeacher.setCourseList(String.valueOf(new HashMap<>()));
            }

            // Ajouter le cours dans la map (le nom de la matière comme clé et l'objet Course comme valeur)
            //existingTeacher.getCourseList().put(course.getName(), course);


            // Sauvegarder l'enseignant avec les cours mis à jour
            session.update(existingTeacher);

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
    }

}