package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class StudentProfilAdminController {

    private final StudentService studentService;

    // Injection du service StudentService via le constructeur
    @Autowired
    public StudentProfilAdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/StudentProfileAdminController")
    public String getStudentProfile(@RequestParam("idStudent") Integer idStudent, Model model) {
        if (idStudent == null) {
            model.addAttribute("error", "ID étudiant manquant");
            return "Administrator/StudentProfileAdmin";
        }

        try {
            // Récupération de l'étudiant via le service
            Student student = studentService.readStudent(idStudent);

            if (student != null) {
                // Récupérer les cours associés à l'étudiant
                List<Course> courses = student.getCourseList();

                // Ajouter les données au modèle
                model.addAttribute("student", student);
                model.addAttribute("courses", courses);

                // Retourner la vue pour afficher les détails
                return "Administrator/StudentProfileAdmin";
            } else {
                // Gestion d'erreur : étudiant introuvable
                model.addAttribute("error", "Étudiant introuvable");
                return "Administrator/StudentProfileAdmin";
            }
        } catch (NumberFormatException e) {
            // Gestion d'erreur : ID invalide
            model.addAttribute("error", "ID étudiant invalide");
            return "Administrator/StudentProfileAdmin";
        }
    }
}
