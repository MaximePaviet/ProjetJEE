<%@ page import="com.projetjee.projetjeespringboot.models.Teacher" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Page enseignants</title>
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

    .container{
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 30px;
    }

    .searchBar{
      background-color: #AAC2FF;
      border: 2px solid #4F2BEC;
      border-radius: 16px;
      margin: 20px 150px;
      padding: 5px 20px;
      width: 200px;
    }

    input{
      color: #000000;
      background-color: #AAC2FF;
      border: none;
      font-size: 14px;
      font-weight: normal;
    }

    input:focus{
      outline:none;
      border:none;
    }

    input::placeholder {
      color: #000000;
    }

    .right{
      display: flex;
      justify-content: flex-end;
      margin-right: 132px;
    }

    button {
      display: inline-flex;
      justify-content: center; /* Centre horizontalement le contenu du bouton */
      align-items: center; /* Centre verticalement le contenu du bouton */
      background-color: transparent;
      border: none;
      padding: 0; /* Supprime les marges internes */
      cursor: pointer;
      width: auto; /* Taille ajustée au contenu, ici le SVG */
      height: auto; /* Taille ajustée au contenu */
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

    .button:hover {
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

    .action{
      text-align: center;
      vertical-align: middle;
    }

  </style>
<body>
<a href="${pageContext.request.contextPath}/views/Administrator/HomeAdministrator"><</a>
<h1>Page enseignants</h1>
<div class="container">
  <div class="searchBar">
    <span>🔍︎</span>
    <input oninput="teachersearch(this.value)" ontype="text" name="search" placeholder="Recherche">
  </div>
  <div class="right">
    <a href="${pageContext.request.contextPath}/views/Administrator/AddTeacherAdmin" class="button">Ajouter Enseignant</a>
  </div>
</div>
<%
  java.util.List<com.projetjee.projetjeespringboot.models.Teacher> teachers = (java.util.List<Teacher>) request.getAttribute("teachers");
  if (teachers == null || teachers.isEmpty()) {
%>
<p>Aucun enseignant trouvé.</p>
<%
} else {
%>
<table id="studentsTable">
  <thead>
  <tr>
    <th>Nom</th>
    <th>Prénom</th>
    <th>Contact</th>
    <th>Action</th>
  </tr>
  </thead>
  <tbody>
  <% for (com.projetjee.projetjeespringboot.models.Teacher teacher : teachers) { %>
  <tr onclick="viewProfile(<%= teacher.getIdTeacher() %>)">
    <td style="cursor: pointer;"><%= teacher.getSurname() %></td>
    <td style="cursor: pointer;"><%= teacher.getName()%></td>
    <td style="cursor: pointer;"><%= teacher.getContact() %></td>
    <td  onclick="event.stopPropagation()" class="action">
      <button onclick="editTeacher(<%= teacher.getIdTeacher() %>)">
        <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M9.16671 4.66664H5.00004C4.55801 4.66664 4.13409 4.84223 3.82153 5.15479C3.50897 5.46736 3.33337 5.89128 3.33337 6.33331V15.5C3.33337 15.942 3.50897 16.3659 3.82153 16.6785C4.13409 16.991 4.55801 17.1666 5.00004 17.1666H14.1667C14.6087 17.1666 15.0327 16.991 15.3452 16.6785C15.6578 16.3659 15.8334 15.942 15.8334 15.5V11.3333M14.655 3.48831C14.8088 3.32912 14.9927 3.20215 15.196 3.1148C15.3994 3.02746 15.6181 2.98148 15.8394 2.97956C16.0607 2.97763 16.2801 3.0198 16.485 3.1036C16.6898 3.1874 16.8759 3.31116 17.0324 3.46765C17.1889 3.62414 17.3126 3.81022 17.3964 4.01505C17.4802 4.21988 17.5224 4.43934 17.5205 4.66064C17.5185 4.88194 17.4726 5.10064 17.3852 5.30398C17.2979 5.50732 17.1709 5.69123 17.0117 5.84497L9.85671 13H7.50004V10.6433L14.655 3.48831Z" stroke="#2E65F3" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>
<%
  }
%>

<script>
  // Fonction pour rediriger vers la page de profil
  function viewProfile(idTeacher) {
    if (idTeacher) {
      // Créez un formulaire HTML de manière dynamique
      const form = document.createElement("form");
      form.method = "POST"; // Utiliser POST au lieu de GET
      form.action = `${pageContext.request.contextPath}/TeacherProfileAdminController`;

      // Ajoutez un champ caché contenant l'ID de l'enseignant
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "idTeacher"; // Le nom doit correspondre à ce que le servlet attend
      input.value = idTeacher;
      form.appendChild(input);

      // Ajoutez le formulaire à la page et soumettez-le
      document.body.appendChild(form);
      form.submit();
    } else {
      console.error("Aucun ID enseignant n'a été transmis.");
    }
  }

  // Fonction pour modifier un enseignant
  function editTeacher(idTeacher) {
    if (idTeacher) {
      // Créez un formulaire HTML de manière dynamique
      const form = document.createElement("form");
      form.method = "GET";
      form.action = `${pageContext.request.contextPath}/UpdateTeacherAdminController`;

      // Ajoutez un champ caché contenant l'ID de l'enseignant
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "idTeacher"; // Le nom doit correspondre à ce que le servlet attend
      input.value = idTeacher;
      form.appendChild(input);

      // Ajoutez le formulaire à la page et soumettez-le
      document.body.appendChild(form);
      form.submit();
    } else {
      console.error("Aucun ID enseignant n'a été transmis.");
    }
  }




</script>
</body>
</html>