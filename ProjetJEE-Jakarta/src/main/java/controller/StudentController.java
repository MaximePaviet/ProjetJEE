package controller;

import jakarta.persistence.*;
import models.Student;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class StudentController {

    private EntityManagerFactory entityManagerFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public StudentController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit"); // à modifier par le nom de la mappe
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
    public Student readStudent(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Student student = null;

        try {
            student = entityManager.find(Student.class, id); // Recherche le student par ID
        } finally {
            entityManager.close();// Ferme l'EntityManager
        }

        return student;// Retourne l'objet Student trouvé
    }

    // Méthode de recherche flexible pour les étudiants par nom, prénom ou contact
    public List<Student> searchStudent(String searchTerm) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Student> students= null;

        try {
            // Requête JPQL pour rechercher les étudiants dont le nom, prénom ou contact contient la chaîne de recherche
            String queryStr = "SELECT t FROM Student t WHERE t.name LIKE :searchTerm OR t.surname LIKE :searchTerm OR t.contact LIKE :searchTerm";
            TypedQuery<Student> query = entityManager.createQuery(queryStr, Student.class);

            // Ajoute des jokers (%) de chaque côté de searchTerm pour permettre la recherche partielle
            query.setParameter("searchTerm", "%" + searchTerm + "%");

            // Exécute la requête et récupère les résultats dans une liste
            students = query.getResultList();
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }

        return students; // Retourne la liste des étudiants
    }


}
