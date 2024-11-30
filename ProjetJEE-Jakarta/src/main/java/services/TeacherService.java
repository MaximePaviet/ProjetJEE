package services;

import jakarta.persistence.*;
import models.Course;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TeacherService {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructor to initialize the EntityManagerFactory
    public TeacherService() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation de l'EntityManagerFactory ou SessionFactory : " + e.getMessage());
        }
    }


    // Creation of a teacher
    public void createTeacher(String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Generate the unique login and password
            AdministratorService administratorService = new AdministratorService();
            String login = administratorService.generateUniqueLoginTeacher(name, surname);
            String password = administratorService.generatePassword();

            // Creation of the Teacher object
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setContact(contact);
            teacher.setLogin(login);
            teacher.setPassword(password);

            //Add the information to the database
            entityManager.persist(teacher);
            transaction.commit();
            System.out.println("Teacher created successfully with login: " + login + " and password: " + password);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Recovery of a teacher
    public Teacher readTeacher(int idTeacher) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Teacher teacher = null;

        try {
            teacher = entityManager.find(Teacher.class, idTeacher);
        } finally {
            entityManager.close();
        }

        return teacher;
    }

    // Recovery of all teachers
    public List<Teacher> readTeacherList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Teacher> teachers = new ArrayList<>();

        try {
            teachers = entityManager.createQuery("FROM Teacher", Teacher.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return teachers;
    }

    // Flexible search for teachers by last name, first name or contact
    public List<Teacher> searchTeacher(String searchTerm) {
        Session session = sessionFactory.openSession();
        List<Teacher> teachers = null;

        try {
            System.out.println("Executing search for: " + searchTerm);

            String hql = "FROM Teacher t WHERE LOWER(t.name) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.surname) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.contact) LIKE LOWER(:searchTerm)";
            TypedQuery<Teacher> query = session.createQuery(hql, Teacher.class);
            query.setParameter("searchTerm",searchTerm.toLowerCase() + "%");

            teachers = query.getResultList();

            System.out.println("Search results for '" + searchTerm + "':");
            teachers.forEach(teacher -> System.out.println("Found: " + teacher.getName() + " " + teacher.getSurname()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        // Return an empty list if no results
        return teachers != null ? teachers : List.of();
    }


    // Update a teacher's information
    public void updateTeacher(Integer id, String name, String surname, String contact) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Teacher recovery
            Teacher teacher = entityManager.find(Teacher.class, id);
            if (teacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + id);
            }

            if (name == null && surname == null && contact == null) {
                throw new IllegalArgumentException("At least one field (name, surname, or contact) must be provided");
            }

            // Update information
            if (name != null) {
                teacher.setName(name);
            }
            if (surname != null) {
                teacher.setSurname(surname);
            }
            if (contact != null) {
                teacher.setContact(contact);
            }
            entityManager.merge(teacher);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    //Registration of a teacher for a course
    public void assignmentCourseToTeacher(Teacher teacher, Course course) {

        if (teacher == null || course == null) {
            throw new IllegalArgumentException("Teacher and Course cannot be null");
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Retrieve the teacher and the course
            Teacher existingTeacher = session.get(Teacher.class, teacher.getIdTeacher());
            if (existingTeacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + teacher.getIdTeacher());
            }
            Course existingCourse = session.get(Course.class, course.getIdCourse());
            if (existingCourse == null) {
                throw new IllegalArgumentException("Course not found with ID: " + course.getIdCourse());
            }

            // Check if the course is already assigned to the teacher
            if (!existingTeacher.getCourseList().contains(existingCourse)) {
                // Add the course to the teacher's course list
                existingTeacher.getCourseList().add(existingCourse);

                // Associate the teacher with the course
                existingCourse.setTeacher(existingTeacher);

                // Update information in the database
                session.update(existingTeacher);
                session.update(existingCourse);
            } else {
                System.out.println("Course already assigned to the teacher.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Close the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}

