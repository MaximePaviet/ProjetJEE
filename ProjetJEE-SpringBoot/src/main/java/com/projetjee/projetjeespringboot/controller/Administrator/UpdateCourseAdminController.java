package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.projetjee.projetjeespringboot.services.CourseService;

@Controller
@RequestMapping("/UpdateCourseAdminController")
public class UpdateCourseAdminController {

    private final CourseService courseService;

    @Autowired
    public UpdateCourseAdminController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Afficher le formulaire pour mettre à jour un cours
    @GetMapping
    public String updateCourseForm(@RequestParam("idCourse") Integer idCourse, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Récupérer le cours avec l'ID
            Course course = courseService.readCourse(idCourse);
            if (course != null) {
                model.addAttribute("course", course);
                return "Administrator/UpdateCourseAdmin";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Cours introuvable");
                return "redirect:/CoursePageAdminController";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la récupération du cours");
            return "redirect:/CoursePageAdminController";
        }
    }

    // Traitement du formulaire pour mettre à jour le cours
    @PostMapping
    public String updateCourse(@RequestParam("idCourse") Integer idCourse, @RequestParam("name") String nameCourse, RedirectAttributes redirectAttributes) {
        try {
            // Mise à jour du cours
            courseService.updateCourse(idCourse, nameCourse);
            redirectAttributes.addFlashAttribute("successMessage", "Cours mis à jour avec succès");
            return "redirect:/CoursePageAdminController"; // Rediriger vers la page des cours
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du cours");
            return "redirect:/CoursePageAdminController";
        }
    }

}

