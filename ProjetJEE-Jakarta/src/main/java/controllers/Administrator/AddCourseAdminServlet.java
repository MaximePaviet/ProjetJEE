package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CourseService;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

@WebServlet("/AddCourseAdminServlet")
public class AddCourseAdminServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouveau cours via un formulaire

        // Récupération des paramètres du formulaire
        String name = request.getParameter("courseName");

        // Création du cours via le service
        courseService.createCourse(name);

        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }
}
