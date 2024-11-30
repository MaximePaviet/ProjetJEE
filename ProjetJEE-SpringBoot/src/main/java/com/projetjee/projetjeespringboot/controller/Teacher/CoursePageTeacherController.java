package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.repositories.AssessmentRepository;
import com.projetjee.projetjeespringboot.services.AssessmentService;
import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/CoursePageTeacherController")
public class CoursePageTeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentService assessmentService;

    public List<Assessment> getAssessmentsByCourseId(Integer idCourse) {
        return assessmentRepository.findByCourseId(idCourse);
    }


    /**
     * Affiche les détails d'un cours et ses évaluations (GET).
     */
    @GetMapping
    public String showCourseDetails(@RequestParam("idCourse") Integer idCourse, Model model) {
        if (idCourse == null) {
            model.addAttribute("error", "ID cours manquant");
            return "error";
        }

        try {
            // Récupérer le cours
            Course course = courseService.readCourse(idCourse);
            if (course == null) {
                model.addAttribute("error", "Cours introuvable.");
                return "error";
            }

            // Récupérer les évaluations liées au cours
            List<Assessment> assessments = assessmentService.getAssessmentsByCourseId(idCourse);

            // Calcul des meilleures et pires notes
            Map<Integer, Map<String, Float>> minMaxGrades = assessmentService.calculateMinMaxGrades(assessments);

            // Ajouter les attributs au modèle pour la vue
            model.addAttribute("course", course);
            model.addAttribute("assessments", assessments);
            model.addAttribute("minMaxGrades", minMaxGrades);

            return "Teacher/CoursePageTeacher"; // Vue Thymeleaf ou JSP associée
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue : " + e.getMessage());
            return "error";
        }
    }

    /**
     * Traite les requêtes POST si nécessaire (optionnel).
     */
    @PostMapping
    public String processCourseDetails(@RequestParam("idCourse") Integer idCourse, Model model) {
        return showCourseDetails(idCourse, model); // Réutiliser la méthode GET pour éviter la duplication
    }
}

