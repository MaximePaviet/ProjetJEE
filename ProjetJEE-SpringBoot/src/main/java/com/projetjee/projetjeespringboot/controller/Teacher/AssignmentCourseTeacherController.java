package com.projetjee.projetjeespringboot.controller.Teacher;

import com.projetjee.projetjeespringboot.models.Course;
import com.projetjee.projetjeespringboot.models.Teacher;
import com.projetjee.projetjeespringboot.services.CourseService;
import com.projetjee.projetjeespringboot.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/AssignmentCourseTeacherController")
public class AssignmentCourseTeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public String showCoursesWithoutTeacher(@RequestParam("idTeacher") int idTeacher, Model model) {
        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            return "redirect:/LoginTeacherController";
        }

        List<Course> courses = courseService.getCoursesWithoutTeacher();
        model.addAttribute("idTeacher", idTeacher);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);

        return "Teacher/AssignmentCourseTeacher";
    }

    @PostMapping
    public String assignCoursesToTeacher(
            @RequestParam("idTeacher") int idTeacher,
            @RequestParam(value = "courseSelection", required = false) List<Integer> selectedCourseIds,
            Model model) {

        Teacher teacher = teacherService.readTeacher(idTeacher);
        if (teacher == null) {
            return "redirect:/LoginTeacherController";
        }

        if (selectedCourseIds == null || selectedCourseIds.isEmpty()) {
            model.addAttribute("error", "Aucun cours sélectionné.");
            return "Teacher/AssignmentCourseTeacher";
        }

        List<String> errors = new ArrayList<>();
        for (Integer courseId : selectedCourseIds) {
            try {
                teacherService.assignmentCourseToTeacher(idTeacher, courseId);
            } catch (IllegalArgumentException e) {
                errors.add("Erreur avec le cours ID " + courseId + " : " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "Teacher/AssignmentCourseTeacher";
        }

        return "redirect:/ProfileTeacherController?idTeacher=" + idTeacher;
    }
}
