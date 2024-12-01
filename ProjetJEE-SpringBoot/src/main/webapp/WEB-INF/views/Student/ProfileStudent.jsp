<%@ page import="com.projetjee.projetjeespringboot.models.Course" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        .right{
            display: flex;
            justify-content: flex-end;
            margin-right: 132px;
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

        button {
            color: white;
            background-color: #4F2BEC;
            border: none;
            border-radius: 20px;
            font-family: "DM Sans", sans-serif;
            font-size: 1rem;
            font-weight: normal;
            cursor: pointer;
            text-decoration: none;
            margin: 20px;
            padding: 5px 20px;
        }

        button:hover{
            opacity: 90%;
        }

        .button {
            color: white;
            background-color: #4F2BEC;
            border: none;
            border-radius: 20px;
            font-family: "DM Sans", sans-serif;
            font-size: 1rem;
            font-weight: normal;
            cursor: pointer;
            text-decoration: none;
            margin: 20px;
            padding: 5px 20px;
        }

        .button:hover{
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
<a href="${pageContext.request.contextPath}/views/Student/ConnexionStudent"><</a>
<h1>Mon Profil</h1>

<%
    com.projetjee.projetjeespringboot.models.Student student = (com.projetjee.projetjeespringboot.models.Student) request.getAttribute("student");
    List<Course> courses = student.getCourseList();
%>
<div class="profileInfo">
    <p><strong>Nom :</strong> <span id="studentName"><%= student.getSurname() %></span></p>
    <p><strong>Prénom :</strong> <span id="studentFirstName"><%= student.getName() %></span></p>
    <p><strong>Date de naissance :</strong> <span id="studentBirthDate"><%= student.getDateBirth() %></span></p>
    <p><strong>Contact :</strong> <span id="studentContact"><%= student.getContact() %></span></p>
</div>
<div class="container">
    <h2>Liste des cours :</h2>
    <div class="right">
        <button onclick="transcriptStudent(this)" data-student-id="${student.idStudent}">Relevé de notes</button>
        <button onclick="assignmentCourse(<%= student.getIdStudent() %>)" class="button" >S'inscrire à un cours</button>
    </div>
</div>

<%
    if (courses == null || courses.isEmpty()) {
%>
<p style="text-align: center;">Aucun cours trouvé pour cet élève.</p>
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
    <%  Map<Integer, String> courseAverages = (Map<Integer, String>) request.getAttribute("courseAverages");
        for (com.projetjee.projetjeespringboot.models.Course course : courses) {
            com.projetjee.projetjeespringboot.models.Teacher courseTeacher = course.getTeacher(); // Obtenez l'objet Teacher associé au cours
    %>
    <tr>
        <td><%= course.getName() %></td>
        <td>
            <%= courseTeacher != null ? courseTeacher.getName() + " " + courseTeacher.getSurname() : "Aucun professeur" %>
        </td>
        <td><%= courseAverages.get(course.getIdCourse()) %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<%
    }
%>
<script>
    function transcriptStudent(button) {
        // Récupérer l'ID de l'étudiant depuis l'attribut data-student-id
        var idStudent = button.getAttribute('data-student-id');

        // Rediriger vers la page de relevé de notes
        window.location.href = "/TranscriptStudentController?idStudent=" + idStudent;
    }
    // Fonction pour que l'étudiant s'inscrive aux cours
    function assignmentCourse(idStudent) {
        if (idStudent) {
            // Créez un formulaire HTML de manière dynamique
            const form = document.createElement("form");
            form.method = "GET";
            form.action = `${pageContext.request.contextPath}/AssignmentCourseStudentController`;

            // Ajoutez un champ caché contenant l'ID de l'enseignant
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "idStudent"; // Le nom doit correspondre à ce que le servlet attend
            input.value = idStudent;
            form.appendChild(input);

            // Ajoutez le formulaire à la page et soumettez-le
            document.body.appendChild(form);
            form.submit();
        } else {
            console.error("Aucun ID étudiant n'a été transmis.");
        }
    }

    //Fonction pour afficher le relevé de note
    function transcritpStudent(){
        // Créez un formulaire HTML de manière dynamique
        const form = document.createElement("form");
        form.method = "GET";
        form.action = `${pageContext.request.contextPath}/TranscriptStudentController`;

        // Ajoutez le formulaire à la page et soumettez-le
        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>