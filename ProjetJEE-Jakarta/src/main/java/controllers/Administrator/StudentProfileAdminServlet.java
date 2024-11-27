package controllers.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Course;
import models.Student;
import services.StudentService;

import java.io.IOException;
import java.util.List;

@WebServlet("/StudentProfileAdminServlet")
public class StudentProfileAdminServlet extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStudentParam = request.getParameter("idStudent");

        if (idStudentParam != null && !idStudentParam.isEmpty()) {
            try {
                int idStudent = Integer.parseInt(idStudentParam);

                // Récupération de l'enseignant
                Student student = studentService.readStudent(idStudent);
                if (student != null) {

                    List<Course> list = student.getCourseList();
                    // Ajouter les données en tant qu'attributs de requête
                    request.setAttribute("student", student);
                    request.setAttribute("courses", list);

                    // Rediriger vers la JSP
                    request.getRequestDispatcher("/view/Administrator/StudentProfileAdmin.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Etudiant introuvable");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant invalide");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID étudiant manquant");
        }
    }


    @Override
    public void destroy() {
        studentService.close();
    }
}
