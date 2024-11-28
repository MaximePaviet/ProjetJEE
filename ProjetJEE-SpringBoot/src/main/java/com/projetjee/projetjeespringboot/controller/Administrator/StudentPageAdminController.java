package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String getStudentPage(Model model) {
        // Récupération de la liste des étudiants
        List<Student> students = studentService.readStudentList();

        // Ajout de la liste des étudiants au modèle
        model.addAttribute("students", students);

        // Retourner la vue JSP
        return "Administrator/StudentPageAdmin";  // "StudentPageAdmin.jsp" dans src/main/webapp/WEB-INF/views
    }
}
