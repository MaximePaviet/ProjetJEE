package controller.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CourseAdminServlet", urlPatterns = {"/CoursePageServlet", "/addCourse"})
public class CourseAdminServlet extends HttpServlet {

    private CourseService courseService;
    private SessionFactory sessionFactory;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
         courseService = new CourseService();

        // Initialisation de la SessionFactory Hibernate pour les opérations liées à la base de données
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (courseService != null) {
            courseService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des cours
        List<Course> courses = courseService.readCourseList();

        // Ajout des cours en tant qu'attribut de la requête
        request.setAttribute("courses", courses);

        // Transfert vers la page JSP correspondante
        request.getRequestDispatcher("/view/Administrator/CoursePageAdmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouveau cours via un formulaire

        // Récupération des paramètres du formulaire
        String name = request.getParameter("courseName");

        // Création du cours via le service
        courseService.createCourse(name);

        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/CoursePageServlet");
    }
}
