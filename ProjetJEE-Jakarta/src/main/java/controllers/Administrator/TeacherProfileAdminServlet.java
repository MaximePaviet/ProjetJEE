package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Teacher;
import services.CourseService;
import services.TeacherService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/TeacherProfileAdminServlet")
public class TeacherProfileAdminServlet extends HttpServlet {

    private TeacherService teacherService;
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        teacherService = new TeacherService();
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        // Release of resources
        teacherService.close();
        courseService.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the teacher id
        String idTeacherParam = request.getParameter("idTeacher");

        if (idTeacherParam != null && !idTeacherParam.isEmpty()) {
            try {
                int idTeacher = Integer.parseInt(idTeacherParam);

                // Teacher recovery
                Teacher teacher = teacherService.readTeacher(idTeacher);

                // Calculation of the average for each course
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

                    List<Course> list = teacher.getCourseList();
                    // Add the data
                    request.setAttribute("teacher", teacher);
                    request.setAttribute("courses", list);
                    request.setAttribute("courseAverages", courseAverages);

                    // Redirection to the teacher's profile page
                    request.getRequestDispatcher("/view/Administrator/TeacherProfileAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Enseignant introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID enseignant invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID enseignant manquant");
        }
    }
}
