package com.projetjee.projetjeespringboot.repositories;

import com.projetjee.projetjeespringboot.models.Administrator;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByLogin(String login);
    List<Student> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
            String name, String surname, String contact);

    List<Student> findByCourseListContaining(Course course);

    Optional<Student> findByLoginAndPassword(String login, String password);
}