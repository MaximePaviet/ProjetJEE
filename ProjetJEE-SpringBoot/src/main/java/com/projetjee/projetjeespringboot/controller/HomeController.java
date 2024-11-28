package com.projetjee.projetjeespringboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    // Méthode pour afficher la page d'accueil
    @GetMapping ("/")
    public String showHomePage() {
        // Retourne le nom de la vue (le préfixe et suffixe sont ajoutés automatiquement par la configuration)
        return "index";  // Correspondra à /WEB-INF/jsp/index.jsp
    }
    @GetMapping("/views/Administrator/ConnexionAdministrator")
    public String showAdminPage() {
        return "Administrator/ConnexionAdministrator"; // Correspond au fichier JSP admin.jsp
    }

    @GetMapping("/views/Teacher/ConnexionTeacher")
    public String showTeacherPage() {
        return "Teacher/ConnexionTeacher"; // Correspond au fichier JSP admin.jsp
    }

    @GetMapping("/views/Student/ConnexionStudent")
    public String showStudentPage() {
        return "Student/ConnexionStudent"; // Correspond au fichier JSP admin.jsp
    }
}