package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.EmailSenderService;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/DeleteStudentAdminController")
public class DeleteStudentAdminController {

    private final StudentService studentService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public DeleteStudentAdminController(StudentService studentService, EmailSenderService emailSenderService) {
        this.studentService = studentService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping
    public String deleteStudent(@RequestParam Integer idStudent, RedirectAttributes redirectAttributes) {
        try {
            // Récupérer l'étudiant
            Student student = studentService.readStudent(idStudent);
            if (student == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable.");
                return "redirect:/StudentPageAdminController";
            }

            // Préparer et envoyer l'email
            String htmlContent = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333;\">" +
                    "<h2>Bonjour " + student.getName() + " " + student.getSurname() + ",</h2>" +
                    "<p>Votre compte CyScolarité a été supprimé.</p>" +
                    "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                    "<p style=\"color: #999; font-size: 0.9em;\">--<br>L'équipe CyScolarité</p>" +
                    "</body>" +
                    "</html>";

            emailSenderService.sendEmail(
                    student.getContact(),
                    "Suppression de votre compte CyScolarité",
                    htmlContent
            );

            // Supprimer l'étudiant
            studentService.deleteStudent(idStudent);

            // Ajouter un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "L'étudiant a été supprimé avec succès.");
        } catch (Exception e) {
            // Ajouter un message d'erreur
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression de l'étudiant.");
        }

        // Rediriger vers la page des étudiants
        return "redirect:/StudentPageAdminController";
    }
}
