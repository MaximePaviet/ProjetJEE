package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Student;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.StudentService;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


@WebServlet(name = "StudentAdminServlet", urlPatterns = {"/StudentPageServlet"})
public class StudentAdminServlet extends HttpServlet {

    private StudentService studentService;
    private SessionFactory sessionFactory;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
        studentService = new StudentService();

        // Initialisation de la SessionFactory Hibernate pour les opérations liées à la base de données
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (studentService != null) {
            studentService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des enseignants
        List<Student> students = studentService.readStudentList();

        // Ajout des enseignants en tant qu'attribut de la requête
        request.setAttribute("students", students);

        // Transfert vers la page JSP correspondante
        request.getRequestDispatcher("/view/Administrator/StudentPageAdmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouvel enseignant via un formulaire

        // Récupération des paramètres du formulaire
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String contact = name + surname + "@cy-tech.fr";
        String dateBirthParam = request.getParameter("dateBirth"); // Format attendu : YYYY-MM-DD

        // Conversion en java.sql.Date
        Date dateBirth = null;
        try {
            if (dateBirthParam != null && !dateBirthParam.isEmpty()) {
                dateBirth = Date.valueOf(dateBirthParam); // Conversion directe depuis une chaîne YYYY-MM-DD
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("error", "Format de date invalide !");
            request.getRequestDispatcher("/formPage.jsp").forward(request, response);
            return;
        }
        Calendar calendar = Calendar.getInstance(); // Crée une instance du calendrier configurée sur la date actuelle
        String currentYear = String.valueOf(calendar.get(Calendar.YEAR)); // Récupère l'année actuelle

        // Création de l'enseignant via le service
        studentService.createStudent(name, surname,dateBirth, contact,currentYear);

        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/StudentPageServlet");
    }
}
