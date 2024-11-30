package services;
import models.Grade;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;


public class GradeService {
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    // Constructeur pour initialiser l'EntityManagerFactory
    public GradeService() {
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

    public List<Grade> readGrade(int idStudent, int idCourse) {
        // Ouverture de la session
        Session session = sessionFactory.openSession();
        List<Grade> grades = null;

        try {
            // Requête HQL pour récupérer les grades correspondant à l'étudiant et au cours spécifiés
            // La jointure via l'entité Assessment pour accéder à l'idCourse
            String hql = "FROM Grade g " +
                    "WHERE g.student.idStudent = :idStudent " +
                    "AND g.assessment.course.idCourse = :idCourse";
            Query<Grade> query = session.createQuery(hql, Grade.class);

            // Définit les paramètres de la requête
            query.setParameter("idStudent", idStudent);
            query.setParameter("idCourse", idCourse);

            // Exécute la requête et récupère les résultats
            grades = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return grades; // Retourne la liste des grades
    }
    public float calculateAverage(int idStudent, int idCourse) {
        // Ouverture de la session
        Session session = sessionFactory.openSession();
        float average = 0.0f;
        int count = 0;

        try {
            // Requête HQL pour récupérer les grades correspondant à l'étudiant et au cours spécifiés
            String hql = "FROM Grade g " +
                    "WHERE g.student.idStudent = :idStudent " +
                    "AND g.assessment.course.idCourse = :idCourse";
            Query<Grade> query = session.createQuery(hql, Grade.class);

            // Définit les paramètres de la requête
            query.setParameter("idStudent", idStudent);
            query.setParameter("idCourse", idCourse);

            // Récupère les résultats
            List<Grade> grades = query.list();

            if (!grades.isEmpty()) {
                float sum = 0.0f;
                for (Grade grade : grades) {
                    sum += grade.getGrade();  // Additionner toutes les notes
                    count++;  // Compter le nombre de notes
                }
                average = sum / count;  // Calculer la moyenne
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return average;  // Retourne la moyenne
    }

    /*public Map<Student, Float> readGradeStudent(Course course) {
        // Ouverture de la session Hibernate
        Session session = sessionFactory.openSession();
        Map<Student, Float> gradeMap = new HashMap<>();

        try {
            // Requête HQL pour récupérer les étudiants inscrits dans ce cours
            String hql = "SELECT g.idStudent FROM Grade g WHERE g.idCourse = :courseId";
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("courseId", course.getIdCourse());

            // Exécute la requête pour récupérer les IDs des étudiants
            List<Integer> studentIds = query.list();

            // Pour chaque étudiant récupéré, on crée un objet Student et on calcule la moyenne
            for (Integer studentId : studentIds) {
                // Création de la requête pour récupérer les informations complètes sur l'étudiant
                String studentHql = "FROM Student s WHERE s.idStudent = :studentId";
                Query<Student> studentQuery = session.createQuery(studentHql, Student.class);
                studentQuery.setParameter("studentId", studentId);

                Student student = studentQuery.uniqueResult();  // Récupère l'objet Student complet

                // Calcul de la moyenne pour cet étudiant
                float average = calculateAverage(studentId, course.getIdCourse());

                // Ajout de l'étudiant et de sa moyenne dans la Map
                gradeMap.put(student, average);
            }
        } finally {
            session.close(); // Ferme la session après l'exécution
        }

        return gradeMap; // Retourne la Map des étudiants et de leurs moyennes
    }*/ // revoir cette méthode en fonction de l'affichage

    public List<Grade> readTranscript(int idStudent) {
        // Ouverture de la session Hibernate
        Session session = sessionFactory.openSession();
        List<Grade> transcript = new ArrayList<>();

        try {
            // Récupérer les cours associés à cet étudiant (par exemple depuis la liste des cours dans le champ courseList de Student)
            String hqlCourses = "SELECT c.idCourse FROM Course c JOIN c.studentList s WHERE s.idStudent = :idStudent";
            Query<Integer> courseQuery = session.createQuery(hqlCourses, Integer.class);
            courseQuery.setParameter("idStudent", idStudent);

            // Exécuter la requête pour récupérer les IDs des cours
            List<Integer> courseIds = courseQuery.list();

            // Pour chaque cours, obtenir les grades de l'étudiant
            for (Integer courseId : courseIds) {
                List<Grade> grades = readGrade(idStudent, courseId);
                transcript.addAll(grades);  // Ajouter toutes les notes de ce cours à la liste du transcript
            }
        } finally {
            session.close();  // Fermer la session
        }

        return transcript;  // Retourner la liste complète des grades de l'étudiant
    }

    // Ferme l'EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
