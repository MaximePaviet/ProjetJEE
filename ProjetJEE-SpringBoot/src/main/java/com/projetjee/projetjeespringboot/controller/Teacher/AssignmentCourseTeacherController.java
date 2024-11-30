package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/AssignmentCourseTeacherController")
public class AssignmentCourseTeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    /**
     * Affiche les cours disponibles pour être assignés à un enseignant
     */
    @GetMapping
    public String showCoursesWithoutTeacher(@RequestParam("idTeacher") int idTeacher, Model model) {
        // Récupérer l'enseignant
        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            return "redirect:/LoginTeacherController";
        }

        // Récupérer les cours sans professeur
        List<Course> courses = courseService.getCoursesWithoutTeacher();
        model.addAttribute("idStudent", idTeacher);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);

        return "Teacher/AssignmentCourseTeacher"; // Vue Thymeleaf associée
    }

    /**
     * Traite les affectations des cours sélectionnés à l'enseignant
     */
    @PostMapping
    public String assignCoursesToTeacher(
            @RequestParam("idTeacher") int idTeacher,
            @RequestParam(value = "courseSelection", required = false) List<Integer> selectedCourseIds,
            Model model) {

        // Vérifier si l'enseignant existe
        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            return "redirect:/LoginTeacherController";
        }

        // Vérifier si des cours ont été sélectionnés
        if (selectedCourseIds != null && !selectedCourseIds.isEmpty()) {
            for (Integer courseId : selectedCourseIds) {
                try {
                    // Utiliser la méthode qui accepte les IDs
                    teacherService.assignmentCourseToTeacher(idTeacher, courseId);
                } catch (IllegalArgumentException e) {
                    // Ajouter des messages d'erreur spécifiques si une exception est levée
                    model.addAttribute("error", "Erreur : " + e.getMessage());
                    return "error";
                }
            }

            // Mettre à jour l'enseignant dans le modèle
            Teacher updatedTeacher = teacherService.readTeacher(idTeacher);
            model.addAttribute("teacher", updatedTeacher);
            model.addAttribute("courses", updatedTeacher.getCourseList());
        } else {
            model.addAttribute("error", "Aucun cours sélectionné.");
        }

        // Redirection vers la page de profil
        return "redirect:/ProfileTeacherController?idTeacher=" + idTeacher;
    }

}
