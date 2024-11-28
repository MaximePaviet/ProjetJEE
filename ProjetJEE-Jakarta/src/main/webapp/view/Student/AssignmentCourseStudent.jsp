<%@ page import="models.Course" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>S'inscrire à un cours</title>
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
            font-style: normal;
            text-align: center;
            margin: 0;
        }

        h2{
            font-size: 24px;
            color: #4F2BEC;
            font-family: 'DM Sans', serif;
            font-weight: bold;
            font-style: normal;
            margin-top: 30px;
            margin-left: 303px;
        }

        form:not(.hiddenForm) {
            display: flex;
            flex-direction: column;
            align-items: center;
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
            width: 60%;
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

<%
    models.Student student = (models.Student) session.getAttribute("student");
%>
<form class="hiddenForm" action="${pageContext.request.contextPath}/LoginStudentServlet" method="POST">
    <input type="hidden" name="login" value="<%= student.getLogin() %>">
    <input type="hidden" name="password" value="<%= student.getPassword() %>">
    <button class="returnButton" type="submit"><</button>
</form>

<h1>S'inscrire à un cours</h1>
<h2>Liste des cours existants :</h2>
<%
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    if (courses == null || courses.isEmpty()) {
%>
<p style="text-align: center">Vous êtes déjà inscrit à tous les cours.</p>
<%
} else {
%>
<form action=${pageContext.request.contextPath}/AssignmentCourseStudentServlet method="POST">
    <table>
        <thead>
        <tr>
            <th>Cours</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for (models.Course course : courses) { %>
        <tr>
            <td><%= course.getName() %></td>

            <td style="text-align: center; padding: 10px;">
                <input type="checkbox" name="courseSelection" value="<%= course.getIdCourse() %>" style="text-align: center; padding: 10px;">
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <button class="button" type="submit">S'inscrire</button>
</form>
<%
    }
%>



</body>
</html>

