<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Profil enseignant</title>
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
<a href="${pageContext.request.contextPath}/TeacherPageAdminServlet"><</a>
<h1>Profil de l'enseignant</h1>

<%-- Afficher les informations sur l'enseignant --%>
<%
    models.Teacher teacher = (models.Teacher) request.getAttribute("teacher");
    if (teacher != null) {
%>
<div class="profileInfo">
    <p><strong>Nom :</strong> <%= teacher.getSurname() %></p>
    <p><strong>Prénom :</strong> <%= teacher.getName() %></p>
    <p><strong>Contact :</strong> <%= teacher.getContact() %></p>
</div>
<%
} else {
%>
<p>Aucun enseignant trouvé.</p>
<%
    }
%>

<h2>Liste des cours :</h2>
<%
    java.util.List<models.Course> courses = (java.util.List<models.Course>) request.getAttribute("courses");
    if (courses == null || courses.isEmpty()) {
%>
<p style="text-align: center;">Aucun cours trouvé pour cet enseignant.</p>
<%
} else {
%>
<table>
    <thead>
    <tr>
        <th>Cours</th>
        <th>Nombre d'élèves</th>
        <th>Moyenne</th>
    </tr>
    </thead>
    <tbody>
    <%  Map<Integer, String> courseAverages = (Map<Integer, String>) request.getAttribute("courseAverages");
        for (models.Course course : courses) { %>
    <tr>
        <td><%= course.getName() %></td>
        <td><%= course.getStudentList().size() %></td> <!-- Assurez-vous que `getStudentList()` retourne une liste -->
        <td><%= courseAverages.get(course.getIdCourse()) %></td> <!-- Si vous avez une méthode pour calculer la moyenne -->
    </tr>
    <% } %>
    </tbody>
</table>
<%
    }
%>
</body>
</html>

