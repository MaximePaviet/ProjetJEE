package com.projetjee.projetjeespringboot.controller.Administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
/*
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public AdminController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }*/
    @GetMapping("/views/Administrator/HomeAdministrator")
    public String showAdminHomePage() {
        return "Administrator/HomeAdministrator"; // Correspond au fichier JSP de la page d'accueil
    }

    @GetMapping("/views/Administrator/TeacherPageAdmin")
    public String showTeacherPageAdmin() {
        return "Administrator/TeacherPageAdmin"; // Correspond au fichier JSP de la page d'accueil
    }
    @GetMapping("/views/Administrator/AddTeacherAdmin")
    public String showAddTeacherAdminPage() {
        return "Administrator/AddTeacherAdmin"; // Correspond au fichier JSP de la page d'accueil
    }

    @GetMapping("/views/Administrator/AddStudentAdmin")
    public String showAddStudentAdminPage() {
        return "Administrator/AddStudentAdmin"; // Correspond au fichier JSP de la page d'accueil
    }
    @GetMapping("/views/Administrator/StudentPageAdmin")
    public String showStudentPageAdmin() {
        return "Administrator/StudentPageAdmin"; // Correspond au fichier JSP de la page d'accueil
    }




}
