package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UpdateTeacherAdminController {

    private final TeacherService teacherService;

    // Injection du service TeacherService via le constructeur
    @Autowired
    public UpdateTeacherAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Méthode pour afficher le formulaire de mise à jour d'un enseignant
    @GetMapping("/UpdateTeacherAdminController")
    public String showUpdateTeacherForm(@RequestParam("id") int idTeacher, Model model) {
        // Récupérer l'enseignant à partir de l'ID
        Teacher teacher = teacherService.readTeacher(idTeacher);

        if (teacher != null) {
            model.addAttribute("teacher", teacher);
        } else {
            model.addAttribute("error", "Enseignant introuvable"); // Message d'erreur dans le modèle
        }
        return "/WEB-INF/views/Administrator/UpdateTeacherAdmin.jsp";  // Nom de la JSP
    }

    // Méthode pour gérer la mise à jour de l'enseignant
    @PostMapping("/UpdateTeacherAdminController")
    public String updateTeacher(@RequestParam("id") int idTeacher,
                                @RequestParam("name") String nameTeacher,
                                @RequestParam("surname") String surnameTeacher,
                                @RequestParam("contact") String contactTeacher,
                                RedirectAttributes redirectAttributes) {

        // Mettre à jour l'enseignant
        teacherService.updateTeacher(idTeacher, nameTeacher, surnameTeacher, contactTeacher);

        // Ajouter un message de succès dans les attributs de redirection
        redirectAttributes.addFlashAttribute("message", "Enseignant mis à jour avec succès!");

        // Rediriger vers la page des enseignants après mise à jour
        return "redirect:/TeacherPageAdminController";  // Remplacez par l'URL de votre page de liste des enseignants
    }
}
