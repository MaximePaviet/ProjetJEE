package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.CourseRepository;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    private final GradeRepository gradeRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, GradeRepository gradeRepository) {
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
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
    // Récupérer un cours par ID
    public Course readCourse(Integer idCourse) {
        return courseRepository.findById(idCourse).orElse(null); // Renvoie null si le cours n'existe pas
    }
    public double calculateCourseAverage(int courseId) {
        // Récupérer les notes pour le cours donné
        List<Double> grades = gradeRepository.findGradesByCourseId(courseId);

        // Retourner la moyenne des notes ou 0.0 si la liste est vide
        return grades.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0); // Valeur par défaut si aucune note n'est trouvée
    }


    // Calcule la moyenne d'un étudiant dans un cours
    public double calculateStudentAverageInCourse(int courseId, int studentId) {
        List<Double> grades = gradeRepository.findGradesByCourseIdAndStudentId(courseId, studentId);
        return grades.isEmpty() ? 0.0 : grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

}