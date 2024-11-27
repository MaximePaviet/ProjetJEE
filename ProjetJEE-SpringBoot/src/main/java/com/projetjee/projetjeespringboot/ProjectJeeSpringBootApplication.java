package com.projetjee.projetjeespringboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectJeeSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectJeeSpringBootApplication.class, args);
        //showHomePage();
    }
    // Méthode de test simple à exécuter au démarrage
   @Bean
    public CommandLineRunner testRunner() {
        return args -> {
            System.out.println("Lancement de l'application avec succès !");

            // Exemple de test ou de tâche à exécuter
            System.out.println("Test : Vérification d'une fonctionnalité...");
            System.out.println("Résultat attendu : Fonctionnalité OK !");
        };
    }
}
