package controllers.Teacher;

import models.Teacher;
import org.hibernate.Hibernate;
import services.HibernateUtil;
import services.TeacherService;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;

@WebServlet("/LoginTeacherServlet")
public class LoginTeacherServlet extends HttpServlet {

    private TeacherService teacherService;

    @Override
    public void init() {
        // Initialisation des services
        teacherService = new TeacherService();
    }

    @Override
    public void destroy() {
        if (teacherService != null) {
            teacherService.close();
        }
        // Fermeture de la SessionFactory via HibernateUtil
        HibernateUtil.shutdown();
    }

    private Integer getTeacherIdByLoginAndPassword(String login, String password) {
        Integer teacherId = null;

        // HQL pour récupérer l'ID en fonction du login et du mot de passe
        String hql = "SELECT t.idTeacher FROM Teacher t WHERE t.login = :login AND t.password = :password";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(hql, Integer.class);
            query.setParameter("login", login);
            query.setParameter("password", password);

            // Récupérer l'ID si un résultat est trouvé
            teacherId = query.uniqueResult();
        } catch (NoResultException e) {
            System.out.println("Aucun professeur trouvé avec ces identifiants.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teacherId;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Integer idTeacherParam = getTeacherIdByLoginAndPassword(login, password);

        if (idTeacherParam != null) {
            Teacher teacher = teacherService.readTeacher(idTeacherParam);

            if (teacher != null) {
                // Charger explicitement les cours si nécessaire
                Hibernate.initialize(teacher.getCourseList());

                // Stocker dans la session
                request.getSession().setAttribute("teacher", teacher);

                // Redirection
                response.sendRedirect(request.getContextPath() + "/ProfileTeacherServlet");
            } else {
                request.setAttribute("errorMessage", "Enseignant introuvable !");
                request.getRequestDispatcher("/view/Teacher/ConnexionTeacher.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Login ou mot de passe incorrect !");
            request.getRequestDispatcher("/view/Teacher/ConnexionTeacher.jsp").forward(request, response);
        }
    }

}
