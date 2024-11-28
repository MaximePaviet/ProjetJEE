package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Teacher;
import services.TeacherService;

import java.io.IOException;
import java.util.List;

@WebServlet("/TeacherProfileAdminServlet")
public class TeacherProfileAdminServlet extends HttpServlet {

    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        teacherService.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idTeacherParam = request.getParameter("idTeacher");

        if (idTeacherParam != null && !idTeacherParam.isEmpty()) {
            try {
                int idTeacher = Integer.parseInt(idTeacherParam);

                // Récupération de l'enseignant
                Teacher teacher = teacherService.readTeacher(idTeacher);
                if (teacher != null) {

                List<Course> list = teacher.getCourseList();
                    // Ajouter les données en tant qu'attributs de requête
                    request.setAttribute("teacher", teacher);
                    request.setAttribute("courses", list);

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
