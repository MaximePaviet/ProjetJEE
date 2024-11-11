package controller;

import jakarta.persistence.*;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class TeacherController {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public TeacherController() {
        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit"); // à modifier par le nom de la mappe
    }

    // Méthode pour créer un nouvel enseignant
    public void createTeacher(String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Création de l'objet Teacher
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setContact(contact);

            entityManager.persist(teacher);    // Persiste l'objet Teacher dans la base de données
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

    // Méthode pour récupérer les informations d'un enseignant par son identifiant
    public Teacher readTeacher(int idTeacher) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Teacher teacher = null;

        try {
            teacher = entityManager.find(Teacher.class, idTeacher); // Recherche l'enseignant par ID
        } finally {
            entityManager.close(); // Ferme l'EntityManager
        }

        return teacher; // Retourne l'objet Teacher trouvé ou null si non trouvé
    }

    // Méthode de recherche flexible pour les enseignants par nom, prénom ou contact
    public List<Teacher> searchTeacher(String searchTerm) {
        // Utilisation de sessionFactory pour obtenir la session
        Session session = sessionFactory.openSession();  // SessionFactory est supposé être injecté ou défini dans la classe
        List<Teacher> teachers = null;

        try {
            // Requête HQL pour rechercher les enseignants dont le nom, prénom ou contact contient la chaîne de recherche
            //String hql = "FROM Teacher t WHERE t.name LIKE :searchTerm OR t.surname LIKE :searchTerm OR t.contact LIKE :searchTerm";
            //Query<Teacher> query = session.createQuery(hql, Teacher.class);

            // Ajoute des jokers (%) de chaque côté de searchTerm pour permettre la recherche partielle
            //query.setParameter("searchTerm", "%" + searchTerm + "%");

            // Exécute la requête et récupère les résultats dans une liste
            //teachers = query.list();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return teachers; // Retourne la liste des enseignants
    }




    // Méthode pour mettre à jour les informations d'un enseignant avec des paramètres spécifiques
    public void updateTeacher(Integer id, String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();// Démarre la transaction

            // Recherche de l'enseignant par son ID
            Teacher teacher = entityManager.find(Teacher.class, id);
            if (teacher != null) {
                // Mise à jour des informations de l'enseignant
                teacher.setName(name);
                teacher.setSurname(surname);
                teacher.setContact(contact);

                entityManager.merge(teacher);  // Met à jour l'objet Teacher dans la base de données
                transaction.commit();          // Valide la transaction
            } else {
                System.out.println("Teacher not found with ID: " + id);
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

    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}

