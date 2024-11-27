package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Teacher;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.TeacherService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TeacherAdminServlet", urlPatterns = {"/TeacherPageServlet", "/addTeacher"})
public class TeacherAdminServlet extends HttpServlet {

    private TeacherService teacherService;
    private SessionFactory sessionFactory;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
        teacherService = new TeacherService();

        // Initialisation de la SessionFactory Hibernate pour les opérations liées à la base de données
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (teacherService != null) {
            teacherService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des enseignants
        List<Teacher> teachers = teacherService.readTeacherList();

        // Ajout des enseignants en tant qu'attribut de la requête
        request.setAttribute("teachers", teachers);

        // Transfert vers la page JSP correspondante
        request.getRequestDispatcher("/view/Administrator/TeacherPageAdmin.jsp").forward(request, response);
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
        response.sendRedirect(request.getContextPath() + "/TeacherPageServlet");
    }
}
