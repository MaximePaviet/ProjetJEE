package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Assessment;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.CourseService;
import services.HibernateUtil;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CoursePageTeacherServlet")
public class CoursePageTeacherServlet extends HttpServlet {

    private CourseService courseService;


    @Override
    public void init() throws ServletException {
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        courseService.close();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idCourseParam = request.getParameter("idCourse");

        if (idCourseParam != null && !idCourseParam.isEmpty()) {
            try {
                int idCourse = Integer.parseInt(idCourseParam);
                Course course = courseService.readCourse(idCourse);

                List<Assessment> assessments = getAssessmentFromIdCourse(Integer.parseInt(idCourseParam));

                request.setAttribute("assessments", assessments);
                request.setAttribute("course", course);

                request.getRequestDispatcher("/view/Teacher/CoursePageTeacher.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours manquant");
        }
    }
}

