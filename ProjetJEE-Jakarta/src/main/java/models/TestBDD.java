package models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Date;
import java.time.LocalDate;


public class TestBDD {

    public static void main(String[] args) {
        new TestBDD().run();
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Créer la SessionFactory à partir de hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    private void run() {
        // Test d'insertion d'un individu avec Hibernate
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {

            Administrator administrator = new Administrator();
            administrator.setLogin("adminTest");
            administrator.setPassword("adminTest");

            Assessment assessment = new Assessment();
            assessment.setIdCourse(1);
            assessment.setName("QCMTest");
            assessment.setAverage(10.0);
            assessment.setGradeList("[10.0,11.0,9.0]");

            Course course = new Course();
            course.setIdTeacher(1);
            course.setName("coursTest");
            course.setStudentList("[1]");

            Grade grade = new Grade();
            grade.setIdStudent(1);
            grade.setIdAssessment(2);
            grade.setIdCourse(1);
            grade.setGrade(10.0);

            Student student = new Student();
            student.setName("Maxime");
            student.setSurname("Paviet");
            student.setDateBirth(LocalDate.of(2002,12,1));
            student.setContact("pavietmaxi@cy-tech.fr");
            student.setSchoolYear("2026");
            student.setLogin("mpaviet");
            student.setPassword("ABCDEFGHIJ");
            student.setCourseList("[1,2]");

            Teacher teacher = new Teacher();
            teacher.setName("Sonia");
            teacher.setSurname("Yassa");
            teacher.setContact("syassa@cy-tech.fr");
            teacher.setLogin("syassa");
            teacher.setPassword("ABCDEFGHIJ");
            teacher.setCourseList("[1,2]");

            session.persist(administrator);
            session.persist(assessment);
            session.persist(course);
            session.persist(grade);
            session.persist(student);
            session.persist(teacher);

            transaction.commit();

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}



