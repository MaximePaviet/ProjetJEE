package controllers.Administrator;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import models.Teacher;
import services.CourseService;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/DeleteCourseAdminServlet")
public class DeleteCourseAdminServlet extends HttpServlet {

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
        // Gestion de l'affichage de la liste des enseignants
        String idCourseParam = request.getParameter("idCourse");

        Integer idCourse = Integer.parseInt(idCourseParam);

        courseService.deleteCourse(idCourse);

        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }
}

