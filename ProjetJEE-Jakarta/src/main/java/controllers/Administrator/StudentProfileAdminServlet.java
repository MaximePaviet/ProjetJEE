package controllers.Administrator;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/StudentProfileAdminServlet")
public class StudentProfileAdminServlet extends HttpServlet {

    private StudentService studentService;
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService();
        courseService = new CourseService();
    }

    @Override
    public void destroy() {

        studentService.close();
        courseService.close();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStudentParam = request.getParameter("idStudent");

        if (idStudentParam != null && !idStudentParam.isEmpty()) {
            try {
                int idStudent = Integer.parseInt(idStudentParam);

                // Récupération de l'étudiant
                Student student = studentService.readStudent(idStudent);
                if (student != null) {
                    List<Course> courses = student.getCourseList();

                    // Calcul des moyennes par cours
                    Map<Course, Double> coursesWithAverages = new HashMap<>();
                    for (Course course : courses) {
                        double average = courseService.calculateStudentAverageInCourse(course.getIdCourse(), idStudent);
                        coursesWithAverages.put(course, average);
                    }

                    // Ajouter les données en tant qu'attributs de requête
                    request.setAttribute("student", student);
                    request.setAttribute("coursesWithAverages", coursesWithAverages);

                    // Rediriger vers la JSP
                    request.getRequestDispatcher("/view/Administrator/StudentProfileAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Étudiant introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant manquant");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("search");
        String promoParam = request.getParameter("promo");

        List<Student> students;

        // Si un terme de recherche est fourni
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            students = studentService.searchStudent(searchTerm);
        } else {
            // Sinon, rechercher tous les étudiants (ou appliquer le filtre)
            if (promoParam != null && !promoParam.trim().isEmpty()) {
                // Traiter les promotions sélectionnées
                String[] promoArray = promoParam.split(",");
                students = studentService.getStudentsByPromo(promoArray);
            } else {
                // Pas de filtre sur les promotions
                students = studentService.readStudentList();
            }
        }

        // Ajouter les étudiants comme attribut
        request.setAttribute("students", students);

        // Ajouter les promotions sélectionnées pour pré-cocher les cases dans la JSP
        if (promoParam != null && !promoParam.trim().isEmpty()) {
            String[] promoArray = promoParam.split(",");
            request.setAttribute("selectedPromos", promoArray); // Les promotions sélectionnées à transmettre à la JSP
            request.setAttribute("showFilter", "true"); // Attribut pour indiquer que le filtre doit être visible
        } else {
            request.setAttribute("showFilter", "false"); // Attribut pour indiquer que le filtre doit être caché
        }

        // Redirection vers la JSP
        request.getRequestDispatcher("/view/Administrator/StudentPageAdmin.jsp").forward(request, response);
    }


}
