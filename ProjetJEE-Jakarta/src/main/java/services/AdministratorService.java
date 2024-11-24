package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.security.SecureRandom;

public class AdministratorService {

    private EntityManagerFactory entityManagerFactory;

    // Constructeur pour initialiser l'EntityManagerFactory
    public AdministratorService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    // Méthode pour fermer EntityManagerFactory lorsque plus nécessaire
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    // Génère un login unique basé sur le prénom et le nom
    public  String generateUniqueLogin(String firstName, String lastName) {
        String baseLogin = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueLogin = baseLogin;
        int count = 1;

        // Vérifie si le login existe déjà dans la base de données
        while (isLoginExists(uniqueLogin)) {
            uniqueLogin = baseLogin + count;
            count++;
        }

        return uniqueLogin;
    }

    // Vérifie dans la base de données si le login existe déjà
    public  boolean isLoginExists(String login) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        long count = 0;

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Teacher t WHERE t.login = :login", Long.class);
            query.setParameter("login", login);
            count = query.getSingleResult();
        } finally {
            entityManager.close();
        }

        return count > 0;
    }
    // Génère un login unique basé sur le prénom et le nom
    public  String generateUniqueLoginStudent(String firstName, String lastName) {
        String baseLogin = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueLogin = baseLogin;
        int count = 1;

        // Vérifie si le login existe déjà dans la base de données
        while (isLoginExistsStudent(uniqueLogin)) {
            uniqueLogin = baseLogin + count;
            count++;
        }

        return uniqueLogin;
    }
    // Vérifie dans la base de données si le login existe déjà
    public  boolean isLoginExistsStudent(String login) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        long count = 0;

        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(s) FROM Student s WHERE s.login = :login", Long.class);
            query.setParameter("login", login);
            count = query.getSingleResult();
        } finally {
            entityManager.close();
        }

        return count > 0;
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

    //public List<List<Course>> readStudentCourses(){}


}
