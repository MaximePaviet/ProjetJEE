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


@WebServlet("/StudentPageAdminServlet")
public class StudentPageAdminServlet extends HttpServlet {

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
}
