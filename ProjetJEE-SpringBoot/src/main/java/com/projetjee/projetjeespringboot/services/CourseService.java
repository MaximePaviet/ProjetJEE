package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.CourseRepository;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Méthode pour créer un cours
    @Transactional
    public void createCourse(String name) {
        Course course = new Course();
        course.setName(name);
        courseRepository.save(course);  // Utilise Spring Data JPA pour enregistrer l'entité
    }

    // Méthode pour mettre à jour un cours
    @Transactional
    public void updateCourse(Integer idCourse, String newName) {
        Course course = courseRepository.findById(idCourse).orElse(null);
        if (course != null) {
            course.setName(newName);  // Mettre à jour les informations du cours
            courseRepository.save(course);  // Sauvegarde les modifications
        } else {
            System.out.println("Course not found with ID: " + idCourse);
        }
    }

    // Méthode pour supprimer un cours
    @Transactional
    public void deleteCourse(Integer idCourse) {
        Course course = courseRepository.findById(idCourse).orElse(null);
        if (course != null) {
            courseRepository.delete(course);  // Supprime le cours de la base de données
            System.out.println("Course deleted with ID: " + idCourse);
        } else {
            System.out.println("Course not found with ID: " + idCourse);
        }
    }

    // Méthode pour récupérer tous les cours
    public List<Course> readCourseList() {
        return courseRepository.findAll();  // Récupère tous les cours via Spring Data JPA
    }

    // Méthode pour attribuer un étudiant à un cours (commentée dans l'exemple, peut être activée si nécessaire)
    /*
    @Transactional
    public void assignStudentToCourse(Integer courseId, Integer studentId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student != null) {
                course.getStudentList().add(student);  // Ajoute l'étudiant à la liste des étudiants du cours
                student.getCourseList().add(course);  // Ajoute le cours à la liste des cours de l'étudiant
                courseRepository.save(course);  // Enregistre le cours mis à jour
                studentRepository.save(student);  // Enregistre l'étudiant mis à jour
            } else {
                System.out.println("Student not found with ID: " + studentId);
            }
        } else {
            System.out.println("Course not found with ID: " + courseId);
        }
    }
    */
}
