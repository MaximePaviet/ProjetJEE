package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Assessment;
import models.Course;
import models.Student;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import services.AssessmentService;
import services.CourseService;
import services.HibernateUtil;
import services.StudentService;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/AddAssessmentTeacherServlet")
public class AddAssessmentTeacherServlet extends HttpServlet {

    private AssessmentService assessmentService;
    private CourseService courseService;
    private StudentService studentService;

    public void init() throws ServletException {
        // Initialisation des services
        assessmentService = new AssessmentService();
        courseService = new CourseService();
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
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

    public Integer getAssessmentIdByNameAndCourse(String assessmentName, int courseId) {
        Integer assessmentId = null;

        String hql = "SELECT a.idAssessment FROM Assessment a WHERE a.name = :name AND a.course.idCourse = :courseId";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("name", assessmentName);
            query.setParameter("courseId", courseId);

            // Récupérer l'ID si un résultat est trouvé
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
        String idCourseParam = request.getParameter("idCourse");
        int idCourse = Integer.parseInt(idCourseParam);

        Course course = courseService.readCourse(idCourse);
        List<Student> students = course.getStudentList();

        request.setAttribute("course", course);
        request.setAttribute("students", students);


        request.getRequestDispatcher("/view/Teacher/AddAssessmentTeacher.jsp").forward(request, response);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nameAssessment = request.getParameter("nameAssessment");
        String idCourseString = request.getParameter("courseId");

        int idCourse = Integer.parseInt(idCourseString);
        Course course = courseService.readCourse(idCourse);

        // Vérifier si un Assessment existe déjà avec le même nom dans ce cours
        boolean exists = assessmentService.checkAssessmentExists(course, nameAssessment);

        if (exists) {
            // Si l'Assessment existe déjà, renvoyer un message d'erreur
            request.setAttribute("errorMessage", "Une évaluation avec ce nom existe déjà pour ce cours.");
            request.setAttribute("course", course);
            request.getRequestDispatcher("/view/Teacher/AddAssessmentTeacher.jsp").forward(request, response); // Redirige vers la page d'ajout
        } else {
            // Créer l'Assessment
            assessmentService.createAssessment(course, nameAssessment); // Méthode pour créer l'évaluation

            // Récupérer toutes les notes envoyées dans la requête
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();

                // Identifier les champs correspondant aux notes des étudiants
                if (paramName.startsWith("grade_")) {
                    String studentIdString = paramName.substring(6); // Extraire l'ID de l'étudiant
                    String gradeString = request.getParameter(paramName);

                    try {
                        int studentId = Integer.parseInt(studentIdString);
                        float gradeValue = Float.parseFloat(gradeString);

                        // Récupérer l'ID de l'Assessment créé
                        int assessmentId = getAssessmentIdByNameAndCourse(nameAssessment, idCourse);

                        // Récupérer l'objet Student et créer la note
                        Student student = studentService.readStudent(studentId); // Méthode pour récupérer l'étudiant par son ID
                        assessmentService.createGrade(student, assessmentId, gradeValue);


                    } catch (NumberFormatException e) {
                        System.out.println("Erreur de conversion pour " + paramName + ": " + e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/CoursePageTeacherServlet?idCourse=" + idCourse);
    }

}