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
        studentService = new StudentService();
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (studentService != null) {
            studentService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("search");
        String promoParam = request.getParameter("promo");

        List<Student> students;

        // Si un terme de recherche est fourni
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            students = studentService.searchStudent(searchTerm);
        } else {
            // Sinon, rechercher tous les étudiants (ou appliquer le filtre)
            if (promoParam != null && !promoParam.trim().isEmpty()) {
                // Traiter les promotions sélectionnées
                String[] promoArray = promoParam.split(",");
                students = studentService.getStudentsByPromo(promoArray);
            } else {
                // Pas de filtre sur les promotions
                students = studentService.readStudentList();
            }
        }

        // Ajouter les étudiants comme attribut
        request.setAttribute("students", students);
        request.getRequestDispatcher("/view/Administrator/StudentPageAdmin.jsp").forward(request, response);
    }
}
