<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modifier étudiant</title>
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
            margin: 20px auto;
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
<a href="${pageContext.request.contextPath}/StudentPageAdminServlet"><</a>
<h1>Modifier étudiant</h1>
<%
    models.Student student = (models.Student) request.getAttribute("student");
    if (student == null) {
%>
<p style="color: red; text-align: center;">Aucun étudiant trouvé. Veuillez réessayer.</p>
<%
    }
%>
<form action="${pageContext.request.contextPath}/UpdateStudentAdminServlet" method="POST">
    <div class="container">
        <input type="hidden" name="id" value="<%= student != null ? student.getIdStudent() : "" %>" />

        <label>Nom :</label>
        <input type="text" name="surname" value="<%= student != null ? student.getSurname() : "" %>" />

        <label>Prénom :</label>
        <input type="text" name="name" value="<%= student != null ? student.getName() : "" %>" />

        <label>Date de naissance :</label>
        <input type="text" name="birthDate" value="<%= student != null ? student.getDateBirth() : "" %>" />

        <label>Contact :</label>
        <input type="text" name="contact" value="<%= student != null ? student.getContact() : "" %>" />

        <label>Promo :</label>
        <input type="text" name="promoYear" value="<%= student != null ? student.getSchoolYear() : "" %>" />
    </div>
    <button type="submit">Mettre à jour</button>
</form>
<script>
    document.querySelector("form").addEventListener("submit", function(event) {
        // Récupération des champs
        const surnameInput = document.querySelector('input[name="surname"]');
        const nameInput = document.querySelector('input[name="name"]');
        const dateBirthInput = document.querySelector('input[name="birthDate"]');
        const contactInput = document.querySelector('input[name="contact"]');
        const promoYearInput = document.querySelector('input[name="promoYear"]');

        // Expressions régulières
        const nameRegex = /^[a-zA-Z\s-]+$/; // Lettres, espaces, tirets
        const dateRegex = /^\d{4}-\d{2}-\d{2}$/; // Format YYYY-MM-DD
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Format email
        const yearRegex = /^\d{4}$/; // Année (quatre chiffres)

        // Messages d'erreur
        let errorMessage = "";

        // Validation du nom
        if (!nameRegex.test(surnameInput.value)) {
            errorMessage += "Le nom ne doit contenir que des lettres, des espaces ou des tirets.\n";
        }

        // Validation du prénom
        if (!nameRegex.test(nameInput.value)) {
            errorMessage += "Le prénom ne doit contenir que des lettres, des espaces ou des tirets.\n";
        }

        // Validation de la date de naissance
        if (!dateRegex.test(dateBirthInput.value)) {
            errorMessage += "La date de naissance doit être au format YYYY-MM-DD.\n";
        } else {
            // Vérification logique de la date
            const dateParts = dateBirthInput.value.split("-");
            const year = parseInt(dateParts[0], 10);
            const month = parseInt(dateParts[1], 10);
            const day = parseInt(dateParts[2], 10);
            const date = new Date(year, month - 1, day);

            if (
                date.getFullYear() !== year ||
                date.getMonth() + 1 !== month ||
                date.getDate() !== day
            ) {
                errorMessage += "La date de naissance est invalide.\n";
            } else {
                // Vérification que la date est antérieure à aujourd'hui
                const today = new Date();
                if (date >= today) {
                    errorMessage += "La date de naissance doit être antérieure à la date du jour.\n";
                }
            }
        }

        // Validation du contact (email)
        if (!emailRegex.test(contactInput.value)) {
            errorMessage += "Le contact doit être une adresse email valide.\n";
        }

        // Validation de promoYear (année)
        if (!yearRegex.test(promoYearInput.value)) {
            errorMessage += "L'année de promotion doit être un nombre à quatre chiffres.\n";
        }

        // Si des erreurs sont trouvées, afficher un message et empêcher l'envoi
        if (errorMessage) {
            alert(errorMessage);
            event.preventDefault(); // Empêche l'envoi du formulaire
        }
    });
</script>
</body>
</html>