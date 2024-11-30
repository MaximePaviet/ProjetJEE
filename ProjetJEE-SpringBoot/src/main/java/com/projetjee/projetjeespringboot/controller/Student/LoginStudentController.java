package com.projetjee.projetjeespringboot.controller.Student;

import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projetjee.projetjeespringboot.models.Student;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/LoginStudentController")
public class LoginStudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String showLoginPage() {
        // Retourne la vue de la page de connexion
        return "Student/ConnexionStudent";  // Le nom du fichier JSP ou template Thymeleaf pour la page de connexion
    }

    @PostMapping
    public String handleLogin(@RequestParam("login") String login, @RequestParam("password") String password, Model model) {
        // Récupérer l'ID de l'étudiant par le login et le mot de passe
        Integer studentId = studentService.getStudentIdByLoginAndPassword(login, password);

        if (studentId != null) {
            // Récupérer l'étudiant complet à partir de l'ID
            Student student = studentService.readStudent(studentId);
            System.out.println(student.getIdStudent());
            if (student != null) {
                // Si l'étudiant est trouvé, on le stocke dans la session
                model.addAttribute("student", student);

                // Redirection vers la page de profil de l'étudiant
                return "redirect:/ProfileStudentController?idStudent=" + student.getIdStudent();
            } else {
                // Si l'étudiant n'est pas trouvé
                model.addAttribute("errorMessage", "Étudiant introuvable !");
                return "Student/ConnexionStudent"; // Renvoie à la page de connexion avec un message d'erreur
            }
        } else {
            // Si les identifiants sont incorrects
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
            return "Student/ConnexionStudent"; // Renvoie à la page de connexion avec un message d'erreur
        }
    }
}
