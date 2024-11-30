package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CourseService;
import java.io.IOException;

@WebServlet("/AddCourseAdminServlet")
public class AddCourseAdminServlet extends HttpServlet {
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (courseService != null) {
            courseService.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String name = request.getParameter("courseName");

        // Course creation
        courseService.createCourse(name);

        // Redirection to the courses page
        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }
}
