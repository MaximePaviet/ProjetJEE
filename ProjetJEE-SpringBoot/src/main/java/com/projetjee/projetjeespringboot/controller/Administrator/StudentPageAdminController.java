package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentPageAdminController {

    private final StudentService studentService;

    // Injection automatique du service via le constructeur
    @Autowired
    public StudentPageAdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/StudentPageAdminController")  // URL correspondant à la requête GET
    public String getStudentPage(
            @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm,
            @RequestParam(value = "promo", required = false, defaultValue = "") String promoParam,
            Model model) {

        List<Student> students;

        if (!searchTerm.isBlank()) {
            // Recherche par terme
            students = studentService.searchStudent(searchTerm.trim());
        } else if (!promoParam.isBlank()) {
            // Recherche par promotion
            String[] promoArray = promoParam.split(",");
            students = studentService.getStudentsByPromo(promoArray);
        } else {
            // Aucun filtre, récupérer tous les étudiants
            students = studentService.readStudentList();
        }

        // Ajout des données au modèle
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("students", students);

        // Retourner la vue JSP
        return "Administrator/StudentPageAdmin";
    }
}
