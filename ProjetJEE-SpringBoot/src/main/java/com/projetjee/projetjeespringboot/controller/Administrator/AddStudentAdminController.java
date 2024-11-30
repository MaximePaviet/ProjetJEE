
package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.services.EmailSenderService;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

@Controller
public class AddStudentAdminController {

    private final StudentService studentService;
    private final EmailSenderService emailSenderService;

    // Injection du service StudentService via le constructeur
    @Autowired
    public AddStudentAdminController(StudentService studentService, EmailSenderService emailSenderService) {
        this.studentService = studentService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/AddStudentAdminController")
    public String addStudent(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("dateBirth") String dateBirthParam, // Paramètre de date au format YYYY-MM-DD
            RedirectAttributes redirectAttributes,
            Model model) {

        // Calcul pour ajuster la longueur totale à 10 caractères
        int maxNameLength = 10 - surname.length();
        String adjustedName = name.length() > maxNameLength ? name.substring(0, maxNameLength) : name;
        // Génération de l'email
        String contact = surname + adjustedName + "@cy-tech.fr";


        Date dateBirth = null;
        try {
            if (dateBirthParam != null && !dateBirthParam.isEmpty()) {
                dateBirth = Date.valueOf(dateBirthParam); // Conversion directe de la chaîne au format YYYY-MM-DD
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Format de date invalide !");
            return "Administrator/AddStudentAdmin"; // Retour à la page du formulaire en cas d'erreur
        }

        // Calcul de l'année de promotion
        Calendar calendar = Calendar.getInstance();
        String promoYear = String.valueOf(calendar.get(Calendar.YEAR) + 3); // Année actuelle + 3

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

        // Ajouter un message de succès
        redirectAttributes.addFlashAttribute("message", "Étudiant ajouté avec succès!");

        // Redirection vers la page des étudiants
        return "redirect:/StudentPageAdminController";
    }
}
