package services;

import jakarta.persistence.*;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;



public class StudentService {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    // Constructeur pour initialiser l'EntityManagerFactory
    public StudentService() {
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
    public void createStudent(String name, String surname, Date dateBirth, String contact, String schoolYear) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Génère le login unique et le mot de passe
            AdministratorService administratorService = new AdministratorService();
            String login = administratorService.generateUniqueLoginStudent(name, surname);
            String password = administratorService.generatePassword();

            // Création de l'objet Student
            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            student.setDateBirth(dateBirth);
            student.setContact(contact);
            student.setSchoolYear(schoolYear);
            student.setLogin(login);
            student.setPassword(password);


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
    public void updateStudent(Integer id, String name, String surname, Date dateBirth, String contact, String schoolYear) {
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
                student.setDateBirth(dateBirth);
                student.setContact(contact);
                student.setSchoolYear(schoolYear);

                entityManager.persist(student);  // Met à jour l'objet Student dans la base de données
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

    // Méthode de recherche flexible pour les enseignants par nom, prénom ou contact
    public List<Student> searchStudent(String searchTerm) {
        // Utilisation de sessionFactory pour obtenir la session
        Session session = sessionFactory.openSession();
        List<Student> students = null;

        try {
            System.out.println("Executing search for: " + searchTerm);

            // Requête HQL avec gestion de la sensibilité à la casse
            String hql = "FROM Student t WHERE LOWER(t.name) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.surname) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.contact) LIKE LOWER(:searchTerm)";
            TypedQuery<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");

            // Exécuter la requête
            students = query.getResultList();

            // Journal des résultats
            System.out.println("Search results for '" + searchTerm + "':");
            students.forEach(student -> System.out.println("Found: " + student.getName() + " " + student.getSurname()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return students != null ? students : List.of(); // Retourne une liste vide si aucun résultat
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

    public void registrationCourse(Course course, Student student) {
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

    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }



}
