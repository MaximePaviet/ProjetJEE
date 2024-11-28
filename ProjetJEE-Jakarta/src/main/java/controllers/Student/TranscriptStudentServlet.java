package controllers.Student;

import jakarta.persistence.NoResultException;
import models.Course;
import models.Student;
import models.Assessment;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.CourseService;
import services.AssessmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/TranscriptStudentServlet")
public class TranscriptStudentServlet extends HttpServlet {

    private CourseService courseService;
    private AssessmentService assessmentService;

    @Override
    public void init() {
        courseService = new CourseService();
        assessmentService = new AssessmentService();
    }

    @Override
    public void destroy() {
        courseService.close();
        assessmentService.close();
    }

    private List<Assessment> getAssessmentFromIdCourse(int idCourse) {
        List<Assessment> assessments = new ArrayList<>();

        // HQL pour récupérer l'ID en fonction du login et du mot de passe
        String hql = "FROM Assessment a WHERE a.course.idCourse = :idCourse";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Assessment> query = session.createQuery(hql, Assessment.class);
            query.setParameter("idCourse", idCourse);
            assessments = query.list();
        } catch (NoResultException e) {
            System.out.println("Aucune évaluation dans cette matière");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assessments;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'étudiant connecté
        Student student = (Student) request.getSession().getAttribute("student");

        // Récupérer les cours suivis par l'étudiant
        List<Course> courses = student.getCourseList();

        // Préparer les données pour la JSP
        Map<Course, Double> coursesWithAverages = new HashMap<>();
        Map<Integer, Map<Assessment, Double>> assessmentsWithGradesByCourse = new HashMap<>();

        for (Course course : courses) {
            // Calculer la moyenne dans chaque cours
            double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), student.getIdStudent());
            coursesWithAverages.put(course, average);

            // Récupérer les évaluations et leurs grades pour ce cours et cet étudiant
            Map<Assessment, Double> assessmentsWithGrades = assessmentService.getAssessmentsAndGradesByCourseAndStudent(course.getIdCourse(), student.getIdStudent());
            assessmentsWithGradesByCourse.put(course.getIdCourse(), assessmentsWithGrades);
        }

        // Attacher les données à la requête
        request.setAttribute("coursesWithAverages", coursesWithAverages);
        request.setAttribute("assessmentsWithGradesByCourse", assessmentsWithGradesByCourse);

        // Transférer vers la JSP
        request.getRequestDispatcher("/view/Student/TranscriptStudent.jsp").forward(request, response);
    }
}
