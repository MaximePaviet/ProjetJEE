<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajouter évaluation</title>
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

        input{
            background-color: #AAC2FF;
            border: 2px solid #4F2BEC;
            border-radius: 16px;
            margin: 20px;
            padding: 5px 20px;
        }

        input:focus{
            outline:none;
            border:none;
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
<a href="view/Administrator/CoursePageTeacher.jsp"><</a>
<h1>Ajouter évaluation</h1>
<form  action="" method="POST">
    <label><span class="assessmentName">Nom</span> :<input type="text" name="nameCourse" placeholder="Nom de l'évaluation" required></label>
    <div class="container">
        <table id="studentsTable">
            <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Note</th>
            </tr>
            </thead>
            <tbody>
            <!-- Les lignes de données sont ajoutées dynamiquement ici -->
            </tbody>
        </table>
        <button type="submit">Ajouter</button>
    </div>
</form>


</body>
</html>
