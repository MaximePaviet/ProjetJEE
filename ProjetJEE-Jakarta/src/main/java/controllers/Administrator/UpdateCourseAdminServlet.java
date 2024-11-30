package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import services.CourseService;
import java.io.IOException;

@WebServlet("/UpdateCourseAdminServlet")
public class UpdateCourseAdminServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the course id
        String idCourseParam = request.getParameter("idCourse");

        if (idCourseParam != null && !idCourseParam.isEmpty()) {
            try {
                int idCourse = Integer.parseInt(idCourseParam);

                // Course recovery
                Course course = courseService.readCourse(idCourse);
                if (course != null) {

                    //Add the data
                    request.setAttribute("course", course);

                    //Redirect to the course modification page
                    request.getRequestDispatcher("/view/Administrator/UpdateCourseAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cours introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID cours manquant");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the course name and id
        String idCourseParam = request.getParameter("id");
        String nameCourse = request.getParameter("name");

        Integer idCourse = Integer.parseInt(idCourseParam);

        //Course update
        courseService.updateCourse(idCourse, nameCourse);

        //Redirection to the course page
        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }

}

