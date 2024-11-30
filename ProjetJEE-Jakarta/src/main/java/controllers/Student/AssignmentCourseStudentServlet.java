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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AssignmentCourseStudentServlet")
public class AssignmentCourseStudentServlet extends HttpServlet {

    private CourseService courseService;
    private StudentService studentService;

    @Override
    public void init() {
        // Initialization of services
        courseService = new CourseService();
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (courseService != null) {
            courseService.close();
        }
        if (studentService != null) {
            studentService.close();
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the student from the session
        Student student = (Student) request.getSession().getAttribute("student");

        //Retrieve the list of courses not taken by the student
        List<Course> coursesStudentList = student.getCourseList();
        List<Course> allCoursesList = courseService.readCourseList();
        List<Course> courses = new ArrayList<>();

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

        //Add the data
        request.setAttribute("courses", courses);

        //Redirect to course registration page for students
        request.getRequestDispatcher("/view/Student/AssignmentCourseStudent.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieving checkbox values
        String[] selectedCourseIds = request.getParameterValues("courseSelection");

        //Retrieval of the student from the session
        Student student = (Student) request.getSession().getAttribute("student");

        // Check if courses have been selected
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

            // Apply changes
            Student updatedStudent = studentService.readStudent(student.getIdStudent());
            request.getSession().setAttribute("student", updatedStudent);
            request.getSession().setAttribute("courses", updatedStudent.getCourseList());
        } else {
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        // Redirect to the student profile page
        response.sendRedirect(request.getContextPath() + "/ProfileStudentServlet");
    }
}
