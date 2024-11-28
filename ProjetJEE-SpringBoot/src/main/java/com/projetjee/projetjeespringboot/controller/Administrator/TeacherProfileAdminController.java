package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeacherProfileAdminController {

    private final TeacherService teacherService;

    // Injection du service TeacherService via le constructeur
    @Autowired
    public TeacherProfileAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Méthode pour récupérer le profil d'un enseignant
    @PostMapping("/TeacherProfileAdminController")
    public String getTeacherProfile(@RequestParam("idTeacher") Integer idTeacher, Model model) {
        if (idTeacher != null) {
            Teacher teacher = teacherService.readTeacher(idTeacher);
            if (teacher != null) {
                // Récupérer la liste des cours de l'enseignant
                List<Course> courses = teacher.getCourseList();

                // Ajouter l'enseignant et ses cours au modèle
                model.addAttribute("teacher", teacher);
                model.addAttribute("courses", courses);

                // Retourner la vue correspondant au profil de l'enseignant
                return "Administrator/TeacherProfileAdmin";
            } else {
                // Retourner une page d'erreur si l'enseignant n'est pas trouvé
                model.addAttribute("error", "Enseignant introuvable");
                return "Administrator/TeacherProfileAdmin";
            }
        } else {
            // Retourner une page d'erreur si l'ID de l'enseignant est manquant ou invalide
            model.addAttribute("error", "ID enseignant manquant");
            return "Administrator/TeacherProfileAdmin";
        }
    }
}

