package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Assessment;
import models.Course;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/CoursePageTeacherServlet")
public class CoursePageTeacherServlet extends HttpServlet {

    private CourseService courseService;
    private AssessmentService assessmentService;


    @Override
    public void init() throws ServletException {
        // Initialization of services
        courseService = new CourseService();
        assessmentService = new AssessmentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        courseService.close();
        assessmentService.close();
    }

    //Retrieve the list of evaluations for a course from its id
    private List<Assessment> getAssessmentFromIdCourse(int idCourse) {
        List<Assessment> assessments = new ArrayList<>();

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
        //Get the course id
        String idCourseParam = request.getParameter("idCourse");

        if (idCourseParam != null && !idCourseParam.isEmpty()) {
            try {
                //Course recovery
                int idCourse = Integer.parseInt(idCourseParam);
                Course course = courseService.readCourse(idCourse);

                //Retrieve the evaluations of a course
                List<Assessment> assessments = getAssessmentFromIdCourse(idCourse);

                // Calculation of worst and best marks
                Map<Integer, Map<String, Float>> minMaxGrades = assessmentService.calculateMinMaxGrades(assessments);

                //Add the data
                request.setAttribute("assessments", assessments);
                request.setAttribute("course", course);
                request.setAttribute("minMaxGrades", minMaxGrades);

                //Redirection to a teacher's course page
                request.getRequestDispatcher("/view/Teacher/CoursePageTeacher.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours manquant");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the course id
        String idCourseParam = request.getParameter("idCourse");

        if (idCourseParam != null && !idCourseParam.isEmpty()) {
            try {
                //Course recovery
                int idCourse = Integer.parseInt(idCourseParam);
                Course course = courseService.readCourse(idCourse);

                //Retrieve the evaluations of a course
                List<Assessment> assessments = getAssessmentFromIdCourse(Integer.parseInt(idCourseParam));

                // Calculation of worst and best marks
                Map<Integer, Map<String, Float>> minMaxGrades = assessmentService.calculateMinMaxGrades(assessments);

                //Add the data
                request.setAttribute("assessments", assessments);
                request.setAttribute("course", course);
                request.setAttribute("minMaxGrades", minMaxGrades);

                //Redirection to a teacher's course page
                request.getRequestDispatcher("/view/Teacher/CoursePageTeacher.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours manquant");
        }
    }
}

