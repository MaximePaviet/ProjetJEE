package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddCourseAdminController {

    @Autowired
    private CourseService courseService;

    // Affichage de la page pour ajouter un cours (Page des cours)
    @GetMapping("/AddCourseAdminController")
    public String showCoursePage() {
        return "CoursePageAdmin";  // La page de gestion des cours (coursePage.html)
    }

    // Traitement de l'ajout d'un cours via le formulaire (POST)
    @PostMapping("/AddCourseAdminController")
    public String addCourse(@RequestParam("courseName") String courseName, RedirectAttributes redirectAttributes) {
        // Appel au service pour créer un cours
        courseService.createCourse(courseName);

        // Redirection vers la page de gestion des cours avec un message
        redirectAttributes.addFlashAttribute("message", "Cours ajouté avec succès");
        return "redirect:/CoursePageAdminController";  // Redirection vers la page des cours
    }
}
