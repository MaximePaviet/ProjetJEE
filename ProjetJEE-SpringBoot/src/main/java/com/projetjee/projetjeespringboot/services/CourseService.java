package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.models.Teacher;
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
        // Dissocier les étudiants du cours
        List<Student> students = course.getStudentList();
        for (Student student : students) {
            student.getCourseList().remove(course); // Retirer le cours de chaque étudiant
        }

        // Supprimer la relation avec le professeur
        Teacher teacher = course.getTeacher();
        if (teacher != null) {
            teacher.getCourseList().remove(course);
        }

        // Supprimer le cours lui-même
        courseRepository.delete(course);
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
        double total = 0;
        int count = 0;

        try {
            // Rechercher les notes associées au cours
            List<Double> grades = gradeRepository.findGradesByCourseId(courseId);

            // Calculer la moyenne
            for (Double grade : grades) {
                total += grade;
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retourner la moyenne ou -1.0 si aucune note
        return count > 0 ? total / count : -1.0;
    }



    // Calcule la moyenne d'un étudiant dans un cours
    public double calculateStudentAverageInCourse(int courseId, int studentId) {
        List<Double> grades = gradeRepository.findGradesByCourseIdAndStudentId(courseId, studentId);
        return grades.isEmpty() ? 0.0 : grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public List<Course> getCoursesWithoutTeacher() {
        return courseRepository.findByTeacherIsNull();
    }
    public List<Course> searchCourse(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of(); // Retourne une liste vide si le terme est null ou vide
        }
        return courseRepository.findByNameContainingIgnoreCase(searchTerm);
    }

}