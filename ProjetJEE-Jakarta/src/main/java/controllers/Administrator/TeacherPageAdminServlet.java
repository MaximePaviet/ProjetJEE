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
        // Initialization of services
        teacherService = new TeacherService();

        // Session initialization
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (teacherService != null) {
            teacherService.close();
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recovering the contents of the search bar
        String searchTerm = request.getParameter("search");

        //Search for teachers
        List<Teacher> teachers;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            teachers = teacherService.searchTeacher(searchTerm);
        } else {
            teachers = teacherService.readTeacherList();
        }

        // Add teachers
        request.setAttribute("teachers", teachers);

        // Redirection to the teacher page
        request.getRequestDispatcher("/view/Administrator/TeacherPageAdmin.jsp").forward(request, response);
    }
}


