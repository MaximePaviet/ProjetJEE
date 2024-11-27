package controllers.Teacher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Teacher;
import services.CourseService;
import services.TeacherService;

import java.io.IOException;

@WebServlet(name = "AssignmentTeacherCourseServlet", value = "/assignmentCourseTeacher")
public class AssignmentTeacherCourseServlet extends HttpServlet {

    private CourseService courseService;
    private TeacherService teacherService;

    @Override
    public void init() {
        // Initialisation des services
        courseService = new CourseService();
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        if (courseService != null) {
            courseService.close();
        }
        if (teacherService != null) {
            teacherService.close();
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des valeurs des checkboxes
        String[] selectedCourseIds = request.getParameterValues("courseSelection");

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        // Vérifier si des cours ont été sélectionnés
        if (selectedCourseIds != null && selectedCourseIds.length > 0) {
            for (String courseId : selectedCourseIds) {
                try {
                    int id = Integer.parseInt(courseId);
                    Course course = courseService.readCourse(id);
                    if (course != null) {
                        teacherService.assignmentCourseToTeacher(teacher, course);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Rechargez les données du professeur après modification
            Teacher updatedTeacher = teacherService.readTeacher(teacher.getIdTeacher());
            request.getSession().setAttribute("teacher", updatedTeacher);
            request.getSession().setAttribute("courses", updatedTeacher.getCourseList());
        } else {
            // Aucun cours sélectionné
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        // Redirigez vers la page Profil pour afficher les données actualisées
        response.sendRedirect(request.getContextPath() + "/view/Teacher/ProfileTeacher.jsp");
    }


}
