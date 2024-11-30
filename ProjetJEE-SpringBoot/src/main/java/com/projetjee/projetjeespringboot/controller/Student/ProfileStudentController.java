package com.projetjee.projetjeespringboot.controller.Student;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileStudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;  // Service pour récupérer l'étudiant par son ID

    @GetMapping("/ProfileStudentController")
    public String showStudentProfile(@RequestParam(name = "studentId") int studentId, Model model) {
        // Récupérer l'objet Student via le service, en utilisant l'ID passé en paramètre
        Student student = studentService.readStudent(studentId);

        if (student == null) {
            return "redirect:/LoginStudentController";  // Rediriger si l'étudiant n'est pas trouvé
        }

        // Structure pour stocker les moyennes des cours
        Map<Integer, String> courseAverages = new HashMap<>();
        List<Course> courses = student.getCourseList();

        if (courses != null) {
            for (Course course : courses) {
                // Calcul de la moyenne pour chaque cours
                double courseAverage = courseService.calculateStudentAverageInCourse(course.getIdCourse(), student.getIdStudent());

                // Si la moyenne est calculée, formatée à 2 décimales
                if (courseAverage > 0) {
                    courseAverages.put(course.getIdCourse(), String.format("%.2f", courseAverage));
                } else {
                    courseAverages.put(course.getIdCourse(), "Pas encore de notes");
                }
            }
        }

        // Passer les informations à la vue Thymeleaf
        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        model.addAttribute("courseAverages", courseAverages);

        // Retourner le nom de la vue Thymeleaf
        return "/Student/ProfileStudent";  // Nom de la vue Thymeleaf (student/profile.html)
    }
}

