package services;

import jakarta.persistence.*;
import models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;


public class CourseService {
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Initialize the EntityManagerFactory and the SessionFactory
    public CourseService() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation de l'EntityManagerFactory ou SessionFactory : " + e.getMessage());
        }
    }

    //Creation of a course
    public void createCourse(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Creation of the Race object
            Course course = new Course();
            course.setName(name);
            entityManager.persist(course);
            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();

        } finally {
            entityManager.close();
        }
    }

    // Update course information
    public void updateCourse(Integer idCourse, String courseName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Get the course
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                // Mise à jour des informations du cours
                course.setName(courseName);
                entityManager.merge(course);
                transaction.commit();
            } else {
                System.out.println("Course not found with ID: " + idCourse);
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

    //Delete a course
    public void deleteCourse(Integer idCourse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Get the course
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                // Retire les étudiants du cours
                List<Student> students = course.getStudentList();
                for (Student student : students) {
                    student.getCourseList().remove(course);
                }

                // Remove the teacher from the class
                Teacher teacher = course.getTeacher();
                if (teacher != null) {
                    teacher.getCourseList().remove(course);
                    course = entityManager.merge(course);
                }
                // Delete the course
                entityManager.remove(course);

                System.out.println("Course deleted with ID: " + idCourse);
            } else {
                System.out.println("Course not found with ID: " + idCourse);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Retrieve a course by its identifier
    public Course readCourse(int idCourse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Course course = null;

        try {
            course = entityManager.find(Course.class, idCourse);
        } finally {
            entityManager.close();
        }

        return course;
    }

    // Retrieve all courses
    public List<Course> readCourseList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Course> courses = new ArrayList<>();

        try {
            courses = entityManager.createQuery("FROM Course", Course.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return courses;
    }

    //Calculate the total course average
    public double calculateCourseAverage(int courseId) {
        double total = 0;
        int count = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //Calculate the total course average
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
        // Return -1.0 if no rating
        return count > 0 ? total / count : -1.0;
    }

    // Calculate the average of a student in a course
    public double calculateStudentAverageInCourse(int courseId, int studentId) {
        double total = 0;
        int count = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Retrieve all grades for a student in a course
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

        // Return -1.0 if no rating
        return count > 0 ? total / count : -1.0;
    }

    // Flexible search method for course
    public List<Course> searchCourse(String searchTerm) {
        Session session = sessionFactory.openSession();
        List<Course> courses = null;

        try {
            System.out.println("Executing search for: " + searchTerm);

            String hql = "FROM Course c WHERE LOWER(c.name) LIKE LOWER(:searchTerm) ";
            TypedQuery<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("searchTerm",searchTerm.toLowerCase() + "%");

            courses = query.getResultList();

            System.out.println("Search results for '" + searchTerm + "':");
            courses.forEach(course -> System.out.println("Found: " + course.getName() + " " + course.getTeacher()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        // Return an empty list if no results
        return courses != null ? courses : List.of();
    }

    // Close the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}