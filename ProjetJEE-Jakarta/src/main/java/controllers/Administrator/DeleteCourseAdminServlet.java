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

@WebServlet("/DeleteCourseAdminServlet")
public class DeleteCourseAdminServlet extends HttpServlet {

    private CourseService courseService;
    private EmailSenderService emailSenderService;

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
        String idCourseParam = request.getParameter("idCourse");

        Integer idCourse = Integer.parseInt(idCourseParam);

        // Récupérer le cours avant suppression pour obtenir les informations nécessaires
        Course course = courseService.readCourse(idCourse);
        if (course != null) {
            List<Student> enrolledStudents = course.getStudentList(); // Récupérer les étudiants inscrits

            // Supprimer le cours
            courseService.deleteCourse(idCourse);

            // Envoyer un email à chaque étudiant inscrit
            String subject = "Suppression du cours " + course.getName();
            String bodyTemplate = "<html>" +
                    "<body>" +
                    "<h2>Bonjour,</h2>" +
                    "<p>Nous vous informons que le cours <strong>%s</strong> a été supprimé.</p>" +
                    "<p>Veuillez contacter votre administration pour plus d'informations.</p>" +
                    "<p>Cordialement,</p>" +
                    "<p><strong>L'équipe CyScolarité</strong></p>" +
                    "</body>" +
                    "</html>";

            for (Student student : enrolledStudents) {
                String toEmail = student.getContact();
                String body = String.format(bodyTemplate, course.getName());
                emailSenderService.sendEmail(toEmail, subject, body);
            }
        }

        // Rediriger vers la page des cours après suppression
        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }
}