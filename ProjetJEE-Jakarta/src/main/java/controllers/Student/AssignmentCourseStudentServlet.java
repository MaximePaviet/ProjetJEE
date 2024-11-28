package controllers.Student;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.CourseService;
import services.HibernateUtil;
import services.StudentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/AssignmentCourseStudentServlet")
public class AssignmentCourseStudentServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession().getAttribute("student");
        List<Course> coursesStudentList = student.getCourseList(); //liste des cours suivis par l'étudiant
        List<Course> allCoursesList = courseService.readCourseList(); //liste de tous les cours

        List<Course> courses = new ArrayList<>();  //liste des cours non suivis par l'étudiant

        for (Course course : allCoursesList) {
            boolean isFollowed = false;
            for (Course followedCourse : coursesStudentList) {
                if (followedCourse.getIdCourse() == course.getIdCourse()) {
                    isFollowed = true;
                    break;
                }
            }
            if (!isFollowed) {
                courses.add(course);
            }
        }

        request.setAttribute("courses", courses);

        request.getRequestDispatcher("/view/Student/AssignmentCourseStudent.jsp").forward(request, response);



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

            // Rechargez les données de l'étudiant après modification
            Student updatedStudent = studentService.readStudent(student.getIdStudent());
            request.getSession().setAttribute("student", updatedStudent);
            request.getSession().setAttribute("courses", updatedStudent.getCourseList());
        } else {
            // Aucun cours sélectionné
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        // Redirigez vers la page Profil pour afficher les données actualisées
        response.sendRedirect(request.getContextPath() + "/ProfileStudentServlet");
    }


}
