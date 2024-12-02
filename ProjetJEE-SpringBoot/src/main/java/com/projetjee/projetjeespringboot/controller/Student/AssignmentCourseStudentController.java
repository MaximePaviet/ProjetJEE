package com.projetjee.projetjeespringboot.controller.Student;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Student;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AssignmentCourseStudentController")
public class AssignmentCourseStudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String showAssignmentCoursePage(@RequestParam("idStudent") int studentId, Model model) {
        // Récupération de l'étudiant depuis la base de données
        Student student = studentService.readStudent(studentId);
        if (student == null) {
            return "redirect:/LoginStudentController"; // Redirection si l'étudiant n'existe pas
        }

        List<Course> coursesStudentList = student.getCourseList(); // Cours suivis
        List<Course> allCoursesList = courseService.readCourseList(); // Tous les cours

        List<Course> courses = new ArrayList<>(); // Cours non suivis
        for (Course course : allCoursesList) {
            boolean isFollowed = coursesStudentList.stream()
                    .anyMatch(followedCourse -> followedCourse.getIdCourse() == course.getIdCourse());
            if (!isFollowed) {
                courses.add(course);
            }
        }
        model.addAttribute("idStudent", studentId);
        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        return "Student/AssignmentCourseStudent"; // Vue Jsp
    }

    @PostMapping
    public String assignCoursesToStudent(@RequestParam("idStudent") int studentId,
                                         @RequestParam(value = "courseSelection", required = false) List<Integer> selectedCourseIds,
                                         Model model) {
        // Récupération de l'étudiant depuis la base de données
        Student student = studentService.readStudent(studentId);
        if (student == null) {
            return "redirect:/LoginStudentController"; // Redirection si l'étudiant n'existe pas
        }

        // Vérifier si des cours ont été sélectionnés
        if (selectedCourseIds != null && !selectedCourseIds.isEmpty()) {
            for (Integer courseId : selectedCourseIds) {
                Course course = courseService.readCourse(courseId);
                if (course != null) {
                    studentService.registrationCourse(course, student);
                }
            }
        } else {
            model.addAttribute("error", "Aucun cours sélectionné.");
        }

        // Rediriger vers le profil de l'étudiant
        return "redirect:/ProfileStudentController?idStudent=" + studentId;
    }
}
