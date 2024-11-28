package controllers.Teacher;

import models.Course;
import models.Teacher;
import org.hibernate.Hibernate;
import services.AssessmentService;
import services.CourseService;
import services.HibernateUtil;
import services.TeacherService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/ProfileTeacherServlet")
public class ProfileTeacherServlet extends HttpServlet {

    private CourseService courseService;

    @Override
    public void init() {
        // Initialisation des services
        courseService = new CourseService();
    }

    @Override
    public void destroy() {
        if (courseService != null) {
            courseService.close();
        }
        // Fermeture de la SessionFactory via HibernateUtil
        HibernateUtil.shutdown();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer l'objet Teacher depuis la session
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        // Structure pour stocker les moyennes ou les messages
        Map<Integer, String> courseAverages = new HashMap<>();

        if (teacher != null && teacher.getCourseList() != null) {
            // Parcourir tous les cours de l'enseignant
            for (Course course : teacher.getCourseList()) {
                // Calculer la moyenne pour chaque cours
                double courseAverage = courseService.calculateCourseAverage(course.getIdCourse());

                if (courseAverage > 0) {
                    // Stocker la moyenne avec 2 décimales
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    // Si pas de notes, afficher un message
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Mettre à jour les attributs de la requête
        request.setAttribute("courses", teacher.getCourseList());
        request.setAttribute("courseAverages", courseAverages);

        // Rediriger vers la page JSP
        request.getRequestDispatcher("/view/Teacher/ProfileTeacher.jsp").forward(request, response);
    }


}
