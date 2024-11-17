package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoginValidationServlet", value = "/validateLogin")
public class LoginValidationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des paramètres de login, password et rôle
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String role = request.getParameter("role"); // Récupération du rôle transmis via <input type="hidden">

        // Identifiants valides pour test
        final String validLogin = "admin";
        final String validPassword = "mdp1";

        // Validation des identifiants et du rôle
        if (validLogin.equals(login) && validPassword.equals(password) && "admin".equals(role)) {
            System.out.println("Connexion réussie pour l'utilisateur : " + login + " avec le rôle : " + role);
            request.getRequestDispatcher("/view/Administrateur/AccueilAdministrateur.jsp").forward(request, response);
        } else {
            System.out.println("Échec de connexion pour l'utilisateur : " + login + " avec le rôle : " + role);
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Administrateur/ConnexionAdministrateur.jsp").forward(request, response);
        }
    }
}
