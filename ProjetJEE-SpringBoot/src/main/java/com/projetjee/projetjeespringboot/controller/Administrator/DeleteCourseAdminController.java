package com.projetjee.projetjeespringboot.controller.Administrator;


import com.projetjee.projetjeespringboot.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.projetjee.projetjeespringboot.services.CourseService;

@Controller
@RequestMapping("/DeleteCourseAdminController")
public class DeleteCourseAdminController{

    private final CourseService courseService;

    @Autowired
    public DeleteCourseAdminController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Endpoint to delete a course
    @PostMapping
    public String deleteCourse(@RequestParam Integer idCourse, RedirectAttributes redirectAttributes) {

        try {
            // Call the service to delete the course
            courseService.deleteCourse(idCourse);

            // Redirect with a success message
            redirectAttributes.addFlashAttribute("successMessage", "Le cours a été supprimé avec succès.");
        } catch (Exception e) {
            // Redirect with an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du cours.");
        }

        // Redirect to the course page after deletion
        return "redirect:/CoursePageAdminController";
    }


}
