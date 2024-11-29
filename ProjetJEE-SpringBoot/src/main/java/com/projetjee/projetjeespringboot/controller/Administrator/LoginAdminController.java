package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.services.AdministratorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginAdminController {

    private final AdministratorService administratorService;

    // Injection du service AdministratorService
    public LoginAdminController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/LoginAdminController")
    public String showLoginPage() {
        // Préparation du message d'erreur (vide par défaut)
        return "Administrator/ConnexionAdministrator"; // Nom de la page HTML Thymeleaf
    }

    @PostMapping("/LoginAdminController")
    public String handleLogin(
            @RequestParam String login,
            @RequestParam String password,
            Model model) {

        // Validation des identifiants via le service
        if (administratorService.loginExist(login, password)) {
            return "redirect:views/Administrator/HomeAdministrator"; // Redirection si succès
        } else {
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
            return "/views/Administrator/ConnexionAdministrator"; // Recharge la page avec un message d'erreur
        }
    }
}
