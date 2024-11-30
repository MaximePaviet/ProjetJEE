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
import java.util.List;


@WebServlet("/StudentPageAdminServlet")
public class StudentPageAdminServlet extends HttpServlet {

    private StudentService studentService;
    private SessionFactory sessionFactory;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        studentService = new StudentService();
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (studentService != null) {
            studentService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the contents of the search bar
        String searchTerm = request.getParameter("search");

        //Get the selected filters
        String promoParam = request.getParameter("promo");

        List<Student> students;

        //Performs search based on filters
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            students = studentService.searchStudent(searchTerm);
        } else {
            if (promoParam != null && !promoParam.trim().isEmpty()) {
                String[] promoArray = promoParam.split(",");
                students = studentService.getStudentsByPromo(promoArray);
            } else {
                students = studentService.readStudentList();
            }
        }

        // Show students
        request.setAttribute("students", students);

        //Redirection to the student page
        request.getRequestDispatcher("/view/Administrator/StudentPageAdmin.jsp").forward(request, response);
    }
}
