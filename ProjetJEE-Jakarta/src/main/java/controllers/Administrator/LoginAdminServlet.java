package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Administrator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.io.IOException;

@WebServlet("/LoginAdminServlet")
public class LoginAdminServlet extends HttpServlet {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        // Session initialization
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public void destroy() {
        //Closing the session
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    //Method to check login and password
    private boolean accountExist(String login, String password) {
        boolean isValid = false;

        String hql = "FROM Administrator WHERE login = :login AND password = :password";

        try (Session session = sessionFactory.openSession()) {
            Query<Administrator> query = session.createQuery(hql, Administrator.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            isValid = query.uniqueResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recovery of login and password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        // Redirect
        if (accountExist(login, password)) {
            //To the home page
            request.getRequestDispatcher("/view/Administrator/HomeAdministrator.jsp").forward(request, response);
        } else {
            //In case of error
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Administrator/ConnexionAdministrator.jsp").forward(request, response);
        }
    }
}
