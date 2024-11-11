<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="fr">
<head>
    <title>CY Scolarité</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Monofett&display=swap" rel="stylesheet">
    <style>
        body {
            display: flex;
            justify-content: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
        }
        .container {
            text-align: center;
        }
        h1 {
            font-size: 64px;
            color: #2549A4;
            font-family: 'Monofett', serif;
            font-weight: 400;
            font-style: normal;
        }
        .button {
            display: block;
            width: 200px;
            margin: 20px auto;
            padding: 15px;
            color: white;
            background-color: #4F2BEC;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            cursor: pointer;
            text-decoration: none;
        }
        .button:hover {
            opacity: 90%;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>CYSCOLARITÉ</h1>
    <a href="login?role=teacher" class="button">Enseignants</a>
    <a href="login?role=student" class="button">Étudiants</a>
    <a href="login?role=admin" class="button">Administrateur</a>
</div>
</body>
</html>