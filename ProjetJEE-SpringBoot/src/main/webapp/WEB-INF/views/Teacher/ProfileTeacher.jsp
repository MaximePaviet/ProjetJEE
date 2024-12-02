<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Course, java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Teacher" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
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

        button {
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

        button:hover {
            opacity: 90%;
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
<a href="${pageContext.request.contextPath}/views/Teacher/ConnexionTeacher"><</a>
<h1>Mon Profil</h1>
<%
    // Récupération des données du modèle
    Teacher teacher = (Teacher) request.getAttribute("teacher");
    List<Course> courses = teacher.getCourseList();
    //Map<Integer, Double> courseAverages = (Map<Integer, Double>) request.getAttribute("courseAverages");

    // Vérification si l'enseignant est absent
    if (teacher == null) {
%>
<p style="color: red;">Aucun enseignant trouvé.</p>
<a href="${pageContext.request.contextPath}/Teacher/Login">Retour à la connexion</a>
<%
        return;
    }
%>

<div class="profileInfo">
    <p><strong>Nom :</strong> <%= teacher.getSurname() %></p>
    <p><strong>Prénom :</strong> <%= teacher.getName() %></p>
    <p><strong>Contact :</strong> <%= teacher.getContact() %></p>
</div>
<div class="container">
    <h2>Liste des cours :</h2>
    <div class="right">

        <button onclick="assignmentCourse(<%= teacher.getIdTeacher() %>)" class="button" >S'inscrire à un cours</button>
    </div>
</div>
<%
    // Vérifiez si la liste des cours est vide ou nulle
    if (courses == null || courses.isEmpty()) {
%>
<p style="text-align: center;">Aucun cours trouvé pour cet enseignant.</p>
<%
} else {

%>
<table>
    <thead>
    <tr>
        <th>Nom du cours</th>
        <th>Nombre d'élèves</th>
        <th>Moyenne</th>
    </tr>
    </thead>
    <tbody>
    <%
        // Récupérer les moyennes des cours
        Map<Integer, String> courseAverages = (Map<Integer, String>) request.getAttribute("courseAverages");
        // Parcourir la liste des cours
        for (Course course : courses) {
    %>
    <tr onclick="viewCourse(<%= course.getIdCourse() %>, <%= teacher.getIdTeacher() %>)" style="cursor: pointer;">
        <td><%= course.getName() %></td>
        <td><%= course.getStudentList() != null ? course.getStudentList().size() : 0 %></td>
        <td><%= courseAverages != null ? courseAverages.get(course.getIdCourse()) : "Non disponible" %></td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
    }
%>

<script>
    // Fonction pour rediriger vers la page de profil
    function viewCourse(idCourse, idTeacher) {
        if (idCourse) {
            // Créez un formulaire HTML de manière dynamique
            const form = document.createElement("form");
            form.method = "POST";
            form.action = `${pageContext.request.contextPath}/CoursePageTeacherController?idTeacher=`+idTeacher;

            // Ajoutez un champ caché contenant l'ID de l'enseignant
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "idCourse";
            input.value = idCourse;
            form.appendChild(input);

            // Ajoutez le formulaire à la page et soumettez-le
            document.body.appendChild(form);
            form.submit();
        } else {
            console.error("Aucun ID cours n'a été transmis.");
        }
    }

    // Fonction pour que le prof s'inscrive aux cours
    function assignmentCourse(idTeacher) {
        if (idTeacher) {
            // Créez un formulaire HTML de manière dynamique
            const form = document.createElement("form");
            form.method = "GET";
            form.action = `${pageContext.request.contextPath}/AssignmentCourseTeacherController`;

            // Ajoutez un champ caché contenant l'ID de l'enseignant
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "idTeacher";
            input.value = idTeacher;
            form.appendChild(input);


            document.body.appendChild(form);
            form.submit();
        } else {
            console.error("Aucun ID étudiant n'a été transmis.");
        }
    }


</script>
</body>
</html>
