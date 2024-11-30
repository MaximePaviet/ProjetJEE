<%@ page import="com.projetjee.projetjeespringboot.models.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="com.projetjee.projetjeespringboot.models.Assessment" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page cours</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Monofett&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&family=Monofett&display=swap" rel="stylesheet">

    <style>
        body {
            background-color: #f5f5f5;
        }

        .hiddenForm{
            position: relative;
            visibility: hidden;
        }
        .returnButton {
            all: unset;
            color: #4F2BEC;
            font-family: "DM Sans", sans-serif;
            font-size: 50px;
            font-weight: bold;
            margin: 0 20px;
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
            font-style: normal;
            text-align: center;
            margin: 0;
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
<form class="hiddenForm" action="${pageContext.request.contextPath}/ProfileTeacherController" method="GET">
    <button class="returnButton" type="submit"><</button>
</form>
<%
    Course course = (Course) request.getAttribute("course");
    List<Assessment> assessments = (List<Assessment>) request.getAttribute("assessments");
    // Récupérer les informations des pires et meilleures notes
    Map<Integer, Map<String, Float>> minMaxGrades = (Map<Integer, Map<String, Float>>) request.getAttribute("minMaxGrades");

%>
<h1><%= course.getName() %></h1>
<div class="container">
    <h2>Liste des évaluations :</h2>
    <div class="right">
        <button onclick="addAssessment(<%= course.getIdCourse() %>)">Ajouter évaluation</button>
    </div>
</div>

<%
    if (assessments == null || assessments.isEmpty()) {
%>
<p style="text-align: center;">Aucune évaluation pour cette matière.</p>
<%
} else {
%>
<table>
    <thead>
    <tr>
        <th>Évaluation</th>
        <th>Meilleure note</th>
        <th>Pire note</th>
        <th>Moyennne</th>
    </tr>
    </thead>
    <tbody>
    <% for ( Assessment assessment : assessments) {
        Map<String, Float> gradesData = minMaxGrades.get(assessment.getIdAssessment());
        Float bestGrade = gradesData != null ? gradesData.get("max") : null;
        Float worstGrade = gradesData != null ? gradesData.get("min") : null;
    %>
    <tr>
        <td><%= assessment.getName() %></td>
        <td><%= bestGrade != null ? bestGrade : "Pas de notes" %></td>
        <td><%= worstGrade != null ? worstGrade : "Pas de notes" %></td>
        <td><%=assessment.getAverage()%></td>
    </tr>
    <% } %>
    </tbody>
</table>
<%
    }
%>
<script>
    // Fonction pour rediriger vers la page de profil
    function addAssessment(idCourse) {
        if (idCourse) {
            // Créez un formulaire HTML de manière dynamique
            const form = document.createElement("form");
            form.method = "GET";
            form.action = `${pageContext.request.contextPath}/AddAssessmentTeacherController`;

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