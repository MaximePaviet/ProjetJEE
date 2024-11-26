package controller.Administrator;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Teacher;
import services.TeacherService;

import java.io.IOException;

@WebServlet("/UpdateTeacherServlet")
public class UpdateTeacherServlet extends HttpServlet {

    private TeacherService teacherService = new TeacherService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupérer les paramètres envoyés par le formulaire
            String idParam = request.getParameter("id");
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String contact = request.getParameter("contact");

            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("ID de l'enseignant non fourni.");
            }

            Integer idTeacher = Integer.parseInt(idParam);

            // Logique de mise à jour via le service
            TeacherService teacherService = new TeacherService();
            Teacher teacher = teacherService.readTeacher(idTeacher);
            teacherService.updateTeacher(idTeacher, name, surname, contact);
            request.setAttribute("teacher", teacher);

            // Redirection après mise à jour
            response.sendRedirect(request.getContextPath() + "/TeacherPageServlet");

        } catch (Exception e) {
            // Gestion des erreurs
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la mise à jour de l'enseignant : " + e.getMessage());
            request.getRequestDispatcher("/view/Administrator/UpdateTeacherAdmin.jsp").forward(request, response);
        }
    }

}



