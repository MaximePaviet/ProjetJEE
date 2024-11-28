package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    // Méthode pour trouver un administrateur par login et password
    Administrator findByLoginAndPassword(String login, String password);
}