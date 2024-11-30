package controllers.Student;

import models.Student;
import services.HibernateUtil;
import services.StudentService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.io.IOException;

@WebServlet("/LoginStudentServlet")
public class LoginStudentServlet extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init() {
        // Initialization of services
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        // Release of resources
        if (studentService != null) {
            studentService.close();
        }
    }

    //Method to retrieve the student based on login and password
    private Integer getStudentIdByLoginAndPassword(String login, String password) {
        Integer studentId = null;

        String hql = "SELECT s.idStudent FROM Student s WHERE s.login = :login AND s.password = :password";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            studentId = query.uniqueResult();
        } catch (NoResultException e) {
            System.out.println("Aucun étudiant trouvé avec ces identifiants.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentId;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recover the login and password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Integer idTStudentParam = getStudentIdByLoginAndPassword(login, password);

        if (idTStudentParam != null) {
            //Get the student
            Student student = studentService.readStudent(idTStudentParam);

            if (student != null) {
                //Add the data
                request.getSession().setAttribute("student", student);

                //Redirects to the student profile page
                response.sendRedirect(request.getContextPath() + "/ProfileStudentServlet");
            } else {
                request.setAttribute("errorMessage", "Etudiant introuvable !");
                request.getRequestDispatcher("/view/Student/ConnexionStudent.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Student/ConnexionStudent.jsp").forward(request, response);
        }
    }

}
