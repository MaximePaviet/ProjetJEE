package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Teacher;
import services.TeacherService;
import java.io.IOException;

@WebServlet("/UpdateTeacherAdminServlet")
public class UpdateTeacherAdminServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the teacher's id
            String idTeacherParam = request.getParameter("idTeacher");


            if (idTeacherParam != null && !idTeacherParam.isEmpty()) {
                try {
                    int idTeacher = Integer.parseInt(idTeacherParam);

                    // Teacher recovery
                    Teacher teacher = teacherService.readTeacher(idTeacher);
                    if (teacher != null) {

                        //Add the data
                        request.setAttribute("teacher", teacher);

                        //Redirects to the teacher edit page
                        request.getRequestDispatcher("/view/Administrator/UpdateTeacherAdmin.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Enseignant introuvable");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID enseignant invalide");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID enseignant manquant");
            }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String idTeacherParam = request.getParameter("id");
        String surnameTeacher = request.getParameter("surname");
        String nameTeacher = request.getParameter("name");
        String contactTeacher = request.getParameter("contact");

        Integer idTeacher = Integer.parseInt(idTeacherParam);

        //Teacher Update
        teacherService.updateTeacher(idTeacher, nameTeacher, surnameTeacher, contactTeacher);

        //Redirection to the teacher page
        response.sendRedirect(request.getContextPath() + "/TeacherPageAdminServlet");
    }

}



