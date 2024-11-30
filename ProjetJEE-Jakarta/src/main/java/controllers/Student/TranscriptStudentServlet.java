package controllers.Student;

import models.Course;
import models.Student;
import models.Assessment;
import services.CourseService;
import services.AssessmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/TranscriptStudentServlet")
public class TranscriptStudentServlet extends HttpServlet {

    private CourseService courseService;
    private AssessmentService assessmentService;

    @Override
    public void init() {
        // Initialization of services
        courseService = new CourseService();
        assessmentService = new AssessmentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        courseService.close();
        assessmentService.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the student from the session
        Student student = (Student) request.getSession().getAttribute("student");

        //Retrieval of grades, evaluations and averages
        List<Course> courses = student.getCourseList();
        Map<Course, Double> coursesWithAverages = new HashMap<>();
        Map<Integer, Map<Assessment, Double>> assessmentsWithGradesByCourse = new HashMap<>();

        for (Course course : courses) {
            double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), student.getIdStudent());
            coursesWithAverages.put(course, average);
            Map<Assessment, Double> assessmentsWithGrades = assessmentService.getAssessmentsAndGradesByCourseAndStudent(course.getIdCourse(), student.getIdStudent());
            assessmentsWithGradesByCourse.put(course.getIdCourse(), assessmentsWithGrades);
        }

        // Add the data
        request.setAttribute("coursesWithAverages", coursesWithAverages);
        request.setAttribute("assessmentsWithGradesByCourse", assessmentsWithGradesByCourse);

        // Redirection to the transcript page
        request.getRequestDispatcher("/view/Student/TranscriptStudent.jsp").forward(request, response);
    }
}
