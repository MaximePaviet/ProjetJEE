package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    List<Teacher> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
            String name, String surname, String contact);

    boolean existsByLogin(String login);
}