package controllers.Student;

import models.Student;
import org.hibernate.Hibernate;
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

@WebServlet(name = "LoginStudentServlet", value = "/studentLogin")
public class LoginStudentServlet extends HttpServlet {

    private StudentService studentService;

    @Override
    public void init() {
        // Initialisation des services
        studentService = new StudentService();
    }

    @Override
    public void destroy() {
        if (studentService != null) {
            studentService.close();
        }
        // Fermeture de la SessionFactory via HibernateUtil
        HibernateUtil.shutdown();
    }

    private Integer getStudentIdByLoginAndPassword(String login, String password) {
        Integer studentId = null;

        // HQL pour récupérer l'ID en fonction du login et du mot de passe
        String hql = "SELECT s.idStudent FROM Student s WHERE s.login = :login AND s.password = :password";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            // Récupérer l'ID si un résultat est trouvé
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
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Integer idTStudentParam = getStudentIdByLoginAndPassword(login, password);

        if (idTStudentParam != null) {
            Student student = studentService.readStudent(idTStudentParam);

            if (student != null) {
                // Charger explicitement les cours si nécessaire
                Hibernate.initialize(student.getCourseList());

                // Stocker dans la session
                request.getSession().setAttribute("student", student);

                // Redirection
                response.sendRedirect(request.getContextPath() + "/view/Student/ProfileStudent.jsp");
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
