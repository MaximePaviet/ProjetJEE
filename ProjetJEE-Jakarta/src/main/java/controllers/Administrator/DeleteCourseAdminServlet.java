package controllers.Administrator;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import services.CourseService;
import services.EmailSenderService;
import java.io.IOException;
import java.util.List;

@WebServlet("/DeleteCourseAdminServlet")
public class DeleteCourseAdminServlet extends HttpServlet {

    private CourseService courseService;
    private EmailSenderService emailSenderService;

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
        //Get the course
        String idCourseParam = request.getParameter("idCourse");
        Integer idCourse = Integer.parseInt(idCourseParam);
        Course course = courseService.readCourse(idCourse);

        //Management of events linked to the deletion of a course
        if (course != null) {

            //Delete the course
            List<Student> enrolledStudents = course.getStudentList();
            courseService.deleteCourse(idCourse);

            // Send email to each registered student
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

        // Redirect to course page after deletion
        response.sendRedirect(request.getContextPath() + "/CoursePageAdminServlet");
    }
}