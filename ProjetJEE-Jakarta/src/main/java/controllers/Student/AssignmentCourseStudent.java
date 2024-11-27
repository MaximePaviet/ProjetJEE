package controllers.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import services.CourseService;
import services.StudentService;

import java.io.IOException;


@WebServlet(name = "AssignmentCourseStudent", value = "/assignmentCourseStudent")
public class AssignmentCourseStudent extends HttpServlet {

    private CourseService courseService;
    private StudentService studentService;

    @Override
    public void init() {
        // Initialisation des services
        courseService = new CourseService();
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        if (courseService != null) {
            courseService.close();
        }
        if (studentService != null) {
            studentService.close();
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des valeurs des checkboxes
        String[] selectedCourseIds = request.getParameterValues("courseSelection");

        Student student = (Student) request.getSession().getAttribute("student");

        // Vérifier si des cours ont été sélectionnés
        if (selectedCourseIds != null && selectedCourseIds.length > 0) {
            for (String courseId : selectedCourseIds) {
                try {
                    int id = Integer.parseInt(courseId);
                    Course course = courseService.readCourse(id);
                    if (course != null) {
                        studentService.registrationCourse(course, student);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Rechargez les données du professeur après modification
            Student updatedStudentr = studentService.readStudent(student.getIdStudent());
            request.getSession().setAttribute("student", updatedStudentr);
            request.getSession().setAttribute("courses", updatedStudentr.getCourseList());
        } else {
            // Aucun cours sélectionné
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        // Redirigez vers la page Profil pour afficher les données actualisées
        response.sendRedirect(request.getContextPath() + "/view/Student/ProfileStudent.jsp");
    }


}
