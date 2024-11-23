package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import services.TeacherService;

import java.io.IOException;

@WebServlet(name = "AddTeacherServlet", value = "/addTeacher")
public class AddTeacherServlet extends HttpServlet {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        // Initialisation de la SessionFactory Hibernate
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des paramètres de login et password
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String contact = name + surname + "@cy-tech.fr";

        TeacherService service = new TeacherService();
        service.createTeacher(name,surname,contact);
    }
}