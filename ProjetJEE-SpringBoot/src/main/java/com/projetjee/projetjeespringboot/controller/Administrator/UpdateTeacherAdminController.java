package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/UpdateTeacherAdminController")
public class UpdateTeacherAdminController {

    private final TeacherService teacherService;

    @Autowired
    public UpdateTeacherAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Méthode GET pour afficher le formulaire de mise à jour
    @GetMapping
    public String showUpdateTeacherForm(@RequestParam("idTeacher") Integer idTeacher, Model model) {
        // Récupération de l'enseignant via l'ID
        Teacher teacher = teacherService.readTeacher(idTeacher);

        if (teacher != null) {
            model.addAttribute("teacher", teacher);
            return "Administrator/UpdateTeacherAdmin"; // Vue HTML ou JSP
        } else {
            model.addAttribute("error", "Enseignant introuvable");
            return "Administrator/ErrorPage"; // Vue pour les erreurs
        }
    }

    // Méthode POST pour gérer la mise à jour
    @PostMapping
    public String updateTeacher(@RequestParam("idTeacher") Integer idTeacher,
                                @RequestParam("name") String nameTeacher,
                                @RequestParam("surname") String surnameTeacher,
                                @RequestParam("contact") String contactTeacher,
                                RedirectAttributes redirectAttributes) {
        // Mise à jour de l'enseignant
        teacherService.updateTeacher(idTeacher, nameTeacher, surnameTeacher, contactTeacher);

        // Ajout d'un message de confirmation
        redirectAttributes.addFlashAttribute("message", "Enseignant mis à jour avec succès!");
        return "redirect:/TeacherPageAdminController"; // Redirection vers la page des enseignants
    }
}
