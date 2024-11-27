package controllers.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.CourseService;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "ProfileStudentServelet", value = "/studentProfile")
public class ProfileStudentServelet extends HttpServlet {

    private CourseService courseService;
    private SessionFactory sessionFactory;

    @Override
    public void init() {
        // Initialisation des services
        courseService = new CourseService();
        // Initialisation de la SessionFactory Hibernate
        sessionFactory = new Configuration().configure().buildSessionFactory();

    }

    @Override
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (courseService != null) {
            courseService.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération de la liste des cours
        List<Course> courses = courseService.readCourseList();

        // Ajout des enseignants en tant qu'attribut de la requête
        request.setAttribute("courses", courses);

        // Redirection vers la page d'inscription aux cours
        request.getRequestDispatcher("/view/Student/AssignmentCourseStudent.jsp").forward(request, response);
    }
}


