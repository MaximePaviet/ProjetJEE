package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Administrator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.projetjee.projetjeespringboot.repositories.AdministratorRepository;
import com.projetjee.projetjeespringboot.repositories.StudentRepository;
import com.projetjee.projetjeespringboot.repositories.TeacherRepository;

import java.security.SecureRandom;

@Component
public class AdministratorService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AdministratorRepository administratorRepository;

    // Constructeur pour l'injection des dépendances
    public AdministratorService(TeacherRepository teacherRepository,
                                StudentRepository studentRepository,
                                AdministratorRepository administratorRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.administratorRepository = administratorRepository;
    }

    // Génère un login unique basé sur le prénom et le nom pour un enseignant
    public String generateUniqueLogin(String firstName, String lastName) {
        String baseLogin = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueLogin = baseLogin;
        int count = 1;

        // Vérifie si le login existe déjà dans la base de données
        while (teacherRepository.existsByLogin(uniqueLogin)) {
            uniqueLogin = baseLogin + count;
            count++;
        }

        return uniqueLogin;
    }

    // Génère un login unique basé sur le prénom et le nom pour un étudiant
    public String generateUniqueLoginStudent(String firstName, String lastName) {
        String baseLogin = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueLogin = baseLogin;
        int count = 1;

        // Vérifie si le login existe déjà dans la base de données
        while (studentRepository.existsByLogin(uniqueLogin)) {
            uniqueLogin = baseLogin + count;
            count++;
        }

        return uniqueLogin;
    }

    // Génère un mot de passe aléatoire de 9 caractères
    public String generatePassword() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(9); // Longueur fixe de 9 caractères

        for (int i = 0; i < 9; i++) { // Génère exactement 9 caractères
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
    // Validation des identifiants
    public boolean loginExist(String login, String password) {
        return administratorRepository.findByLoginAndPassword(login, password) != null;
    }
}
