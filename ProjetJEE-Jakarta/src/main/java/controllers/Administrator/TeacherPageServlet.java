package controllers.Administrator;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import models.Teacher;
import services.TeacherService;
import java.io.IOException;
import java.util.List;


public class TeacherPageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TeacherService teacherService = new TeacherService();
        List<Teacher> teachers = teacherService.readTeacherList();
        teacherService.close();

        // Ajouter les enseignants à la requête
        request.setAttribute("teachers", teachers);

        // Rediriger vers la page JSP
        request.getRequestDispatcher("/view/Administrator/PageEnseignants.jsp").forward(request, response);

    }
}


