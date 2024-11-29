
package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Calendar;

@Controller
public class AddStudentAdminController {

    private final StudentService studentService;

    // Injection du service StudentService via le constructeur
    @Autowired
    public AddStudentAdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/AddStudentAdminController")
    public String addStudent(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("dateBirth") String dateBirthParam, // Paramètre de date au format YYYY-MM-DD
            RedirectAttributes redirectAttributes,
            Model model) {

        // Créer le contact de l'étudiant
        String contact = name + surname + "@cy-tech.fr";


        Date dateBirth = null;
        try {
            if (dateBirthParam != null && !dateBirthParam.isEmpty()) {
                dateBirth = Date.valueOf(dateBirthParam); // Conversion directe de la chaîne au format YYYY-MM-DD
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Format de date invalide !");
            return "Administrator/AddStudentAdmin"; // Retour à la page du formulaire en cas d'erreur
        }

        // Calcul de l'année de promotion
        Calendar calendar = Calendar.getInstance();
        String promoYear = String.valueOf(calendar.get(Calendar.YEAR) + 3); // Année actuelle + 3

        // Utiliser le service pour créer l'étudiant
        assert dateBirthParam != null;
        studentService.createStudent(name, surname, dateBirth, contact, promoYear);

        // Ajouter un message de succès
        redirectAttributes.addFlashAttribute("message", "Étudiant ajouté avec succès!");

        // Redirection vers la page des étudiants
        return "redirect:/StudentPageAdminController";
    }
}
