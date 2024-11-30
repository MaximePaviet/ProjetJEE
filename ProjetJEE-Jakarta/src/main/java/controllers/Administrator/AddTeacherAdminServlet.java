package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TeacherService;
import java.io.IOException;

@WebServlet("/AddTeacherAdminServlet")
public class AddTeacherAdminServlet extends HttpServlet {

    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (teacherService != null) {
            teacherService.close();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve form parameters
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String contact = name + surname + "@cy-tech.fr";

        // Teacher creation
        teacherService.createTeacher(name, surname, contact);

        // Redirection or transfer to the teachers page
        response.sendRedirect(request.getContextPath() + "/TeacherPageAdminServlet");
    }
}
