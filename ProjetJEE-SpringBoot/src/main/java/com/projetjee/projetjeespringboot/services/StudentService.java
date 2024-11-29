package com.projetjee.projetjeespringboot.services;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projetjee.projetjeespringboot.repositories.CourseRepository;
import com.projetjee.projetjeespringboot.repositories.StudentRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service // Indique que cette classe est un service Spring
public class StudentService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AdministratorService administratorService;

    // Création d'un étudiant
    @Transactional
    public void createStudent(String name, String surname, Date dateBirth, String contact, String schoolYear) {
        String login = administratorService.generateUniqueLoginStudent(name, surname);
        String password = administratorService.generatePassword();

        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setDateBirth(dateBirth);
        student.setContact(contact);
        student.setSchoolYear(schoolYear);
        student.setLogin(login);
        student.setPassword(password);

        studentRepository.save(student);
    }

    // Mise à jour des informations d'un étudiant
    @Transactional
    public void updateStudent(Integer id, String name, String surname, Date dateBirth, String contact, String schoolYear) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(name);
            student.setSurname(surname);
            student.setDateBirth(dateBirth);
            student.setContact(contact);
            student.setSchoolYear(schoolYear);

            studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
    }

    // Suppression d'un étudiant par ID
    @Transactional
    public void deleteStudent(Integer idStudent) {
        if (studentRepository.existsById(idStudent)) {
            studentRepository.deleteById(idStudent);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + idStudent);
        }
    }

    // Récupération de tous les étudiants
    public List<Student> readStudentList() {
        return studentRepository.findAll();
    }

    // Récupération d'un étudiant par ID
    public Student readStudent(Integer idStudent) {
        return studentRepository.findById(idStudent)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + idStudent));
    }

    // Recherche flexible des étudiants
    public List<Student> searchStudent(String searchTerm) {
        return studentRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm);
    }

    // Filtrage des étudiants inscrits à un cours spécifique
    public List<Student> filtering(Course course) {
        return studentRepository.findByCourseListContaining(course);
    }

    // Inscription d'un étudiant à un cours
    @Transactional
    public void registrationCourse(Course course, Student student) {
        Student existingStudent = studentRepository.findById(student.getIdStudent())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + student.getIdStudent()));

        Course existingCourse = courseRepository.findById(course.getIdCourse())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + course.getIdCourse()));

        if (!existingStudent.getCourseList().contains(existingCourse)) {
            existingStudent.getCourseList().add(existingCourse);
            existingCourse.getStudentList().add(existingStudent);

            studentRepository.save(existingStudent);
            courseRepository.save(existingCourse);
        } else {
            throw new IllegalStateException("Student is already assigned to the course.");
        }
    }

    // Récupération des cours suivis par un étudiant
    public List<Course> readCourse(int idStudent) {
        Student student = studentRepository.findById(idStudent)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + idStudent));
        return student.getCourseList();
    }
    public boolean loginExist(String login, String password) {
        return studentRepository.findByLoginAndPassword(login, password) != null;
    }
}
