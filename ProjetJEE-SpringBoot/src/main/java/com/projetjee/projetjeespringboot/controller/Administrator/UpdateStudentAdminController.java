package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;

@Controller
@RequestMapping("/UpdateStudentAdminController")
public class UpdateStudentAdminController {

    private final StudentService studentService;

    @Autowired
    public UpdateStudentAdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Méthode GET pour afficher le formulaire de mise à jour
    @GetMapping
    public String showUpdateStudentForm(@RequestParam("idStudent") Integer idStudent, Model model) {
        // Récupération de l'étudiant via l'ID
        Student student = studentService.readStudent(idStudent);

        if (student != null) {
            model.addAttribute("student", student);
            return "Administrator/UpdateStudentAdmin"; // Vue JSP ou Thymeleaf
        } else {
            model.addAttribute("error", "Étudiant introuvable");
            return "StudentPageAdminController"; // Vue d'erreur
        }
    }

    // Méthode POST pour gérer la mise à jour
    @PostMapping
    public String updateStudent(@RequestParam("idStudent") Integer idStudent,
                                @RequestParam("surname") String surnameStudent,
                                @RequestParam("name") String nameStudent,
                                @RequestParam("birthDate") String birthDateStudent,
                                @RequestParam("contact") String contactStudent,
                                @RequestParam("promoYear") String promoYearStudent,
                                RedirectAttributes redirectAttributes) {
        // Mise à jour de l'étudiant
        studentService.updateStudent(idStudent, nameStudent, surnameStudent, Date.valueOf(birthDateStudent), contactStudent, promoYearStudent);

        // Ajout d'un message de confirmation
        redirectAttributes.addFlashAttribute("message", "Étudiant mis à jour avec succès!");
        return "redirect:/StudentPageAdminController"; // Redirection vers la page des étudiants
    }
}
