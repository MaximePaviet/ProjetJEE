package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.repositories.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.AdministratorRepository;
import com.projetjee.projetjeespringboot.repositories.StudentRepository;
import com.projetjee.projetjeespringboot.repositories.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final AdministratorService administratorService;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          CourseRepository courseRepository,
                          AdministratorService administratorService) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.administratorService = administratorService;
    }

    // Créer un enseignant
    public Teacher createTeacher(String name, String surname, String contact) {
        String login = administratorService.generateUniqueLogin(name, surname);
        String password = administratorService.generatePassword();

        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setSurname(surname);
        teacher.setContact(contact);
        teacher.setLogin(login);
        teacher.setPassword(password);

        return teacherRepository.save(teacher);
    }

    // Lire un enseignant par ID
    public Teacher readTeacher(Integer idTeacher) {
        return teacherRepository.findById(idTeacher)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + idTeacher));
    }

    // Lire la liste des enseignants
    public List<Teacher> readTeacherList() {
        return teacherRepository.findAll();
    }

    // Recherche d'enseignants
    public List<Teacher> searchTeacher(String searchTerm) {
        return teacherRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm);
    }

    // Mettre à jour un enseignant
    public Teacher updateTeacher(Integer id, String name, String surname, String contact) {
        Teacher teacher = readTeacher(id);

        if (name != null) teacher.setName(name);
        if (surname != null) teacher.setSurname(surname);
        if (contact != null) teacher.setContact(contact);

        return teacherRepository.save(teacher);
    }

    // Assigner un cours à un enseignant
    public void assignmentCourseToTeacher(Integer teacherId, Integer courseId) {
        Teacher teacher = readTeacher(teacherId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        if (!teacher.getCourseList().contains(course)) {
            teacher.getCourseList().add(course);
            course.setTeacher(teacher);
            teacherRepository.save(teacher); // Cascade effect handles course update
        } else {
            throw new IllegalArgumentException("Course is already assigned to the teacher.");
        }
    }
}

