package controllers.Administrator;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Teacher;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.TeacherService;
import java.io.IOException;
import java.util.List;

@WebServlet("/TeacherPageAdminServlet")
public class TeacherPageAdminServlet extends HttpServlet {

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
}


