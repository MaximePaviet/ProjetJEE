package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Grade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createCourse(String name) {
        try {
            // Création de l'objet Course
            Course course = new Course();
            course.setName(name);

            // Persiste l'objet Course dans la base de données
            entityManager.persist(course);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création du cours : " + e.getMessage());
        }
    }

    @Transactional
    public void updateCourse(Integer idCourse, String courseName) {
        try {
            // Recherche du cours par son ID
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                // Mise à jour des informations du cours
                course.setName(courseName);
                entityManager.merge(course);
            } else {
                throw new RuntimeException("Cours non trouvé avec l'ID : " + idCourse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du cours : " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCourse(Integer idCourse) {
        try {
            // Recherche du cours par son ID
            Course course = entityManager.find(Course.class, idCourse);
            if (course != null) {
                entityManager.remove(course);
            } else {
                throw new RuntimeException("Cours non trouvé avec l'ID : " + idCourse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du cours : " + e.getMessage());
        }
    }

    public Course readCourse(int idCourse) {
        try {
            return entityManager.find(Course.class, idCourse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture du cours : " + e.getMessage());
        }
    }

    public List<Course> readCourseList() {
        try {
            TypedQuery<Course> query = entityManager.createQuery("FROM Course", Course.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération de la liste des cours : " + e.getMessage());
        }
    }

    public double calculateCourseAverage(int courseId) {
        try {
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT AVG(g.grade) FROM Grade g WHERE g.assessment.course.idCourse = :courseId", Double.class);
            query.setParameter("courseId", courseId);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public double calculateStudentAverageInCourse(int courseId, int studentId) {
        try {
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT AVG(g.grade) FROM Grade g WHERE g.assessment.course.idCourse = :courseId " +
                            "AND g.student.idStudent = :studentId", Double.class);
            query.setParameter("courseId", courseId);
            query.setParameter("studentId", studentId);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
