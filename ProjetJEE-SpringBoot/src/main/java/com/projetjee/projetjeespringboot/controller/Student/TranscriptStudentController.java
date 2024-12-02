package com.projetjee.projetjeespringboot.controller.Student;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.AssessmentService;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.StudentService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TranscriptStudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private StudentService studentService;  // Service pour récupérer l'étudiant par son ID



    @GetMapping("/TranscriptStudentController")
    public String showTranscriptStudent(@RequestParam(name = "idStudent") int idStudent, Model model) {
        // Récupérer l'objet Student via le service
        Student student = studentService.readStudent(idStudent);

        if (student == null) {
            return "redirect:/LoginStudentController";  // Rediriger si l'étudiant n'est pas trouvé
        }

        // Récupérer les cours suivis par l'étudiant
        List<Course> courses = student.getCourseList();

        // Préparer les données pour la JSP
        Map<Course, Double> coursesWithAverages = new HashMap<>();
        Map<Integer, Map<Assessment, Double>> assessmentsWithGradesByCourse = new HashMap<>();

        for (Course course : courses) {
            // Calculer la moyenne dans chaque cours
            double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), student.getIdStudent());
            coursesWithAverages.put(course, average);

            // Récupérer les évaluations et leurs grades pour ce cours et cet étudiant
            Map<Assessment, Double> assessmentsWithGrades = assessmentService.getAssessmentsAndGradesByCourseAndStudent(course.getIdCourse(), student.getIdStudent());
            assessmentsWithGradesByCourse.put(course.getIdCourse(), assessmentsWithGrades);
        }

        // Passer les informations à la vue
        model.addAttribute("idStudent", idStudent);
        model.addAttribute("coursesWithAverages", coursesWithAverages);
        model.addAttribute("assessmentsWithGradesByCourse", assessmentsWithGradesByCourse);

        // Retourner le bulletin de notes
        return "Student/TranscriptStudent";
    }
}
