package services;

import jakarta.persistence.*;
import models.Assessment;
import models.Grade;
import models.Student;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AssessmentService {

    //Initializing the EntityManagerFactory
    public AssessmentService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    // Creation of an assessment
    public void createAssessment(Course course, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Check if the Course is valid
            if (course == null || course.getIdCourse() == 0) {
                System.out.println("Course is null or has idCourse = 0");
                throw new IllegalArgumentException("Invalid Course object provided.");
            }

            // Check if the Course exists in the database
            Course persistedCourse = entityManager.find(Course.class, course.getIdCourse());
            if (persistedCourse == null) {
                throw new IllegalArgumentException("Course with ID " + course.getIdCourse() + " not found in the database.");
            }

            // Adding the assessment to the database
            Assessment assessment = new Assessment();
            assessment.setCourse(persistedCourse);
            assessment.setName(name);
            entityManager.persist(assessment);
            transaction.commit();
            System.out.println("Assessment created successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    //Creating a note for an assessment
    public void createGrade(Student student, int idAssessment, float gradeValue) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Retrieve the assessment
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Retrieve the student
            Student persistedStudent = entityManager.find(Student.class, student.getIdStudent());
            if (persistedStudent == null) {
                throw new IllegalArgumentException("Student with ID " + student.getIdStudent() + " not found.");
            }

            // Adding the note to the database
            Grade grade = new Grade();
            grade.setStudent(persistedStudent);
            grade.setAssessment(assessment);
            grade.setGrade(gradeValue);

            // Add the grade to the evaluation
            assessment.getGradeList().add(grade);
            entityManager.persist(grade);
            entityManager.merge(assessment);
            transaction.commit();

            // Updates the rating average
            updateAssessmentAverage(idAssessment);

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // Updates the rating average
    public void updateAssessmentAverage(int idAssessment) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Retrieve the evaluation
            Assessment assessment = entityManager.find(Assessment.class, idAssessment);
            if (assessment == null) {
                throw new IllegalArgumentException("Assessment with ID " + idAssessment + " not found.");
            }

            // Calculate the average of the evaluation scores
            List<Grade> gradeList = assessment.getGradeList();
            if (gradeList == null || gradeList.isEmpty()) {
                assessment.setAverage(-1.0);
            } else {
                double average = gradeList.stream()
                        .mapToDouble(Grade::getGrade)
                        .average()
                        .orElse(-14.0);
                assessment.setAverage(average);
            }

            // Add changes to the database
            entityManager.merge(assessment);
            transaction.commit();
            System.out.println("Moyenne mise à jour pour l'évaluation avec ID " + idAssessment);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    //Check if there is already an assessment with this name in the course
    public boolean checkAssessmentExists(Course course, String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        String queryStr = "SELECT COUNT(a) FROM Assessment a WHERE a.course.idCourse = :idCourse AND a.name = :name";
        Long count = (Long) entityManager.createQuery(queryStr)
                .setParameter("idCourse", course.getIdCourse())
                .setParameter("name", name)
                .getSingleResult();

        entityManager.close();

        return count > 0;
    }

    //Calculate the best and worst rating
    public Map<Integer, Map<String, Float>> calculateMinMaxGrades(List<Assessment> assessments) {
        Map<Integer, Map<String, Float>> result = new HashMap<>();

        for (Assessment assessment : assessments) {
            Map<String, Float> minMax = new HashMap<>();
            List<Grade> grades = assessment.getGradeList();

            if (grades == null || grades.isEmpty()) {
                minMax.put("min", null);
                minMax.put("max", null);
            } else {
                float min = (float) grades.stream().mapToDouble(Grade::getGrade).min().orElse(0.0);
                float max = (float) grades.stream().mapToDouble(Grade::getGrade).max().orElse(0.0);
                minMax.put("min", min);
                minMax.put("max", max);
            }
            result.put(assessment.getIdAssessment(), minMax);
        }

        return result;
    }

    //Retrieve grades and associated assessments for a student for a course
    public Map<Assessment, Double> getAssessmentsAndGradesByCourseAndStudent(int courseId, int studentId) {
        Map<Assessment, Double> assessmentsWithGrades = new HashMap<>();

        String hql = " SELECT a, g.grade FROM Assessment a LEFT JOIN Grade g ON g.assessment.idAssessment = a.idAssessment AND g.student.idStudent = :studentId WHERE a.course.idCourse = :courseId ";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.query.Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("courseId", courseId);
            query.setParameter("studentId", studentId);

            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                Assessment assessment = (Assessment) row[0];
                Double grade = (Double) row[1];
                assessmentsWithGrades.put(assessment, grade);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assessmentsWithGrades;
    }


    // Close the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }



}
