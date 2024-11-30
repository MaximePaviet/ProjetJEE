<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Course, com.projetjee.projetjeespringboot.models.Assessment, java.util.List, java.util.Map" %>
<html>
<head>
    <title>Relevé de notes</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Monofett&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&family=Monofett&display=swap" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
        }

        .hiddenForm {
            position: relative;
            visibility: hidden;
        }

        .returnButton {
            all: unset;
            color: #4F2BEC;
            font-family: "DM Sans", sans-serif;
            font-size: 50px;
            font-weight: bold;
            margin: 20px;
            cursor: pointer;
            text-decoration: none;
            visibility: visible;
            position: relative;
        }

        h1 {
            font-size: 36px;
            color: #4F2BEC;
            font-family: 'DM Sans', serif;
            font-weight: bold;
            text-align: center;
            margin: 20px 0;
        }

        table {
            width: 50%;
            margin: 20px auto;
            border-collapse: separate;
            border-spacing: 0;
            font-family: "DM Sans", sans-serif;
            color: #2B3674;
            font-weight: bold;
            border-radius: 12px; /* Arrondir les 4 coins principaux du tableau */
            overflow: hidden; /* Cache les bords arrondis */
        }

        table tr {
            height: 40px; /* Réduction de la hauteur des lignes */
        }

        td, th {
            border: 2px solid #AAC2FF;
            padding: 15px;
        }

        /* Lignes des cours */
        .course-row td {
            background-color: #AAC2FF;
        }

        /* Alignement du texte des notes */
        .right-align {
            text-align: right;
        }

        /* Évaluations */
        .assessment-row td {
            background-color: #f9f9f9;
        }

    </style>
</head>
<body>
<%
    com.projetjee.projetjeespringboot.models.Student student = (com.projetjee.projetjeespringboot.models.Student) session.getAttribute("student");
%>
<form class="hiddenForm" action="/ProfileStudentController" method="GET">
    <input type="hidden" name="idStudent" value="${idStudent}">
    <button class="returnButton" type="submit"><</button>
</form>


<h1>Relevé de notes</h1>

<%
    // Récupération des maps transmises par le servlet
    Map<com.projetjee.projetjeespringboot.models.Course, Double> coursesWithAverages = (Map<com.projetjee.projetjeespringboot.models.Course, Double>) request.getAttribute("coursesWithAverages");
    Map<Integer, Map<com.projetjee.projetjeespringboot.models.Assessment, Double>> assessmentsWithGradesByCourse = (Map<Integer, Map<com.projetjee.projetjeespringboot.models.Assessment, Double>>) request.getAttribute("assessmentsWithGradesByCourse");
%>

<%-- Vérifiez si des données sont présentes --%>
<%
    if (coursesWithAverages != null && !coursesWithAverages.isEmpty()) {
%>
<table>
    <%
        // Parcourir chaque cours
        for (Map.Entry<com.projetjee.projetjeespringboot.models.Course, Double> courseEntry : coursesWithAverages.entrySet()) {
            com.projetjee.projetjeespringboot.models.Course course = courseEntry.getKey();
            Double courseAverage = courseEntry.getValue();

            // Obtenir les évaluations et les notes pour ce cours
            Map<com.projetjee.projetjeespringboot.models.Assessment, Double> assessmentsWithGrades = assessmentsWithGradesByCourse.get(course.getIdCourse());

            // Vérifier si le cours a des évaluations
            boolean hasAssessments = (assessmentsWithGrades != null && !assessmentsWithGrades.isEmpty());
    %>
    <tr class="course-row">
        <td><%= course.getName() %></td>
        <td class="right-align"><%
            String displayAverage;
            if (hasAssessments) {
                displayAverage = (courseAverage != null && courseAverage >= 0) ? String.format("%.2f", courseAverage) : "Pas encore de notes";
            } else {
                displayAverage = "Pas encore de notes";
            }
        %>
            <%= displayAverage %>
        </td>
    </tr>

    <%
        // Afficher les évaluations uniquement si elles existent
        if (hasAssessments) {
            for (Map.Entry<com.projetjee.projetjeespringboot.models.Assessment, Double> assessmentEntry : assessmentsWithGrades.entrySet()) {
                com.projetjee.projetjeespringboot.models.Assessment assessment = assessmentEntry.getKey();
                Double grade = assessmentEntry.getValue();
    %>
    <tr class="assessment-row">
        <td>&nbsp;&nbsp;&nbsp; <%= assessment.getName() %></td>
        <td class="right-align"><%= grade != null ? String.format("%.2f", grade) : "Non notée" %></td>
    </tr>
    <%
            }
        }
    %>
    <%
        // Si aucune évaluation pour le cours, afficher un message dans le tableau
        if (!hasAssessments) {
    %>
    <tr>
        <td colspan="2" style="text-align: center;">Aucune évaluation pour ce cours.</td>
    </tr>
    <%
            }
        }
    %>
</table>
<%
} else {
%>
<p style="text-align: center; color: #4F2BEC;">Aucune donnée disponible pour cet étudiant.</p>
<%
    }
%>

</body>
</html>