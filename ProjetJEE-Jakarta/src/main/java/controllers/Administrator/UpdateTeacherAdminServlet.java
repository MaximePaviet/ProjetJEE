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

@WebServlet("/UpdateTeacherAdminServlet")
public class UpdateTeacherAdminServlet extends HttpServlet {

    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (teacherService != null) {
            teacherService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Récupérer les paramètres envoyés par le formulaire
            String idTeacherParam = request.getParameter("idTeacher");


            if (idTeacherParam != null && !idTeacherParam.isEmpty()) {
                try {
                    int idTeacher = Integer.parseInt(idTeacherParam);

                    // Récupération de l'enseignant
                    Teacher teacher = teacherService.readTeacher(idTeacher);
                    if (teacher != null) {

                        request.setAttribute("teacher", teacher);

                        request.getRequestDispatcher("/view/Administrator/UpdateTeacherAdmin.jsp").forward(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des enseignants
        String idTeacherParam = request.getParameter("id");
        String surnameTeacher = request.getParameter("surname");
        String nameTeacher = request.getParameter("name");
        String contactTeacher = request.getParameter("contact");

        Integer idTeacher = Integer.parseInt(idTeacherParam);

        teacherService.updateTeacher(idTeacher, nameTeacher, surnameTeacher, contactTeacher);

        response.sendRedirect(request.getContextPath() + "/TeacherPageAdminServlet");
    }

}



