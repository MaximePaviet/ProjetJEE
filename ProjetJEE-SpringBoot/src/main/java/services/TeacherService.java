package services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.Course;
import models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.TeacherRepository;

import java.util.List;

@Service
@Transactional
public class TeacherService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository; // Repository JPA Spring pour Teacher

    public void createTeacher(String name, String surname, String contact) {
        // Génération de login et password
        AdministratorService administratorService = new AdministratorService(); // Si existant
        String login = administratorService.generateUniqueLogin(name, surname);
        String password = administratorService.generatePassword();

        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setSurname(surname);
        teacher.setContact(contact);
        teacher.setLogin(login);
        teacher.setPassword(password);

        entityManager.persist(teacher); // Sauvegarde l'enseignant
        System.out.println("Teacher created successfully with login: " + login);
    }

    public Teacher readTeacher(int idTeacher) {
        return entityManager.find(Teacher.class, idTeacher);
    }

    public void updateTeacher(Integer id, String name, String surname, String contact) {
        Teacher teacher = entityManager.find(Teacher.class, id);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found with ID: " + id);
        }

        if (name != null) teacher.setName(name);
        if (surname != null) teacher.setSurname(surname);
        if (contact != null) teacher.setContact(contact);

        entityManager.merge(teacher);
    }

    public void assignmentCourseToTeacher(Teacher teacher, Course course) {
        Teacher existingTeacher = entityManager.find(Teacher.class, teacher.getIdTeacher());
        Course existingCourse = entityManager.find(Course.class, course.getIdCourse());

        if (existingTeacher == null || existingCourse == null) {
            throw new IllegalArgumentException("Teacher or Course not found");
        }

        if (!existingTeacher.getCourseList().contains(existingCourse)) {
            existingTeacher.getCourseList().add(existingCourse);
            existingCourse.setTeacher(existingTeacher);
        }
    }

    public List<Teacher> searchTeacher(String searchTerm) {
        return teacherRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCaseOrContactContainingIgnoreCase(searchTerm, searchTerm, searchTerm);
    }
}

