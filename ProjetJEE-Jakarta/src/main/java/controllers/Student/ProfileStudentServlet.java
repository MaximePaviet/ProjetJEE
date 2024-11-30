package controllers.Student;

import models.Course;
import models.Student;
import services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ProfileStudentServlet")
public class ProfileStudentServlet extends HttpServlet {

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
        // Retrieve from session student
        Student student = (Student) request.getSession().getAttribute("student");

        // Calculation of averages per course
        Map<Integer, String> courseAverages = new HashMap<>();
        if (student != null && student.getCourseList() != null) {
            for (Course course : student.getCourseList()) {
                double courseAverage = courseService.calculateStudentAverageInCourse(course.getIdCourse(),student.getIdStudent());
                if (courseAverage >= 0) {
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Add the data
        request.setAttribute("courses", student.getCourseList());
        request.setAttribute("courseAverages", courseAverages);

        // Redirection to the student profile page
        request.getRequestDispatcher("/view/Student/ProfileStudent.jsp").forward(request, response);
    }
}
