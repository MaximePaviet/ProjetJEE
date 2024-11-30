package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import com.projetjee.projetjeespringboot.services.EmailSenderService;
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

    private EmailSenderService emailSenderService;

    @Autowired
    public UpdateStudentAdminController(StudentService studentService, EmailSenderService emailSenderService) {
        this.studentService = studentService;
        this.emailSenderService = emailSenderService;
    }

    // Méthode GET pour afficher le formulaire de mise à jour
    @GetMapping
    public String showUpdateStudentForm(@RequestParam("idStudent") Integer idStudent, Model model) {
        // Récupération de l'étudiant via l'ID
        Student student = studentService.readStudent(idStudent);

        String htmlContent = "<html>" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                "<h2>Bonjour " + student.getName() + " " + student.getSurname() + ",</h2>" +
                "<p>Bonjour,\n\n</p>" +
                "Votre compte CyScolarité à été modifié. \n</p>" +
                "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                "<p style=\"color: #999; font-size: 0.9em;\">--<br>L'équipe CyScolarité</p>" +
                "</body>" +
                "</html>";
        emailSenderService.sendEmail(student.getContact(), student.getName() + " " + student.getSurname() + "- Modification de votre compte !", htmlContent);

        if (student != null) {
            model.addAttribute("student", student);
            return "Administrator/UpdateStudentAdmin"; // Vue JSP ou Thymeleaf
        } else {
            model.addAttribute("error", "Étudiant introuvable");
            return "/StudentPageAdminController"; // Vue d'erreur
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
