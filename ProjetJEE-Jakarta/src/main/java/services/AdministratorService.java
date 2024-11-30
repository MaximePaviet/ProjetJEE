package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.security.SecureRandom;

public class AdministratorService {

    private EntityManagerFactory entityManagerFactory;

    //Initializing the EntityManagerFactory
    public AdministratorService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    //Closing the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    // Generates a unique login from the first and last name for a teacher
    public String generateUniqueLoginTeacher(String firstName, String lastName) {
        String baseLogin = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String uniqueLogin = baseLogin;
        int count = 1;

        while (isLoginExistsTeacher(uniqueLogin)) {
            uniqueLogin = baseLogin + count;
            count++;
        }

        return uniqueLogin;
    }

    // Check if the login already exists in the database for a teacher
    public boolean isLoginExistsTeacher(String login) {
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

    // Generates a unique login from the first and last name for a student
    public String generateUniqueLoginStudent(String firstName, String lastName) {
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

    // Check if the login already exists in the database for a student
    public boolean isLoginExistsStudent(String login) {
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

    // Generate a random 9 character password
    public String generatePassword() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(9);

        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
