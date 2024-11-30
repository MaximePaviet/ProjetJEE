package controllers.Student;

import models.Course;
import models.Student;
import models.Teacher;
import org.hibernate.Hibernate;
import services.*;
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

@WebServlet("/ProfileStudentServlet")
public class ProfileStudentServlet extends HttpServlet {

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
        Student student = (Student) request.getSession().getAttribute("student");

        // Structure pour stocker les moyennes ou les messages
        Map<Integer, String> courseAverages = new HashMap<>();

        if (student != null && student.getCourseList() != null) {
            // Parcourir tous les cours de l'élève
            for (Course course : student.getCourseList()) {
                // Calculer la moyenne pour chaque cours
                double courseAverage = courseService.calculateStudentAverageInCourse(course.getIdCourse(),student.getIdStudent());

                if (courseAverage >= 0) {
                    // Stocker la moyenne avec 2 décimales
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    // Si pas de notes, afficher un message
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Mettre à jour les attributs de la requête
        request.setAttribute("courses", student.getCourseList());
        request.setAttribute("courseAverages", courseAverages);

        // Rediriger vers la page JSP
        request.getRequestDispatcher("/view/Student/ProfileStudent.jsp").forward(request, response);
    }


}
