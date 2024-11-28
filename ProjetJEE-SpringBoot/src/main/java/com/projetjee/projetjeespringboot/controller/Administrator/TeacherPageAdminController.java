package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TeacherPageAdminController {

    private final TeacherService teacherService;

    // Injection automatique du service via le constructeur
    @Autowired
    public TeacherPageAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/TeacherPageAdminController")  // Cette URL correspondra à la requête HTTP GET
    public String getTeacherPage(Model model) {
        // Récupération de la liste des enseignants
        List<Teacher> teachers = teacherService.readTeacherList();

        // Ajout de la liste des enseignants au modèle pour l'affichage dans la vue
        model.addAttribute("teachers", teachers);

        // Retourne la vue JSP
        return "Administrator/TeacherPageAdmin";  // "TeacherPageAdmin.jsp" dans le dossier src/main/webapp/WEB-INF/views
    }
}
