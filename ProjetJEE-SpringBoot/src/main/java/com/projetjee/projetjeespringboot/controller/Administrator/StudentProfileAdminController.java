package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.StudentService;
import com.projetjee.projetjeespringboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import java.util.List;

@Controller
public class StudentProfileAdminController {

    private final StudentService studentService;
    private final CourseService courseService;

    // Injection du service StudentService via le constructeur
    @Autowired
    public StudentProfileAdminController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
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
                // Calcul des moyennes par cours
                Map<Course, Double> coursesWithAverages = new HashMap<>();
                for (Course course : courses) {
                    double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), idStudent);
                    coursesWithAverages.put(course, average);
                }
                    // Ajouter les données au modèle
                    model.addAttribute("student", student);
                    model.addAttribute("courses", courses);
                    model.addAttribute("courseWithAverages", coursesWithAverages);

                    // Retourner la vue pour afficher les détails
                    return "Administrator/StudentProfileAdmin";
                } else{
                    // Gestion d'erreur : étudiant introuvable
                    model.addAttribute("error", "Étudiant introuvable");
                    return "Administrator/StudentProfileAdmin";
                }
            } catch(NumberFormatException e){
                // Gestion d'erreur : ID invalide
                model.addAttribute("error", "ID étudiant invalide");
                return "Administrator/StudentProfileAdmin";
            }
        }
    @GetMapping("/StudentProfileAdminController")
    public String getStudents(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "promo", required = false) String promoParam,
            Model model) {

        List<Student> students;

        // Recherche des étudiants par nom ou prénom
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            students = studentService.searchStudent(searchTerm);
        }
        // Filtrer les étudiants par promotions
        else if (promoParam != null && !promoParam.trim().isEmpty()) {
            String[] promoArray = promoParam.split(",");
            students = studentService.getStudentsByPromo(promoArray);
        }
        // Récupérer tous les étudiants
        else {
            students = studentService.readStudentList();
        }

        // Ajouter les étudiants au modèle
        model.addAttribute("students", students);

        // Ajouter les promotions sélectionnées au modèle pour pré-cocher les cases dans la vue
        if (promoParam != null && !promoParam.trim().isEmpty()) {
            String[] promoArray = promoParam.split(",");
            model.addAttribute("selectedPromos", Arrays.asList(promoArray));
            model.addAttribute("showFilter", true); // Afficher le filtre actif
        } else {
            model.addAttribute("showFilter", false); // Pas de filtre actif
        }

        // Retourne la vue associée
        return "Administrator/StudentPageAdmin"; // Vue située dans "src/main/resources/templates/Administrator/StudentPageAdmin.html"
    }
    }