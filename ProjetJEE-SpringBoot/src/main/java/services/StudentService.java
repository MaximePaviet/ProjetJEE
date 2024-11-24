package services;

import models.Course;
import models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.CourseRepository;
import repositories.StudentRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service // Indique que cette classe est un service Spring
public class StudentService {

    @Autowired
    private StudentRepository studentRepository; // Injection du repository pour Student

    @Autowired
    private CourseRepository courseRepository; // Injection du repository pour Course

    @Autowired
    private AdministratorService administratorService; // Injection du service administrateur pour générer login et password

    // Crée un nouvel étudiant avec login et mot de passe générés
    @Transactional
    public void createStudent(String name, String surname, Date dateBirth, String contact, String schoolYear) {
        String login = administratorService.generateUniqueLoginStudent(name, surname);
        String password = administratorService.generatePassword();

        // Création de l'objet Student
        Student student = new Student();
        student.setName(name);
        student.setSurname(surname);
        student.setDateBirth(dateBirth);
        student.setContact(contact);
        student.setSchoolYear(schoolYear);
        student.setLogin(login);
        student.setPassword(password);

        // Sauvegarde dans la base de données
        studentRepository.save(student);
    }

    // Met à jour un étudiant
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

            studentRepository.save(student); // Mise à jour de l'étudiant
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
    }

    // Supprime un étudiant par son ID
    @Transactional
    public void deleteStudent(Integer idStudent) {
        if (studentRepository.existsById(idStudent)) {
            studentRepository.deleteById(idStudent);
        } else {
            throw new IllegalArgumentException("Student not found with ID: " + idStudent);
        }
    }

    // Récupère tous les étudiants
    public List<Student> readStudentList() {
        return studentRepository.findAll(); // Utilise le repository pour récupérer tous les étudiants
    }

    // Récupère un étudiant par son ID
    public Student readStudent(Integer idStudent) {
        return studentRepository.findById(idStudent)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + idStudent));
    }

    // Recherche des étudiants par nom, prénom ou contact
    public List<Student> searchStudent(String searchTerm) {
        return studentRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(
                searchTerm, searchTerm, searchTerm);
    }

    // Filtrage des étudiants associés à un cours donné
    public List<Student> filtering(Course course) {
        return studentRepository.findByCourseListContaining(course); // Utilise une méthode custom du repository
    }

    // Inscription d'un étudiant à un cours
    @Transactional
    public void registrationCourse(Course course, Student student) {
        Course existingCourse = courseRepository.findById(course.getIdCourse())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + course.getIdCourse()));

        Student existingStudent = studentRepository.findById(student.getIdStudent())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + student.getIdStudent()));

        if (!existingStudent.getCourseList().contains(existingCourse)) {
            existingStudent.getCourseList().add(existingCourse);
            existingCourse.getStudentList().add(existingStudent);

            studentRepository.save(existingStudent); // Sauvegarde des modifications
            courseRepository.save(existingCourse);   // Sauvegarde des modifications
        } else {
            throw new IllegalStateException("Student is already assigned to the course.");
        }
    }

    // Récupère la liste des cours d'un étudiant
    public List<Course> readCourse(int idStudent) {
        Student student = studentRepository.findById(idStudent)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + idStudent));

        return student.getCourseList();
    }
}