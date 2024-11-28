<%@ page import="models.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Course" %>
<%@ page import="models.Assessment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajouter une évaluation</title>
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

        input{
            background-color: #AAC2FF;
            border: 2px solid #4F2BEC;
            border-radius: 16px;
            margin: 20px;
            padding: 5px 20px;
        }

        input:focus{
            outline:none;
            border:2px solid #4F2BEC;
        ;
        }

        input::placeholder {
            color: #000000;
            opacity: 70%;
        }

        .assessmentName{
            font-size: 24px;
            color: #2B3674;
            font-family: 'DM Sans', serif;
            font-weight: bold;
            font-style: normal;
            margin-top: 30px;
            margin-left: 150px;
        }

        .container{
            display: flex;
            flex-direction: column;
            align-items: center;
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
<% Course course = (Course) request.getAttribute("course"); %>
<form class="hiddenForm" action="${pageContext.request.contextPath}/CoursePageTeacherServlet" method="GET">
    <input type="hidden" name="idCourse" value="<%= course.getIdCourse()%>">
    <button class="returnButton" type="submit"><</button>
</form>
<h1>Ajouter une évaluation</h1>

<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) {
%>
<div style="color: red; text-align: center;">
    <p><strong><%= errorMessage %></strong></p>
</div>
<% } else { %>

<%
    List<Student> students = (List<Student>) request.getAttribute("students");

    if (students == null || students.isEmpty()) {
%>
<p style="text-align: center;">Aucun élève n'est inscrit dans cette matière.</p>
<%
} else {
%>
<form action="${pageContext.request.contextPath}/AddAssessmentTeacherServlet" method="POST">
    <label><span class="assessmentName">Nom</span> :
        <input type="text" name="nameAssessment" placeholder="Nom de l'évaluation" required>
    </label>
    <div class="container">
        <table>
            <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Note</th>
            </tr>
            </thead>
            <tbody>
            <% for (Student student : students) { %>
            <tr>
                <td><%= student.getSurname() %>
                </td>
                <td><%= student.getName() %>
                </td>
                <td style="text-align: center; padding: 10px;">
                    <input type="hidden" name="courseId" value="<%= course.getIdCourse() %>">
                    <input type="hidden" name="studentId" value="<%= student.getIdStudent() %>">
                    <input type="number" name="grade_<%= student.getIdStudent() %>" step="0.1" min="0" max="20"
                           placeholder="Note" required>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <button type="submit">Ajouter</button>
    </div>
</form>
<%
        }
    }
%>
</body>
</html>
