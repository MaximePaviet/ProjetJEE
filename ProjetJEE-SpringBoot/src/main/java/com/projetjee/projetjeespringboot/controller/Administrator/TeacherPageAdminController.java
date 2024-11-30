package com.projetjee.projetjeespringboot.controller.Administrator;

import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeacherPageAdminController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherPageAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/TeacherPageAdminController")
    public String getTeacherPage(
            @RequestParam(value = "search", required = false, defaultValue = "") String searchTerm,
            Model model) {

        List<Teacher> teachers;

        if (!searchTerm.trim().isEmpty()) {
            // Effectuer la recherche si un terme est fourni
            teachers = teacherService.searchTeacher(searchTerm);
        } else {
            // Retourner tous les enseignants si aucun terme n'est fourni
            teachers = teacherService.readTeacherList();
        }

        // Ajouter les données nécessaires pour la vue
        model.addAttribute("teachers", teachers);
        model.addAttribute("searchTerm", searchTerm); // Pour pré-remplir le champ de recherche

        return "Administrator/TeacherPageAdmin"; // Vue JSP
    }
}
