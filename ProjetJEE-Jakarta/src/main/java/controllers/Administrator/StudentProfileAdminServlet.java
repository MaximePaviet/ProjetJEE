package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import services.CourseService;
import services.StudentService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/StudentProfileAdminServlet")
public class StudentProfileAdminServlet extends HttpServlet {

    private StudentService studentService;
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        studentService = new StudentService();
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        // Release of resources
        studentService.close();
        courseService.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the student id
        String idStudentParam = request.getParameter("idStudent");

        if (idStudentParam != null && !idStudentParam.isEmpty()) {
            try {
                int idStudent = Integer.parseInt(idStudentParam);

                // Student recovery
                Student student = studentService.readStudent(idStudent);

                if (student != null) {

                    //Recovery of courses taken by the student
                    List<Course> courses = student.getCourseList();

                    // Calculation of averages per course
                    Map<Course, Double> coursesWithAverages = new HashMap<>();
                    for (Course course : courses) {
                        double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), idStudent);
                        coursesWithAverages.put(course, average);
                    }

                    // Add the data
                    request.setAttribute("student", student);
                    request.setAttribute("coursesWithAverages", coursesWithAverages);

                    // Redirect to the student profile page
                    request.getRequestDispatcher("/view/Administrator/StudentProfileAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Étudiant introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant manquant");
        }
    }
}
