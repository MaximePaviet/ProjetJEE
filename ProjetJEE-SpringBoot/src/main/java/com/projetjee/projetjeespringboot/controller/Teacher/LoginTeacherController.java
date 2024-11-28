package com.projetjee.projetjeespringboot.controller.Teacher;


import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginTeacherController {

    private final TeacherService teacherService;

    // Injection du service TeacherService via le constructeur
    @Autowired
    public LoginTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/loginTeacher")
    public String showLoginPage() {
        return "Teacher/ConnexionTeacher"; // Retourne la vue pour la page de connexion
    }

    @PostMapping("/loginTeacher")
    public String loginTeacher(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            Model model) {

        // Recherche de l'ID de l'enseignant par login et mot de passe
        Integer idTeacherParam = teacherService.findTeacherIdByLoginAndPassword(login, password);

        if (idTeacherParam != null) {
            // Lecture des informations de l'enseignant
            Teacher teacher = teacherService.readTeacher(idTeacherParam);

            if (teacher != null) {
                // Ajout de l'objet Teacher au modèle pour l'afficher dans la vue
                model.addAttribute("teacher", teacher);
                return "Teacher/ProfileTeacher"; // Page de profil de l'enseignant
            } else {
                model.addAttribute("errorMessage", "Enseignant introuvable !");
            }
        } else {
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
        }
        return "Teacher/ConnexionTeacher"; // Retour à la page de connexion en cas d'erreur
    }
}
