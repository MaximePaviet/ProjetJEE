<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import=" java.util.Map" %>
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
            border-radius: 12px;
            overflow: hidden;
        }

        table tr {
            height: 40px;
        }

        td, th {
            border: 2px solid #AAC2FF;
            padding: 15px;
        }

        .course-row td {
            background-color: #AAC2FF;
        }

        .right-align {
            text-align: right;
        }

        .assessment-row td {
            background-color: #f9f9f9;
        }

    </style>
</head>
<body>
<%
    models.Student student = (models.Student) session.getAttribute("student");
%>
<form class="hiddenForm" action="${pageContext.request.contextPath}/ProfileStudentServlet" method="GET">
    <button class="returnButton" type="submit"><</button>
</form>
<h1>Relevé de notes</h1>

<%
    Map<models.Course, Double> coursesWithAverages = (Map<models.Course, Double>) request.getAttribute("coursesWithAverages");
    Map<Integer, Map<models.Assessment, Double>> assessmentsWithGradesByCourse = (Map<Integer, Map<models.Assessment, Double>>) request.getAttribute("assessmentsWithGradesByCourse");
%>

<%
    if (coursesWithAverages != null && !coursesWithAverages.isEmpty()) {
%>
<table>
    <%
        for (Map.Entry<models.Course, Double> courseEntry : coursesWithAverages.entrySet()) {
            models.Course course = courseEntry.getKey();
            Double courseAverage = courseEntry.getValue();

            Map<models.Assessment, Double> assessmentsWithGrades = assessmentsWithGradesByCourse.get(course.getIdCourse());

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
        if (hasAssessments) {
            for (Map.Entry<models.Assessment, Double> assessmentEntry : assessmentsWithGrades.entrySet()) {
                models.Assessment assessment = assessmentEntry.getKey();
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
