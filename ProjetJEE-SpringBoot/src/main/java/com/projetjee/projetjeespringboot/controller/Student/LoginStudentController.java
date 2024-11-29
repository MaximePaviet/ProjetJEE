package com.projetjee.projetjeespringboot.controller.Student;

import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginStudentController {

    private final StudentService studentService;

    // Injection du service AdministratorService
    public LoginStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/LoginStudentController")
    public String showLoginPage() {
        // Préparation du message d'erreur (vide par défaut)
        return "Student/ConnexionStudent"; // Nom de la page HTML Thymeleaf
    }

    @PostMapping("/LoginStudentController")
    public String handleLogin(
            @RequestParam String login,
            @RequestParam String password,
            Model model) {

        // Validation des identifiants via le service
        if (studentService.loginExist(login, password)) {
            return "redirect:views/Student/ProfileStudent"; // Redirection si succès
        } else {
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
            return "/Student/ConnexionStudent"; // Recharge la page avec un message d'erreur
        }
    }
}
