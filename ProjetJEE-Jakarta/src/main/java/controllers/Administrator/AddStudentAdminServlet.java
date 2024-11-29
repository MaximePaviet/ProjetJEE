package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.EmailSenderService;
import services.StudentService;
import services.TeacherService;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

@WebServlet("/AddStudentAdminServlet")
public class AddStudentAdminServlet extends HttpServlet {

    private StudentService studentService;
    private EmailSenderService emailSenderService;

    @Override
    public void init() throws ServletException {
        // Initialisation des services
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Libération des ressources
        if (studentService != null) {
            studentService.close();
        }
        if (emailSenderService != null) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouvel enseignant via un formulaire

        // Récupération des paramètres du formulaire
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");

        // Calcul pour ajuster la longueur totale à 10 caractères
        int maxNameLength = 10 - surname.length();
        String adjustedName = name.length() > maxNameLength ? name.substring(0, maxNameLength) : name;

        // Génération de l'email
        String contact = surname + adjustedName + "@cy-tech.fr";
        String dateBirthParam = request.getParameter("dateBirth"); // Format attendu : YYYY-MM-DD

        // Conversion en java.sql.Date
        Date dateBirth = null;
        try {
            if (dateBirthParam != null && !dateBirthParam.isEmpty()) {
                dateBirth = Date.valueOf(dateBirthParam); // Conversion directe depuis une chaîne YYYY-MM-DD
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute("error", "Format de date invalide !");
            request.getRequestDispatcher("/formPage.jsp").forward(request, response);
            return;
        }
        Calendar calendar = Calendar.getInstance(); // Crée une instance du calendrier configurée sur la date actuelle
        String promoYear = String.valueOf(calendar.get(Calendar.YEAR) + 3); // Récupère l'année actuelle


        // Création de l'enseignant via le service
        Map<String, String> generatedInfo = studentService.createStudent(name, surname,dateBirth, contact,promoYear);
        // Récupération des informations générées
        String login = generatedInfo.get("login");
        String password = generatedInfo.get("password");

// Envoi de l'email avec les informations
        String htmlContent = "<html>" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                "<h2>Bonjour " + name + " " + surname + ",</h2>" +
                "<p>Nous avons le plaisir de vous communiquer vos codes d'accès à <strong>CyScolarité</strong> :</p>" +
                "<table style=\"border-collapse: collapse; width: 60%; margin: 20px 0;\">" +
                "<tr style=\"background-color: #f5f5f5;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Login :</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + login + "</td></tr>" +
                "<tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Password :</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + password + "</td></tr>" +
                "</table>" +
                "<p style=\"margin: 20px 0;\">Cliquez sur le bouton ci-dessous pour accéder à votre compte :</p>" +
                "<p><a href=\"http://localhost:8081/ProjetJEE_Jakarta_war_exploded/view/Student/ConnexionStudent.jsp\" style=\"color: #ffffff; background-color: #4F2BEC; padding: 10px 15px; text-decoration: none; border-radius: 5px;\">Cliquez ici pour continuer</a></p>" +
                "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p style=\"color: #999; font-size: 0.9em;\">--<br>L'équipe CyScolarité</p>" +
                "</body>" +
                "</html>";

        emailSenderService.sendEmail(contact, name + " " + surname + " - Vos codes d'accès !", htmlContent);



        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/StudentPageAdminServlet");
    }
}
