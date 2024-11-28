<%@ page import="com.projetjee.projetjeespringboot.models.Teacher" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Student" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Course" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    Student student = (Student) request.getAttribute("student");
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
    java.util.List<Course> courses = (java.util.List<Course>) request.getAttribute("courses");
    if (courses == null || courses.isEmpty()) {
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
    <% for (Course course : courses) {
        Teacher courseTeacher = course.getTeacher(); // Obtenez l'objet Teacher associé au cours
    %>
    <tr>
        <td><%= course.getName() %></td>
        <td><%= courseTeacher != null ? courseTeacher.getName() + " " + courseTeacher.getSurname() : "Aucun professeur" %></td> <!-- Assurez-vous que `getStudentList()` retourne une liste -->
        <td> Chargement ...</td> <!-- Si vous avez une méthode pour calculer la moyenne -->
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