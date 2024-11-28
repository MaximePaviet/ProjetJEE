package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfilTeacherController {

    private final CourseService courseService;

    @Autowired
    public ProfilTeacherController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/ProfileTeacher")
    public String showTeacherProfile(HttpSession session, Model model) {
        // Récupérer l'objet Teacher depuis la session
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        // Structure pour stocker les moyennes ou les messages
        Map<Integer, String> courseAverages = new HashMap<>();

        if (teacher != null && teacher.getCourseList() != null) {
            // Parcourir tous les cours de l'enseignant
            for (Course course : teacher.getCourseList()) {
                // Calculer la moyenne pour chaque cours
                double courseAverage = courseService.calculateCourseAverage(course.getIdCourse());

                if (courseAverage > 0) {
                    // Stocker la moyenne avec 2 décimales
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    // Si pas de notes, afficher un message
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Ajouter les attributs au modèle
        model.addAttribute("courses", teacher != null ? teacher.getCourseList() : null);
        model.addAttribute("courseAverages", courseAverages);

        // Retourner la vue ProfileTeacher
        return "Teacher/ProfileTeacher";
    }
}
