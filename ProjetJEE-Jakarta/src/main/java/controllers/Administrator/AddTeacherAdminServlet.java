package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.TeacherService;

import java.io.IOException;

@WebServlet("/AddTeacherAdminServlet")
public class AddTeacherAdminServlet extends HttpServlet {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouvel enseignant via un formulaire

        // Récupération des paramètres du formulaire
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String contact = name + surname + "@cy-tech.fr";

        // Création de l'enseignant via le service
        teacherService.createTeacher(name, surname, contact);

        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/TeacherPageAdminServlet");
    }
}
