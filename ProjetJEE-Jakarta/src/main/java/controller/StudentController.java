package controller;

import jakarta.persistence.*;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



public class StudentController {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    // Constructeur pour initialiser l'EntityManagerFactory
    public StudentController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }
    public void createStudent(String name, String surname, Date dateBirth, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Création de l'objet Student
            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            student.setDateBirth(dateBirth.toLocalDate());
            student.setContact(contact);


            entityManager.persist(student);    // Persiste l'objet Student dans la base de données
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

    // Méthode pour mettre à jour les informations d'un étudiant avec des paramètres spécifiques
    public void updateStudent(Integer id, String name, String surname, Date dateBirth, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Recherche d'étudiant par son ID
            Student student = entityManager.find(Student.class, id);
            if (student != null) {
                // Mise à jour des informations de l'étudiant
                student.setName(name);
                student.setSurname(surname);
                student.setDateBirth(dateBirth.toLocalDate());
                student.setContact(contact);

                entityManager.merge(student);  // Met à jour l'objet Student dans la base de données
                transaction.commit();          // Valide la transaction
            } else {
                System.out.println("Student not found with ID: " + id);
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

    // Méthode pour supprimer un étudiant par son identifiant
    public void deleteStudent(Integer idStudent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarre la transaction

            // Recherche d'étudiant par son ID
            Student student = entityManager.find(Student.class, idStudent);
            if (student != null) {
                entityManager.remove(student); // Met à jour l'objet Student dans la base de données
                transaction.commit();          // Valide la transaction
                System.out.println("Student deleted with ID: " + idStudent);
            } else {
                System.out.println("Student not found with ID: " + idStudent);
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

    // Méthode pour récupérer tous les étudiants
    public List<Student> readStudentList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Student> students = new ArrayList<>();

        try {
            // Utilise HQL pour récupérer tous les étudiants
            students = entityManager.createQuery("FROM Student", Student.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }

        return students;
    }

    // Méthode pour récupérer les informations d'un étudiant par son identifiant
    public Student readStudent(Integer idStudent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Student student = null;

        try {
            student = entityManager.find(Student.class, idStudent); // Recherche le student par ID
        } finally {
            entityManager.close();// Ferme l'EntityManager
        }

        return student;// Retourne l'objet Student trouvé
    }

    // Méthode de recherche flexible pour les étudiants par nom, prénom ou contact
    public List<Student> searchStudent(String searchTerm) {
        // Utilisation de sessionFactory pour obtenir la session
        Session session = sessionFactory.openSession(); // SessionFactory est supposé être injecté ou défini dans la classe
        List<Student> students = null;

        try {
            // Requête HQL pour rechercher les étudiants dont le nom, prénom ou contact contient la chaîne de recherche
            String hql = "FROM Student s WHERE s.name LIKE :searchTerm OR s.surname LIKE :searchTerm OR s.contact LIKE :searchTerm";
            TypedQuery<Student> query = session.createQuery(hql, Student.class);

            // Ajoute des jokers (%) de chaque côté de searchTerm pour permettre la recherche partielle
            query.setParameter("searchTerm", "%" + searchTerm + "%");

            // Exécute la requête et récupère les résultats dans une liste
            students = query.getResultList();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return students; // Retourne la liste des étudiants
    }
    public List<Student> filtering(Course course) {
        // Utilisation de sessionFactory pour obtenir la session
        Session session = sessionFactory.openSession(); // SessionFactory est supposé être injecté dans la classe
        List<Student> students = null;

        try {
            // Requête HQL pour récupérer les étudiants liés au cours spécifié
            String hql = "SELECT s FROM Student s JOIN s.courseList c WHERE c = :course";
            TypedQuery<Student> query = session.createQuery(hql, Student.class);

            // Définit le paramètre course
            query.setParameter("course", course);

            // Exécute la requête et récupère les résultats dans une liste
            students = query.getResultList();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return students; // Retourne la liste des étudiants
    }
    public void registrationCourse(int idStudent, int idCourse) {
        // Ouverture de la session
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // Début de la transaction
            transaction = session.beginTransaction();

            // Récupère l'objet Student par son ID
            Student student = session.get(Student.class, idStudent);
            if (student == null) {
                throw new IllegalArgumentException("Aucun étudiant trouvé avec l'ID : " + idStudent);
            }

            // Récupère l'objet Course par son ID
            Course course = session.get(Course.class, idCourse);
            if (course == null) {
                throw new IllegalArgumentException("Aucun cours trouvé avec l'ID : " + idCourse);
            }

            // Ajoute le cours à la liste des cours de l'étudiant
            if (!student.getCourseList().contains(course)) { // Évite les doublons
                student.getCourseList().add(course);
            }

            // Met à jour l'étudiant dans la base de données
            session.update(student);

            // Valide la transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Annule la transaction en cas d'erreur
            }
            throw e; // Relance l'exception pour gestion dans la couche supérieure
        } finally {
            session.close(); // Ferme la session
        }
    }
    public List<Course> readCourse(int idStudent) {
        // Ouverture de la session
        Session session = sessionFactory.openSession();
        List<Course> courses = null;

        try {
            // Récupère l'objet Student par son ID
            Student student = session.get(Student.class, idStudent);

            if (student == null) {
                throw new IllegalArgumentException("Aucun étudiant trouvé avec l'ID : " + idStudent);
            }

            // Récupère la liste des cours de l'étudiant
            courses = student.getCourseList();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return courses; // Retourne la liste des cours
    }
    public static void main(String[] args) {
        // Initialisation du StudentController
        StudentController studentController = new StudentController();

        // Test de la création d'un étudiant
        System.out.println("### Test de création d'un étudiant ###");
        studentController.createStudent("John", "Doe", java.sql.Date.valueOf(LocalDate.of(2000, 1, 1)), "john.doe@example.com");
        List<Student> students = studentController.readStudentList();
        System.out.println("Étudiants après création : " + students);

        // Test de mise à jour d'un étudiant
        System.out.println("\n### Test de mise à jour d'un étudiant ###");
        Student studentToUpdate = students.get(0);
        studentController.updateStudent(studentToUpdate.getIdStudent(), "John", "Smith", java.sql.Date.valueOf(LocalDate.of(2000, 1, 1)), "john.smith@example.com");
        Student updatedStudent = studentController.readStudent(studentToUpdate.getIdStudent());
        System.out.println("Étudiant mis à jour : " + updatedStudent);

        // Test de suppression d'un étudiant
        System.out.println("\n### Test de suppression d'un étudiant ###");
        studentController.deleteStudent(studentToUpdate.getIdStudent());
        Student deletedStudent = studentController.readStudent(studentToUpdate.getIdStudent());
        System.out.println("Étudiant après suppression : " + deletedStudent);

        // Test de recherche d'étudiant
        System.out.println("\n### Test de recherche d'un étudiant ###");
        studentController.createStudent("Jane", "Doe", java.sql.Date.valueOf(LocalDate.of(2001, 5, 15)), "jane.doe@example.com");
        List<Student> searchResults = studentController.searchStudent("Jane");
        System.out.println("Résultats de la recherche : " + searchResults);

        // Test du filtrage des étudiants inscrits à un cours
        System.out.println("\n### Test du filtrage des étudiants par cours ###");
        Course course = new Course();  // Créez un cours ou récupérez-le de la base
        studentController.createStudent("Tom", "Riddle", java.sql.Date.valueOf(LocalDate.of(1997, 12, 5)), "tom.riddle@example.com");
        Student student = studentController.readStudentList().get(0);
        studentController.registrationCourse(student.getIdStudent(), course.getIdCourse());
        List<Student> studentsInCourse = studentController.filtering(course);
        System.out.println("Étudiants inscrits au cours : " + studentsInCourse);

        // Test de la récupération des cours d'un étudiant
        System.out.println("\n### Test de la récupération des cours d'un étudiant ###");
        List<Course> courses = studentController.readCourse(student.getIdStudent());
        System.out.println("Cours de l'étudiant : " + courses);

        // Test de l'inscription d'un étudiant à un cours
        System.out.println("\n### Test de l'inscription d'un étudiant à un cours ###");
        studentController.registrationCourse(student.getIdStudent(), course.getIdCourse());
        List<Student> studentsInCourseAfterRegistration = studentController.filtering(course);
        System.out.println("Étudiants inscrits au cours après inscription : " + studentsInCourseAfterRegistration);
    }







}
