package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getCoursePage(@RequestParam(value = "search", required = false, defaultValue = "") String searchTerm, Model model) {

        List<Course> courses;

        if (!searchTerm.isBlank()) {
            // Effectuer la recherche avec le terme
            courses = courseService.searchCourse(searchTerm);
        } else {
            // Retourner tous les enseignants si aucun terme de recherche n'est fourni
            courses = courseService.readCourseList();
        }


        // Ajouter les cours en tant qu'attribut du modèle
        model.addAttribute("courses", courses);

        // Retourner la vue JSP
        return "Administrator/CoursePageAdmin";  // Nom de la vue, sans l'extension .jsp
    }
}

