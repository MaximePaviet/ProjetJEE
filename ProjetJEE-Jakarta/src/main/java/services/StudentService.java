package services;

import jakarta.persistence.*;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.sql.Date;
import java.util.*;


public class StudentService {

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Constructor to initialize the EntityManagerFactory and the SessionFactory
    public StudentService() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation de l'EntityManagerFactory ou SessionFactory : " + e.getMessage());
        }
    }

    //Creation of a student
    public Map<String, String> createStudent(String name, String surname, Date dateBirth, String contact, String schoolYear) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Map<String, String> generatedInfo = new HashMap<>();

        try {
            transaction.begin();//

            // Generate the unique login and password
            AdministratorService administratorService = new AdministratorService();
            String login = administratorService.generateUniqueLoginStudent(name, surname);
            String password = administratorService.generatePassword();

            // Creation of the student
            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            student.setDateBirth(dateBirth);
            student.setContact(contact);
            student.setSchoolYear(schoolYear);
            student.setLogin(login);
            student.setPassword(password);
            entityManager.persist(student);
            transaction.commit();

            // Add the information to the database
            generatedInfo.put("login", login);
            generatedInfo.put("password", password);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return generatedInfo;
    }


    // Update a student's information
    public void updateStudent(Integer id, String name, String surname, Date dateBirth, String contact, String schoolYear) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();//

            // Get the student
            Student student = entityManager.find(Student.class, id);
            if (student != null) {
                // Update student information
                student.setName(name);
                student.setSurname(surname);
                student.setDateBirth(dateBirth);
                student.setContact(contact);
                student.setSchoolYear(schoolYear);
                entityManager.persist(student);
                transaction.commit();
            } else {
                System.out.println("Student not found with ID: " + id);
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    //Supprimer un étudiant
    public void deleteStudent(Integer idStudent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Get the student
            Student student = entityManager.find(Student.class, idStudent);
            if (student != null) {
                //Delete the student from the database
                entityManager.remove(student);
                transaction.commit();
                System.out.println("Student deleted with ID: " + idStudent);
            } else {
                System.out.println("Student not found with ID: " + idStudent);
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Retrieve all students
    public List<Student> readStudentList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Student> students = new ArrayList<>();

        try {
            students = entityManager.createQuery("FROM Student", Student.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return students;
    }

    // Retrieve a student
    public Student readStudent(Integer idStudent) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Student student = null;

        try {
            student = entityManager.find(Student.class, idStudent);
        } finally {
            entityManager.close();
        }

        return student;
    }

    // Flexible search for students by last name, first name or contact
    public List<Student> searchStudent(String searchTerm) {
        Session session = sessionFactory.openSession();
        List<Student> students = null;

        try {
            System.out.println("Executing search for: " + searchTerm);

            String hql = "FROM Student t WHERE LOWER(t.name) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.surname) LIKE LOWER(:searchTerm) " +
                    "OR LOWER(t.contact) LIKE LOWER(:searchTerm)";
            TypedQuery<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("searchTerm",searchTerm.toLowerCase() + "%");

            students = query.getResultList();

            System.out.println("Search results for '" + searchTerm + "':");
            students.forEach(student -> System.out.println("Found: " + student.getName() + " " + student.getSurname()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        // Return an empty list if no results
        return students != null ? students : List.of();
    }

    //Registration of a student for a course
    public void registrationCourse(Course course, Student student) {
        if (course == null || student == null) {
            throw new IllegalArgumentException("Course and Student cannot be null");
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Retrieve the student and the course from the database
            Student existingStudent = session.get(Student.class, student.getIdStudent());
            if (existingStudent == null) {
                throw new IllegalArgumentException("Student not found with ID: " + student.getIdStudent());
            }

            Course existingCourse = session.get(Course.class, course.getIdCourse());
            if (existingCourse == null) {
                throw new IllegalArgumentException("Course not found with ID: " + course.getIdCourse());
            }

            // Check if the student is already registered for this course
            if (!existingStudent.getCourseList().contains(existingCourse)) {
                // Add the course to the student's course list
                existingStudent.getCourseList().add(existingCourse);

                // Add the student to the course student list
                existingCourse.getStudentList().add(existingStudent);

                // Update information in the database
                session.update(existingStudent);
                session.update(existingCourse);
            } else {
                System.out.println("Student is already assigned to the course.");
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

    //Retrieve students from one or more promotions
    public List<Student> getStudentsByPromo(String[] promos) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Student> students = session.createQuery("FROM Student WHERE schoolYear IN :promos", Student.class)
                .setParameter("promos", Arrays.asList(promos))
                .getResultList();

        session.getTransaction().commit();
        session.close();

        return students;
    }

    // Close the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }



}
