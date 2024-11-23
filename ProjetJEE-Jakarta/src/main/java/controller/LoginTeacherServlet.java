package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;

@WebServlet(name = "LoginTeacherServlet", value = "/teacherLogin")
public class LoginTeacherServlet extends HttpServlet {

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

    private boolean loginExist(String login, String password) {
        boolean isValid = false;

        // HQL pour vérifier les identifiants
        String hql = "FROM Teacher WHERE login = :login AND password = :password";

        try (Session session = sessionFactory.openSession()) {
            // Création de la requête
            Query<Teacher> query = session.createQuery(hql, Teacher.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            // Vérifiez si un résultat existe
            isValid = query.uniqueResult() != null; // Retourne true si un résultat est trouvé
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des paramètres de login et password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        // Validation des identifiants
        if (loginExist(login, password)) {
            request.getRequestDispatcher("/view/Teacher/ProfileTeacher.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Teacher/ConnexionTeacher.jsp").forward(request, response);
        }
    }
}
