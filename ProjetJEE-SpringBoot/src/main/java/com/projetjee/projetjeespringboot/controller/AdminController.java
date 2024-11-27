package com.projetjee.projetjeespringboot.controller;

import com.projetjee.projetjeespringboot.models.Administrator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
/*
    private boolean loginExist(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Administrator WHERE login = :login AND password = :password";
            Query<Administrator> query = session.createQuery(hql, Administrator.class);
            query.setParameter("login", login);
            query.setParameter("password", password);
            return query.uniqueResult() != null;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification des identifiants : ", e);
            return false;
        }
    }

    @PostMapping("/adminLogin")
    public String login(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            Model model) {

        if (loginExist(login, password)) {
            return "Administrator/HomeAdministrator"; // Vue pour la page d'accueil de l'administrateur
        } else {
            model.addAttribute("errorMessage", "Login ou mot de passe incorrect !");
            return "Administrator/ConnexionAdministrator"; // Vue pour la page de connexion
        }
    }*/
}
