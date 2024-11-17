<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="fr">
<head>
    <title>CY Scolarité</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Monofett&display=swap" rel="stylesheet">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:ital,opsz,wght@0,9..40,100..1000;1,9..40,100..1000&family=Monofett&display=swap" rel="stylesheet">

    <style>
        body {
            background-color: #f5f5f5;
        }

        h1 {
            font-size: 64px;
            color: #2549A4;
            font-family: 'Monofett', serif;
            font-weight: 400;
            font-style: normal;
            text-align: center;
            margin-top: 90px;
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
            padding: 10px 15px;
            width: 400px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .button:hover {
            opacity: 90%;
        }

        .container{
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            align-items: center;
            height: 50%;

        }
    </style>
</head>
<body>
    <h1>CYSCOLARITÉ</h1>
    <div class="container">
        <a href="loginTeacher" class="button">Enseignants</a>
        <a href="loginStudent" class="button">Étudiants</a>
        <a href="loginAdmin" class="button">Administrateur</a>
    </div>
</body>
</html>