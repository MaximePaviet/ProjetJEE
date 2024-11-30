package controllers.Administrator;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Student;
import services.EmailSenderService;
import services.StudentService;
import java.io.IOException;

@WebServlet("/DeleteStudentAdminServlet")
public class DeleteStudentAdminServlet extends HttpServlet {

    private StudentService studentService;
    private EmailSenderService emailSenderService;

    @Override
    public void init() throws ServletException {
        // Initialization of services
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (studentService != null) {
            studentService.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the student
        String idStudentParam = request.getParameter("idStudent");
        Integer idStudent = Integer.parseInt(idStudentParam);
        Student student = studentService.readStudent(idStudent);

        //Send email to the student
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

        //Delete the student
        studentService.deleteStudent(idStudent);

        //Redirection to student page after deletion
        response.sendRedirect(request.getContextPath() + "/StudentPageAdminServlet");
    }
}

