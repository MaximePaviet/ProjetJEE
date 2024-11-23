<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Connexion Administrateur</title>
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
            font-size: 64px;
            color: #2549A4;
            font-family: 'Monofett', serif;
            font-weight: 400;
            font-style: normal;
            text-align: center;
            margin-top: 30px;
        }

        h2 {
            font-size: 36px;
            color: #4F2BEC;
            font-family:'DM Sans', serif;
            text-align: center;
        }

        hr {
            width: 426px;
            border-top: 1px solid #E0E5F2;
            border-left: none;
            border-bottom: none;
            border-right: none;
        }

        form {
            font-family:'DM Sans', serif;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 50%;
        }
        
        label{
            font-size: 14px;
            font-weight: 500;
            color: #2B3674;
        }

        .asterisque{
            font-size: 14px;
            font-weight: 500;
            color: #4318FF
        }

        input{
            color: #A3AED0;
            background-color: #f5f5f5;
            padding: 10px 40px 10px 10px;
            margin: 10px 0 10px 0;
            border: 2px solid #4F2BEC;
            border-radius: 16px;
            font-size: 14px;
            font-weight: normal;
            width: 450px;
            height: 50px;
        }

        input:focus {
            outline: none;
            border: 2px solid #4F2BEC;
        }

        input::placeholder {
            color: #A3AED0;
        }

         .error-message {
             color: red;
             font-weight: bold;
             position: absolute;
             top: 305px;
             left: 50%;
             transform: translateX(-50%);
             z-index: 10;
         }


    .toggle-password {
            position: absolute;
            top: 65.65%;
            right: 36%;
            cursor: pointer;
            color: #A3AED0;
            font-size: 1.8rem;
            user-select: none;
        }

        .toggle-password:hover {
            color: #4F2BEC;
        }

        button{
            color: white;
            background-color: #4F2BEC;
            border: none;
            border-radius: 20px;
            margin-top: 20px;
            font-family: "DM Sans", sans-serif;
            font-size: 1rem;
            font-weight: normal;
            cursor: pointer;
            text-decoration: none;
            padding: 10px 15px;
            width: 250px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/view/index.jsp"><</a>
    <h1>CYSCOLARITÉ</h1>
    <h2>Espace Administrateur</h2>
    <hr>
    <form action="${pageContext.request.contextPath}/adminLogin" method="POST">
        <!-- Afficher le message d'erreur si l'attribut errorMessage existe -->
        <div class="error-message">
            ${errorMessage}
        </div>

        <label>Login<span class="asterisque">*</span><br>
            <input type="text" name="login" placeholder="jdoe" required>
        </label> <br>
        <label>Password*<br>
            <div class="password-container">
                <input type="password" name="password" placeholder="Min. 8 caractères" required>
                <span class="toggle-password" onclick="togglePassword()">👁</span>
            </div>
        </label> <br>
        <button type="submit">Connexion</button>

    </form>

    <script>
        function togglePassword() {
            const passwordField = document.querySelector('.password-container input');
            if (passwordField.type === 'password') {
                passwordField.type = 'text'; // Affiche le texte
            } else {
                passwordField.type = 'password'; // Masque le texte
            }
        }
    </script>
</body>
</html>
