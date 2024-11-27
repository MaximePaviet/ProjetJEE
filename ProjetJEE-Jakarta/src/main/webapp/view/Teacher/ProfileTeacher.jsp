<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Course, java.util.List" %>
<html>
<head>
    <title>Mon Profil</title>
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

        .container{
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 30px;
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

        .right{
            display: flex;
            justify-content: flex-end;
            margin-right: 132px;
        }

        .button {
            color: white;
            background-color: #4F2BEC;
            border: 2px solid #4F2BEC;
            border-radius: 20px;
            font-family: "DM Sans", sans-serif;
            font-size: 1rem;
            font-weight: normal;
            cursor: pointer;
            text-decoration: none;
            margin: 20px;
            padding: 5px 20px;
        }

        .button:hover {
            opacity: 90%;
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

<a href="${pageContext.request.contextPath}/view/Teacher/ConnexionTeacher.jsp"><</a>
<h1>Mon Profil</h1>

<%
    models.Teacher teacher = (models.Teacher) session.getAttribute("teacher");
    java.util.List<models.Course> courses = teacher.getCourseList();
%>


<div class="profileInfo">
    <p><strong>Nom :</strong> <span id="teacherName"><%= teacher.getSurname() %>.</span></p>
    <p><strong>Prénom :</strong> <span id="teacherFirstName"><%= teacher.getName() %></span></p>
    <p><strong>Contact :</strong> <span id="teacherContact"><%= teacher.getContact() %></span></p>
</div>

<div class="container">
    <h2>Liste des cours :</h2>
    <div class="right">
        <a class="button" href="${pageContext.request.contextPath}/teacherProfile">S'inscrire à un cours</a>

    </div>
</div>

<%
    if (courses == null || courses.isEmpty()) {
%>
<p style="text-align: center;">Aucun cours trouvé pour cet enseignant.</p>
<%
} else {
%>
<table id="coursesTable">
    <thead>
    <tr>
        <th>Nom du cours</th>
        <th>Nombre d'élèves</th>
        <th>Moyenne</th>
    </tr>
    </thead>
    <tbody>
    <% for (models.Course course : courses) { %>
    <tr onclick="viewProfile(<%= course.getIdCourse() %>)" style="cursor: pointer;">
        <td><%= course.getName() %></td>
        <th><%=course.getStudentList().size()%></th>
        <th>Chargement...</th>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<%
    }
%>
<script>
    // Fonction pour rediriger vers la page de profil
    function viewProfile(idCourse) {
        if (idTeacher) {
            // Créez un formulaire HTML de manière dynamique
            const form = document.createElement("form");
            form.method = "POST";
            form.action = `${pageContext.request.contextPath}/CoursePageServlet`;

            // Ajoutez un champ caché contenant l'ID de l'enseignant
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "idCourse"; // Le nom doit correspondre à ce que le servlet attend
            input.value = idCourse;
            form.appendChild(input);

            // Ajoutez le formulaire à la page et soumettez-le
            document.body.appendChild(form);
            form.submit();
        } else {
            console.error("Aucun ID cours n'a été transmis.");
        }
    }

</script>
</body>
</html>

