package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
            String name, String surname, String contact);

    boolean existsByLogin(String login);
    // Ajout de la méthode pour trouver un enseignant par login et mot de passe
    @Query("SELECT t.idTeacher FROM Teacher t WHERE t.login = :login AND t.password = :password")
    Integer findIdByLoginAndPassword(@Param("login") String login, @Param("password") String password);
}