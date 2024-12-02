package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddTeacherAdminController {

    private final TeacherService teacherService;

    // Injection du service TeacherService via le constructeur
    @Autowired
    public AddTeacherAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Méthode pour gérer l'ajout d'un enseignant
    @PostMapping("/AddTeacherAdminController")
    public String addTeacher(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            RedirectAttributes redirectAttributes) {

        // Créer le contact de l'enseignant
        String contact = name + surname + "@cy-tech.fr";

        // Utiliser le service pour créer l'enseignant
        teacherService.createTeacher(name, surname, contact);

        // Ajouter un message de succès dans les attributs de redirection
        redirectAttributes.addFlashAttribute("message", "Enseignant ajouté avec succès!");

        // Rediriger vers la page des enseignants
        return "redirect:/TeacherPageAdminController";
    }
}

