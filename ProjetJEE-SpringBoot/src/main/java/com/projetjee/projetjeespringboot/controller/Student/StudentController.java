package com.projetjee.projetjeespringboot.controller.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/views/Student/ProfileStudent")
    public String showStudentPage() {
        return "Student/ProfileStudent"; // Correspond au fichier JSP de la page d'accueil
    }
}
