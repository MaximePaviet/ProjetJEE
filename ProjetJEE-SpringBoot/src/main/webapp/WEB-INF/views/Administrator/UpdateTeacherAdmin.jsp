
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modifier enseignant</title>
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

        form{
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .container {
            color: #2B3674;
            background-color: #AAC2FF;
            border: 6px solid #4318FF;
            border-radius: 30px;
            font-family: 'DM Sans', serif;
            font-size: 32px;
            font-weight: bold;
            width: 40%;
            height: 50%;
            padding: 30px;
            margin: 50px auto;
            display: flex;
            flex-direction: column;
            justify-content: space-around;;
        }

        label {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 100%;
            margin-bottom: 15px;
        }

        input{
            flex-grow: 1;
            background-color: #FFFFFF;
            border: 3px solid #4F2BEC;
            border-radius: 16px;
            padding: 5px 10px;
            margin-left: 20px;
        }

        input:focus{
            outline:none;
            border:3px solid #4F2BEC;;
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
            padding: 5px 20px;
        }

        button:hover {
            opacity: 90%;
        }

    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/TeacherPageAdminController"><</a>
<h1>Modifier enseignant</h1>
<%
    com.projetjee.projetjeespringboot.models.Teacher teacher = (com.projetjee.projetjeespringboot.models.Teacher) request.getAttribute("teacher");
    if (teacher == null) {
%>
<p style="color: red; text-align: center;">Aucun enseignant trouvé. Veuillez réessayer.</p>
<%
    }
%>
<form action="${pageContext.request.contextPath}/UpdateTeacherAdminController" method="POST">
    <div class="container">
        <input type="hidden" name="id" value="<%= teacher != null ? teacher.getIdTeacher() : "" %>" />

        <label>Nom :</label>
        <input type="text" name="surname" value="<%= teacher != null ? teacher.getSurname() : "" %>" />

        <label>Prénom :</label>
        <input type="text" name="name" value="<%= teacher != null ? teacher.getName() : "" %>" />

        <label>Contact :</label>
        <input type="text" name="contact" value="<%= teacher != null ? teacher.getContact() : "" %>" />
    </div>
    <button type="submit">Mettre à jour</button>
</form>


</body>
</html>
