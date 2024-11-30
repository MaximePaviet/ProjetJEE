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
        teacherService = new TeacherService();
        courseService = new CourseService();
    }

    @Override
    public void destroy() {

        teacherService.close();
        courseService.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idTeacherParam = request.getParameter("idTeacher");

        if (idTeacherParam != null && !idTeacherParam.isEmpty()) {
            try {
                int idTeacher = Integer.parseInt(idTeacherParam);

                // Récupération de l'enseignant
                Teacher teacher = teacherService.readTeacher(idTeacher);

                // Structure pour stocker les moyennes ou les messages
                Map<Integer, String> courseAverages = new HashMap<>();

                if (teacher != null && teacher.getCourseList() != null) {
                    // Parcourir tous les cours de l'enseignant
                    for (Course course : teacher.getCourseList()) {
                        // Calculer la moyenne pour chaque cours
                        double courseAverage = courseService.calculateCourseAverage(course.getIdCourse());

                        if (courseAverage >= 0) {
                            // Stocker la moyenne avec 2 décimales
                            courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                        } else {
                            // Si pas de notes, afficher un message
                            courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                        }
                    }

                List<Course> list = teacher.getCourseList();
                    // Ajouter les données en tant qu'attributs de requête
                    request.setAttribute("teacher", teacher);
                    request.setAttribute("courses", list);
                    request.setAttribute("courseAverages", courseAverages);

                    // Rediriger vers la JSP
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
