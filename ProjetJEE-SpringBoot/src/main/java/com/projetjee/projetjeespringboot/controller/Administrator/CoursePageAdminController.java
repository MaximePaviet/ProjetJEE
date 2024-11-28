package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CoursePageAdminController {

    private final CourseService courseService;

    // Injection du service CourseService via le constructeur
    @Autowired
    public CoursePageAdminController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/CoursePageAdminController") // URL de la page des cours pour l'admin
    public String getCoursePage(Model model) {
        // Récupérer la liste des cours
        List<Course> courses = courseService.readCourseList();

        // Ajouter les cours en tant qu'attribut du modèle
        model.addAttribute("courses", courses);

        // Retourner la vue JSP
        return "Administrator/CoursePageAdmin";  // Nom de la vue, sans l'extension .jsp
    }
}

