package controllers.Administrator;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import models.Teacher;
import services.CourseService;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/UpdateCourseAdminServlet")
public class UpdateCourseAdminServlet extends HttpServlet {

    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (courseService != null) {
            courseService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les paramètres envoyés par le formulaire
        String idCourseParam = request.getParameter("idCourse");


        if (idCourseParam != null && !idCourseParam.isEmpty()) {
            try {
                int idCourse = Integer.parseInt(idCourseParam);

                // Récupération de l'enseignant
                Course course = courseService.readCourse(idCourse);
                if (course != null) {

                    request.setAttribute("course", course);

                    request.getRequestDispatcher("/view/Administrator/UpdateCourseAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cours introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours manquant");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des enseignants
        String idCourseParam = request.getParameter("id");
        String nameCourse = request.getParameter("name");

        Integer idCourse = Integer.parseInt(idCourseParam);

        courseService.updateCourse(idCourse, nameCourse);

        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }

}

