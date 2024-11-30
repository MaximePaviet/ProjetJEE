package services;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;


public class GradeService {
    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;

    //Initializing the EntityManagerFactory and the SessionFactory
    public GradeService() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("default");
            org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur d'initialisation de l'EntityManagerFactory ou SessionFactory : " + e.getMessage());
        }
    }


    // Close the EntityManagerFactory
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}

