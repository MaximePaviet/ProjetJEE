package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoginAdminServlet", value = "/validateLogin")
public class LoginAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des paramètres de login et password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        // Identifiants valides pour test
        final String validLogin = "admin";
        final String validPassword = "mdp1";

        // Validation des identifiants
        if (validLogin.equals(login) && validPassword.equals(password)) {
            System.out.println("Connexion réussie pour l'utilisateur : " + login);
            request.getRequestDispatcher("/view/Administrator/HomeAdministrator.jsp").forward(request, response);
        } else {
            System.out.println("Échec de connexion pour l'utilisateur : " + login);
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Administrator/ConnexionAdministrator.jsp").forward(request, response);
        }
    }
}
