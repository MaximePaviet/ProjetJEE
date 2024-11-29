package com.projetjee.projetjeespringboot.controller.Teacher;


import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.repositories.TeacherRepository;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LoginTeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final TeacherRepository teacherRepository;

    // Injection du service TeacherService via le constructeur
    @Autowired
    public LoginTeacherController(TeacherService teacherService, CourseService courseService, TeacherRepository teacherRepository) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/loginTeacher")
    public String showLoginPage() {
        return "Teacher/ConnexionTeacher"; // Retourne la vue pour la page de connexion
    }

    @PostMapping("/loginTeacher")
    public String loginTeacher(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            Model model) {

        // Recherche de l'ID de l'enseignant par login et mot de passe
        Integer idTeacherParam = teacherRepository.findIdByLoginAndPassword(login, password);

        if (idTeacherParam != null) {
            // Lecture des informations de l'enseignant
            Teacher teacher = teacherService.readTeacher(idTeacherParam);

            if (teacher != null) {
                // Ajout de l'objet Teacher au modèle pour l'afficher dans la vue
                model.addAttribute("teacher", teacher);
                return "Teacher/ProfileTeacher"; // Page de profil de l'enseignant
            } else {
                model.addAttribute("errorMessage", "Enseignant introuvable !");
            }
        } else {
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
        }
        return "Teacher/ConnexionTeacher"; // Retour à la page de connexion en cas d'erreur
    }
    @GetMapping("/ProfileTeacher")
    @Transactional // Assure que la session Hibernate reste active pendant l'exécution
    public String showTeacherProfile(@RequestParam("idTeacher") int idTeacher, Model model) {
        // Récupération de l'enseignant
        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            model.addAttribute("error", "Enseignant introuvable.");
            return "error";
        }

        // Forcer le chargement de la liste des cours (lazy loading)
        Hibernate.initialize(teacher.getCourseList());
        List<Course> courses = teacher.getCourseList();

        // Gestion des cours
        if (courses == null || courses.isEmpty()) {
            model.addAttribute("courses", Collections.emptyList());
        } else {
            model.addAttribute("courses", courses);
        }

        // Calcul des moyennes des cours
        Map<Integer, Double> courseAverages = courses.stream()
                .collect(Collectors.toMap(
                        Course::getIdCourse,
                        course -> courseService.calculateCourseAverage(course.getIdCourse())
                ));
        model.addAttribute("teacher", teacher);
        model.addAttribute("courseAverages", courseAverages);

        return "ProfileTeacher"; // Nom de la vue JSP
    }





}
