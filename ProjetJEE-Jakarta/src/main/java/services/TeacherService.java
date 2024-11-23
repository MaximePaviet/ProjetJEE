package services;

import jakarta.persistence.*;
import models.Course;

import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TeacherService {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public TeacherService() {
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


    // Méthode pour créer un nouvel enseignant
    public void createTeacher(String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin(); // Démarre la transaction

            // Génère le login unique et le mot de passe
            AdministratorService administratorService = new AdministratorService();
            String login = administratorService.generateUniqueLogin(name, surname);
            String password = administratorService.generatePassword();

            // Création de l'objet Teacher
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setContact(contact);
            teacher.setLogin(login);
            teacher.setPassword(password);

            // Persiste l'objet Teacher dans la base de données
            entityManager.persist(teacher);
            transaction.commit(); // Valide la transaction

            System.out.println("Teacher created successfully with login: " + login + " and password: " + password);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Annule la transaction en cas d'erreur
            }
            e.printStackTrace();
        } finally {
            entityManager.close(); // Ferme l'EntityManager pour libérer les ressources
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
        Session session = sessionFactory.openSession();
        List<Teacher> teachers = null;

        try {
            System.out.println("Executing search for: " + searchTerm);

            // Requête HQL avec gestion de la sensibilité à la casse
            String hql = "FROM Teacher t WHERE LOWER(t.name) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.surname) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.contact) LIKE LOWER(:searchTerm)";
            TypedQuery<Teacher> query = session.createQuery(hql, Teacher.class);
            query.setParameter("searchTerm", "%" + searchTerm.toLowerCase() + "%");

            // Exécuter la requête
            teachers = query.getResultList();

            // Journal des résultats
            System.out.println("Search results for '" + searchTerm + "':");
            teachers.forEach(teacher -> System.out.println("Found: " + teacher.getName() + " " + teacher.getSurname()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return teachers != null ? teachers : List.of(); // Retourne une liste vide si aucun résultat
    }


    // Méthode pour mettre à jour les informations d'un enseignant avec des paramètres spécifiques
    public void updateTeacher(Integer id, String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Recherche de l'enseignant par son ID
            Teacher teacher = entityManager.find(Teacher.class, id);
            if (teacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + id);
            }

            // Validation des champs obligatoires
            if (name == null && surname == null && contact == null) {
                throw new IllegalArgumentException("At least one field (name, surname, or contact) must be provided");
            }

            // Mise à jour des champs non-nuls
            if (name != null) {
                teacher.setName(name);
            }
            if (surname != null) {
                teacher.setSurname(surname);
            }
            if (contact != null) {
                teacher.setContact(contact);
            }

            entityManager.merge(teacher); // Enregistrer les modifications
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Propager l'exception
        } finally {
            entityManager.close();
        }
    }




    public void assignmentCourseToTeacher(Teacher teacher, Course course) {

        if (teacher == null || course == null) {
            throw new IllegalArgumentException("Teacher and Course cannot be null");
        }

        // Ouvrir une session Hibernate
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // Commencer une transaction
            transaction = session.beginTransaction();

            // Récupérer le professeur et le cours depuis la base de données
            Teacher existingTeacher = session.get(Teacher.class, teacher.getIdTeacher());
            if (existingTeacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + teacher.getIdTeacher());
            }

            Course existingCourse = session.get(Course.class, course.getIdCourse());
            if (existingCourse == null) {
                throw new IllegalArgumentException("Course not found with ID: " + course.getIdCourse());
            }

            // Vérifier si le cours est déjà assigné au professeur
            if (!existingTeacher.getCourseList().contains(existingCourse)) {
                // Ajouter le cours à la liste des cours du professeur
                existingTeacher.getCourseList().add(existingCourse);

                // Associer le professeur au cours
                existingCourse.setTeacher(existingTeacher);

                // Mettre à jour les entités dans la base de données
                session.update(existingTeacher);
                session.update(existingCourse);
            } else {
                System.out.println("Course already assigned to the teacher.");
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



    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}

