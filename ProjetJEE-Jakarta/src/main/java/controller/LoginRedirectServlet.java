package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");

        // Redirection vers la page de connexion spécifique en fonction du rôle
        if ("teacher".equals(role)) {
            request.getRequestDispatcher("loginTeacher.jsp").forward(request, response);
        } else if ("student".equals(role)) {
            request.getRequestDispatcher("loginStudent.jsp").forward(request, response);
        } else if ("admin".equals(role)) {
            request.getRequestDispatcher("view/Administrateur/ConnexionAdministrateur.jsp").forward(request, response);
        } else {
            response.sendRedirect("index.jsp"); // Retour à la page d'accueil en cas de rôle invalide
        }
    }
}
