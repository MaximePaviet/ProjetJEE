package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/AddAssessmentTeacherServlet")
public class AddAssessmentTeacherServlet extends HttpServlet {

    private AssessmentService assessmentService;
    private CourseService courseService;
    private StudentService studentService;
    private EmailSenderService emailSenderService;

    public void init() throws ServletException {
        // Initialization of services
        assessmentService = new AssessmentService();
        courseService = new CourseService();
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (assessmentService != null) {
            assessmentService.close();
        }
        if(courseService != null) {
            courseService.close();
        }
        if(studentService != null) {
            studentService.close();
        }
    }

    //Retrieve the evaluation id from its name and the course id
    public Integer getAssessmentIdByNameAndCourse(String assessmentName, int courseId) {
        Integer assessmentId = null;

        String hql = "SELECT a.idAssessment FROM Assessment a WHERE a.name = :name AND a.course.idCourse = :courseId";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("name", assessmentName);
            query.setParameter("courseId", courseId);

            assessmentId = query.uniqueResult();
        } catch (NoResultException e) {
            System.out.println("Aucun professeur trouvé avec ces identifiants.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assessmentId;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Retrieve the course id
        String idCourseParam = request.getParameter("idCourse");
        int idCourse = Integer.parseInt(idCourseParam);

        //Course recovery
        Course course = courseService.readCourse(idCourse);

        //Recovery of students taking the course
        List<Student> students = course.getStudentList();

        //Add the data
        request.setAttribute("course", course);
        request.setAttribute("students", students);

        //Redirect to the teacher assessment creation page
        request.getRequestDispatcher("/view/Teacher/AddAssessmentTeacher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the assessment name and course id
        String nameAssessment = request.getParameter("nameAssessment");
        String idCourseString = request.getParameter("courseId");

        //Get the course
        int idCourse = Integer.parseInt(idCourseString);
        Course course = courseService.readCourse(idCourse);

        // Check if an Assessment already exists with the same name in this course
        boolean exists = assessmentService.checkAssessmentExists(course, nameAssessment);
        if (exists) {
            request.setAttribute("errorMessage", "Une évaluation avec ce nom existe déjà pour ce cours.");
            request.setAttribute("course", course);
            request.getRequestDispatcher("/view/Teacher/AddAssessmentTeacher.jsp").forward(request, response);
        } else {
            // Creation of the evaluation
            assessmentService.createAssessment(course, nameAssessment);

            //Added grades for each student
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();

                if (paramName.startsWith("grade_")) {
                    String studentIdString = paramName.substring(6);
                    String gradeString = request.getParameter(paramName);

                    try {
                        int studentId = Integer.parseInt(studentIdString);
                        float gradeValue = Float.parseFloat(gradeString);

                        // Get the ID of the evaluation created
                        int assessmentId = getAssessmentIdByNameAndCourse(nameAssessment, idCourse);

                        // Retrieve the student and create their grade
                        Student student = studentService.readStudent(studentId);
                        assessmentService.createGrade(student, assessmentId, gradeValue);

                        // Send email to the student
                        String toEmail = student.getContact();
                        String subject = "Nouvelle note pour " + course.getName();
                        String body = "<html>" +
                                "<body>" +
                                "<h2>Bonjour " + student.getName() + " " + student.getSurname() + ",</h2>" +
                                "<p>Vous avez reçu une nouvelle note pour l'évaluation \"" + nameAssessment + "\" dans le cours \"" + course.getName() + "\":</p>" +
                                "<p><strong>Note : " + gradeValue + "</strong></p>" +
                                "<p>Connectez-vous à votre espace étudiant pour plus d'informations.</p>" +
                                "<p>Cordialement,</p>" +
                                "<p><strong>L'équipe CyScolarité</strong></p>" +
                                "</body>" +
                                "</html>";

                        emailSenderService.sendEmail(toEmail, subject, body);

                    } catch (NumberFormatException e) {
                        System.out.println("Erreur de conversion pour " + paramName + ": " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //Redirect to the course page
        response.sendRedirect(request.getContextPath() + "/CoursePageTeacherServlet?idCourse=" + idCourse);
    }

}
