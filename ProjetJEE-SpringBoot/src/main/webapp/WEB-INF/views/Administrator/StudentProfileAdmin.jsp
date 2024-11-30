<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>

<html>
<head>
    <title>Profil étudiant</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Monofett&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&family=Monofett&display=swap" rel="stylesheet">

    <style>
        body {
            background-color: #f5f5f5;
        }

        a {
            color: #4F2BEC;
            font-family: "DM Sans", sans-serif;
            font-size: 50px;
            font-weight: bold;
            margin: 20px;
            cursor: pointer;
            text-decoration: none;
        }

        h1 {
            font-size: 36px;
            color: #4F2BEC;
            font-family: 'DM Sans', serif;
            font-weight: bold;
            font-style: normal;
            text-align: center;
            margin: 0;
        }

        .profileInfo{
            color: #2B3674;
            border: 2px solid #3965FF;
            border-radius:16px;
            font-family: 'DM Sans', serif;
            width: 300px;
            padding: 10px;
            margin-left:150px;
            margin-top:30px;
        }

        h2{
            font-size: 24px;
            color: #4F2BEC;
            font-family: 'DM Sans', serif;
            font-weight: bold;
            font-style: normal;
            margin-top: 30px;
            margin-left: 150px;
        }

        /* Style du tableau */
        table {
            color: #4F2BEC;
            width: 80%;
            border-collapse: collapse;
            margin: 20px auto;
            font-family: "DM Sans", sans-serif;
        }

        table, th, td {
            border: 2px solid #AAC2FF;
        }

        td {
            padding: 12px;
            text-align: left;
        }

        th {
            padding: 12px;
            text-align: center;
        }

    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/StudentPageAdminController"><</a>
<h1>Profil étudiant</h1>

<%
    com.projetjee.projetjeespringboot.models.Student student = (com.projetjee.projetjeespringboot.models.Student) request.getAttribute("student");
    if (student != null) {
%>
<div class="profileInfo">
    <p><strong>Nom :</strong> <span id="studentName"> <%= student.getSurname() %></span></p>
    <p><strong>Prénom :</strong> <span id="studentFirstName"> <%= student.getName() %></span></p>
    <p><strong>Date de naissance :</strong> <span id="studentBirthDate"> <%= student.getDateBirth() %></span></p>
    <p><strong>Contact :</strong> <span id="studentContact"> <%= student.getContact() %></span></p>
</div>
<%
} else {
%>
<p>Aucun étudiant trouvé.</p>
<%
    }
%>
<h2>Liste des cours :</h2>
<%
    // Récupération des moyennes par cours
    Map<com.projetjee.projetjeespringboot.models.Course, Double> coursesWithAverages = (Map<com.projetjee.projetjeespringboot.models.Course, Double>) request.getAttribute("coursesWithAverages");
    if (coursesWithAverages == null || coursesWithAverages.isEmpty()) {
%>
<p style="text-align: center;">Aucun cours trouvé pour cet étudiant.</p>
<%
} else {
%>
<table id="studentsTable">
    <thead>
    <tr>
        <th>Cours</th>
        <th>Professeur</th>
        <th>Moyenne</th>
    </tr>
    </thead>
    <tbody>
    <% for (Map.Entry<com.projetjee.projetjeespringboot.models.Course, Double> entry : coursesWithAverages.entrySet()) {
        com.projetjee.projetjeespringboot.models.Course course = entry.getKey();
        Double average = entry.getValue();
        com.projetjee.projetjeespringboot.models.Teacher courseTeacher = course.getTeacher(); // Obtenez l'objet Teacher associé au cours
    %>
    <tr>
        <td><%= course.getName() %></td>
        <td><%= courseTeacher != null ? courseTeacher.getName() + " " + courseTeacher.getSurname() : "Aucun professeur" %></td>
        <td>
            <%
                // Vérification de la validité de la moyenne
                if (average == null || average < 0) {
            %>
            Pas encore de notes
            <%
            } else {
            %>
            <%= String.format("%.2f", average) %>
            <%
                }
            %>
        </td>
    </tr>
    <% } %>
    </tbody>
    <!-- Générer la case moyenne générale avec du JavaScript -->
</table>
<%
    }
%>
</body>
</html>