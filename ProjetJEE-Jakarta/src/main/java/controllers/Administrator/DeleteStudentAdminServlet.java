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
import services.EmailSenderService;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/DeleteStudentAdminServlet")
public class DeleteStudentAdminServlet extends HttpServlet {

    private StudentService studentService;
    private EmailSenderService emailSenderService;
    @Override
    public void init() throws ServletException {
        // Initialisation des services
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (studentService != null) {
            studentService.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'affichage de la liste des enseignants
        String idStudentParam = request.getParameter("idStudent");
        Integer idStudent = Integer.parseInt(idStudentParam);
        Student student = studentService.readStudent(idStudent);
        String htmlContent = "<html>" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                "<h2>Bonjour " + student.getName() + " " + student.getSurname() + ",</h2>" +
                "<p>Bonjour,\n\n</p>" +
                "Votre compte CyScolarité à été supprimé. \n</p>" +
                "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p style=\"color: #999; font-size: 0.9em;\">--<br>L'équipe CyScolarité</p>" +
                "</body>" +
                "</html>";

        emailSenderService.sendEmail(student.getContact(), student.getName() + " " + student.getSurname() + "- Suppression de votre compte !", htmlContent);

        studentService.deleteStudent(idStudent);

        response.sendRedirect(request.getContextPath() + "/StudentPageAdminServlet");
    }
}

