package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Assessment;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.repositories.AssessmentRepository;
import com.projetjee.projetjeespringboot.services.AssessmentService;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.EmailSenderService;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/AddAssessmentTeacherController")
public class AddAssessmentTeacherController {

    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * Affiche la page pour ajouter une évaluation.
     */
    @GetMapping
    public String showAddAssessmentForm(@RequestParam("idCourse") int idCourse, Model model) {
        // Récupération du cours et des étudiants
        Course course = courseService.readCourse(idCourse);
        if (course == null) {
            model.addAttribute("errorMessage", "Cours introuvable.");
            return "error";
        }
        model.addAttribute("idCourse", idCourse);
        model.addAttribute("course", course);
        model.addAttribute("students", course.getStudentList());
        return "Teacher/AddAssessmentTeacher"; // Vue associée
    }

    /**
     * Traite la soumission du formulaire pour ajouter une évaluation et les notes.
     */
    @PostMapping
    public String addAssessment(
            @RequestParam("nameAssessment") String nameAssessment,
            @RequestParam("idCourse") int idCourse,
            @RequestParam Map<String, String> allParams,
            Model model) {

        // Récupérer le cours
        Course course = courseService.readCourse(idCourse);
        if (course == null) {
            model.addAttribute("errorMessage", "Cours introuvable.");
            return "error";
        }
        model.addAttribute("idCourse", course);
        // Vérifier si une évaluation avec le même nom existe déjà
        if (assessmentService.checkAssessmentExists(course, nameAssessment)) {
            model.addAttribute("errorMessage", "Une évaluation avec ce nom existe déjà pour ce cours.");
            model.addAttribute("course", course);
            model.addAttribute("students", course.getStudentList());
            return "Teacher/AddAssessmentTeacher";
        }

        // Créer une nouvelle évaluation
        assessmentService.createAssessment(course, nameAssessment);

        // Associer les notes aux étudiants
        allParams.forEach((paramName, gradeString) -> {
            if (paramName.startsWith("grade_")) {
                try {
                    int studentId = Integer.parseInt(paramName.substring(6)); // Extraire l'ID étudiant
                    float gradeValue = Float.parseFloat(gradeString); // Note

                    // Charger l'étudiant
                    Student student = studentService.readStudent(studentId);

                    // Ajouter la note à l'évaluation
                    int assessmentId = assessmentRepository.findByNameAndCourseId(nameAssessment, idCourse)
                            .getIdAssessment();
                    assessmentService.createGrade(student, assessmentId, gradeValue);

                    // Envoyer un email à l'étudiant
                    String toEmail = student.getContact();
                    String subject = "Nouvelle note pour " + course.getName();
                    String body = String.format("""
                            <html>
                            <body>
                                <h2>Bonjour %s %s,</h2>
                                <p>Vous avez reçu une nouvelle note pour l'évaluation "<strong>%s</strong>" dans le cours "<strong>%s</strong>":</p>
                                <p><strong>Note : %.2f</strong></p>
                                <p>Connectez-vous à votre espace étudiant pour plus d'informations.</p>
                                <p>Cordialement,</p>
                                <p><strong>L'équipe CyScolarité</strong></p>
                            </body>
                            </html>
                            """, student.getName(), student.getSurname(), nameAssessment, course.getName(), gradeValue);

                    emailSenderService.sendEmail(toEmail, subject, body);

                } catch (NumberFormatException e) {
                    System.err.println("Erreur de conversion pour " + paramName + ": " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Rediriger vers la page du cours
        return "redirect:/CoursePageTeacherController?idCourse=" + idCourse;
    }
}
