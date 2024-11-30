package controllers.Teacher;

import models.Course;
import models.Teacher;
import org.hibernate.Hibernate;
import services.AssessmentService;
import services.CourseService;
import services.HibernateUtil;
import services.TeacherService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ProfileTeacherServlet")
public class ProfileTeacherServlet extends HttpServlet {

    private CourseService courseService;

    @Override
    public void init() {
        // Initialization of services
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (courseService != null) {
            courseService.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the session teacher
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        //Calculation of averages for each course
        Map<Integer, String> courseAverages = new HashMap<>();
        if (teacher != null && teacher.getCourseList() != null) {
            for (Course course : teacher.getCourseList()) {
                double courseAverage = courseService.calculateCourseAverage(course.getIdCourse());
                if (courseAverage >= 0) {
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Add the data
        request.setAttribute("courses", teacher.getCourseList());
        request.setAttribute("courseAverages", courseAverages);

        // Redirection to the teacher profile page
        request.getRequestDispatcher("/view/Teacher/ProfileTeacher.jsp").forward(request, response);
    }
}
