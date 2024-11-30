package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.CourseService;
import services.HibernateUtil;
import services.TeacherService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/AssignmentCourseTeacherServlet")
public class AssignmentCourseTeacherServlet extends HttpServlet {

    private CourseService courseService;
    private TeacherService teacherService;

    @Override
    public void init() {
        // Initialization of services
        courseService = new CourseService();
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (courseService != null) {
            courseService.close();
        }
        if (teacherService != null) {
            teacherService.close();
        }
    }

    //Recover courses without an assigned teacher
    private List<Course> getCoursesWithoutTeacher() {
        List<Course> courses = new ArrayList<>();

        String hql = "FROM Course c WHERE c.teacher IS NULL";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Course> query = session.createQuery(hql, Course.class);
            courses = query.list();
        } catch (NoResultException e) {
            System.out.println("Aucun cours non enseigné trouvé");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Get the teacher of the session
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        //Recover courses without an assigned teacher
        List<Course> courses = getCoursesWithoutTeacher();

        //Add the data
        request.setAttribute("courses", courses);

        //Redirect to the course registration page for a teacher
        request.getRequestDispatcher("/view/Teacher/AssignmentCourseTeacher.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieving checkbox values
        String[] selectedCourseIds = request.getParameterValues("courseSelection");

        //Get the session teacher
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        // Check if courses have been selected
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

            //Add data after modification
            Teacher updatedTeacher = teacherService.readTeacher(teacher.getIdTeacher());
            request.getSession().setAttribute("teacher", updatedTeacher);
            request.getSession().setAttribute("courses", updatedTeacher.getCourseList());
        } else {
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        //Redirection to the teacher profile page
        response.sendRedirect(request.getContextPath() + "/ProfileTeacherServlet");
    }


}
