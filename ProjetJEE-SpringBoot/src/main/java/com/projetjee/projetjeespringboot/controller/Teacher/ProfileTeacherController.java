package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProfileTeacherController {

    @Autowired
    private final CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    public ProfileTeacherController(CourseService courseService) {
        this.courseService = courseService;
    }
    @GetMapping("/ProfileTeacherController")
    @Transactional
    public String showTeacherProfile(@RequestParam("idTeacher") int idTeacher, Model model) {
        // Récupération de l'enseignant
        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            model.addAttribute("error", "Enseignant introuvable.");
            return "redirect:/LoginTeacherController";
        }

        // Forcer le chargement de la liste des cours
        Hibernate.initialize(teacher.getCourseList());
        List<Course> courses = teacher.getCourseList();


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
        model.addAttribute("idTeacher", idTeacher);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courseAverages", courseAverages);
        model.addAttribute("courses", courses);

        return "/Teacher/ProfileTeacher"; // Nom de la vue JSP
    }


}
