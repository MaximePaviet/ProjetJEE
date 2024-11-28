package controllers.Teacher;

import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import models.Teacher;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.CourseService;
import services.HibernateUtil;
import services.StudentService;
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

    private List<Course> getCoursesWithoutTeacher() {
        List<Course> courses = new ArrayList<>();

        // HQL pour récupérer l'ID en fonction du login et du mot de passe
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
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        List<Course> courses = getCoursesWithoutTeacher(); //liste des cours non enseignés par un prof

        request.setAttribute("courses", courses);

        request.getRequestDispatcher("/view/Teacher/AssignmentCourseTeacher.jsp").forward(request, response);

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

            // Rechargez les données de l'étudiant après modification
            Teacher updatedTeacher = teacherService.readTeacher(teacher.getIdTeacher());
            request.getSession().setAttribute("teacher", updatedTeacher);
            request.getSession().setAttribute("courses", updatedTeacher.getCourseList());
        } else {
            // Aucun cours sélectionné
            request.setAttribute("error", "Aucun cours sélectionné.");
        }

        response.sendRedirect(request.getContextPath() + "/ProfileTeacherServlet");
    }


}
