package controller.Administrator;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import services.AssessmentService;
import services.CourseService;

import java.io.IOException;
import java.util.List;

@WebServlet("/AddAssessmentServlet")
public class AddAssessmentServlet extends HttpServlet {

    private AssessmentService assessmentService ;
    private SessionFactory sessionFactory;

    public void init() throws ServletException {
        // Initialisation des services
        assessmentService = new AssessmentService();

        // Initialisation de la SessionFactory Hibernate pour les opérations liées à la base de données
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    public List<Student> getStudentsByCourse(Integer courseId) {
        Session session = sessionFactory.openSession(); // Ouvrir une session Hibernate
        Transaction transaction = null;
        List<Student> students = null;

        try {
            transaction = session.beginTransaction();

            // Requête JPQL pour récupérer les étudiants liés à un cours donné
            students = session.createQuery(
                            "SELECT s FROM Student s JOIN s.courseList c WHERE c.idCourse = :courseId", Student.class)
                    .setParameter("courseId", courseId)
                    .getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return students;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseIdParam = request.getParameter("idCourse");

        try {
            if (courseIdParam == null || courseIdParam.isEmpty()) {
                throw new IllegalArgumentException("ID du cours non fourni.");
            }

            Integer courseId = Integer.parseInt(courseIdParam);

            // Récupérer la liste des étudiants inscrits dans ce cours
            List<Student> students = getStudentsByCourse(courseId);
            if (students == null || students.isEmpty()) {
                throw new IllegalArgumentException("Aucun étudiant trouvé pour ce cours.");
            }

            // Ajouter les données au contexte de la requête
            request.setAttribute("students", students);
            request.setAttribute("courseId", courseId);

            // Rediriger vers la JSP d'ajout d'évaluation
            request.getRequestDispatcher("/view/Teacher/CoursePageTeacher.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur : " + e.getMessage());
            request.getRequestDispatcher("/view/Teacher/CoursePageTeacher.jsp").forward(request, response);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gestion de l'ajout d'un nouvel enseignant via un formulaire

        // Récupération des paramètres du formulaire
        String surname = request.getParameter("grade");




        // Redirection ou transfert vers la page des enseignants pour afficher la liste mise à jour
        response.sendRedirect(request.getContextPath() + "/StudentPageServlet");
    }
}
