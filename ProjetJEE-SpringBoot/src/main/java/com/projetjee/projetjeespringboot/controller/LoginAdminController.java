package com.projetjee.projetjeespringboot.controller;

import com.projetjee.projetjeespringboot.services.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class LoginAdminController {

    @Autowired
    private AdministratorService administratorService;

   /*@GetMapping("/adminLogin")
    public String showLoginPage() {
        return "Administrator/ConnexionAdministrator";
    }*/

    @PostMapping("/adminLogin")
    public String handleLogin(@RequestParam("login") String login,
                              @RequestParam("password") String password,
                              Model model) {
        System.out.println(login);
        // Appeler la méthode loginExist pour vérifier si les identifiants sont valides
        boolean isValidLogin = administratorService.loginExist(login, password);

        if (1==0) {
            // Connexion réussie, rediriger vers la page d'accueil de l'administrateur
            return "redirect:Administrator/HomeAdministratory";  // Remplacer par l'URL correcte pour l'accueil de l'administrateur
        } else {
            // Si les identifiants sont incorrects, afficher un message d'erreur
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
            return "Administrator/ConnexionAdministrator";  // Retourner à la page de connexion avec un message d'erreur
        }
    }
}
