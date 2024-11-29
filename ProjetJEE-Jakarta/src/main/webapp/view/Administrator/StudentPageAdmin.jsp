<%@ page import="models.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Page étudiants</title>
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

      .actionButton {
          all: unset;
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

      /* Style du menu de filtre */
      #filterMenu {
          display: none;
          position: absolute;
          background-color: #f5f5f5;
          border: 2px solid #4F2BEC;
          padding: 10px;
          border-radius: 8px;
      }

      .filter-option {
          margin-bottom: 10px;
      }

      .filter-option input {
          margin-right: 10px;
      }

    </style>
  </head>
  <body>
  <a href="${pageContext.request.contextPath}/view/Administrator/HomeAdministrator.jsp"><</a>
  <h1>Page étudiants</h1>
  <div class="container">
      <form id="searchForm" action="${pageContext.request.contextPath}/StudentPageAdminServlet" method="GET">
          <div class="searchBar">
              <span>🔍︎</span>
              <input type="text" name="search" placeholder="Recherche" oninput="performSearch(this.value)" value="${param.search}">
          </div>
      </form>
      <div class="right">
          <button onclick="viewFilerMenu()" id="filterButton">Filtre</button>
          <a href="${pageContext.request.contextPath}/view/Administrator/AddStudentAdmin.jsp" class="button">Ajouter Étudiant</a>
      </div>
  </div>

  <!-- Menu de filtre avec des checkboxes -->
  <div id="filterMenu" style="display:none;">
      <div class="filter-option">
          <label><input type="checkbox" name="promo" value="2025" onclick="applyFilter()"> Promo 2025</label>
      </div>
      <div class="filter-option">
          <label><input type="checkbox" name="promo" value="2026" onclick="applyFilter()"> Promo 2026</label>
      </div>
      <div class="filter-option">
          <label><input type="checkbox" name="promo" value="2027" onclick="applyFilter()"> Promo 2027</label>
      </div>
  </div>

  <%
      java.util.List<models.Student> students = (java.util.List<models.Student>) request.getAttribute("students");
  %>
  <table id="studentsTable">
      <thead>
      <tr>
          <th>Nom</th>
          <th>Prénom</th>
          <th>Date de naissance</th>
          <th>Contact</th>
          <th>Promo</th>
          <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <% if (students != null && !students.isEmpty()) { %>
      <% for (models.Student student : students) { %>
      <tr class="studentRow" data-promo="<%= student.getSchoolYear() %>">
          <td style="cursor: pointer;"><%= student.getSurname() %></td>
          <td style="cursor: pointer;"><%= student.getName() %></td>
          <td style="cursor: pointer;"><%= student.getDateBirth() %></td>
          <td style="cursor: pointer;"><%= student.getContact() %></td>
          <td style="cursor: pointer;"><%= student.getSchoolYear() %></td>
          <td onclick="event.stopPropagation()" class="action">
              <button class="actionButton" onclick="editStudent(<%= student.getIdStudent() %>)">
                  <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M9.16671 4.66664H5.00004C4.55801 4.66664 4.13409 4.84223 3.82153 5.15479C3.50897 5.46736 3.33337 5.89128 3.33337 6.33331V15.5C3.33337 15.942 3.50897 16.3659 3.82153 16.6785C4.13409 16.991 4.55801 17.1666 5.00004 17.1666H14.1667C14.6087 17.1666 15.0327 16.991 15.3452 16.6785C15.6578 16.3659 15.8334 15.942 15.8334 15.5V11.3333M14.655 3.48831C14.8088 3.32912 14.9927 3.20215 15.196 3.1148C15.3994 3.02746 15.6181 2.98148 15.8394 2.97956C16.0607 2.97763 16.2801 3.0198 16.485 3.1036C16.6898 3.1874 16.8759 3.31116 17.0324 3.46765C17.1889 3.62414 17.3126 3.81022 17.3964 4.01505C17.4802 4.21988 17.5224 4.43934 17.5205 4.66064C17.5185 4.88194 17.4726 5.10064 17.3852 5.30398C17.2979 5.50732 17.1709 5.69123 17.0117 5.84497L9.85671 13H7.50004V10.6433L14.655 3.48831Z" stroke="#2E65F3" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/> </svg>

              </button>
              |
              <button class="actionButton" onclick="deleteStudent(<%= student.getIdStudent() %>)">
                  <svg width="20" height="21" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path d="M8.33334 9.16667V14.1667M11.6667 9.16667V14.1667M3.33334 5.83333H16.6667M15.8333 5.83333L15.1108 15.9517C15.0809 16.3722 14.8928 16.7657 14.5843 17.053C14.2758 17.3403 13.8699 17.5 13.4483 17.5H6.55168C6.13013 17.5 5.72423 17.3403 5.41575 17.053C5.10726 16.7657 4.91911 16.3722 4.88918 15.9517L4.16668 5.83333H15.8333ZM12.5 5.83333V3.33333C12.5 3.11232 12.4122 2.90036 12.2559 2.74408C12.0997 2.5878 11.8877 2.5 11.6667 2.5H8.33334C8.11233 2.5 7.90037 2.5878 7.74409 2.74408C7.58781 2.90036 7.50001 3.11232 7.50001 3.33333V5.83333H12.5Z" stroke="#F32D2D" stroke-width="1.4" stroke-linecap="round" stroke-linejoin="round"/></svg>
              </button>
          </td>
      </tr>
      <% } %>
      <% } else { %>
      <tr>
          <td colspan="6" style="text-align: center;">Aucun étudiant trouvé.</td>
      </tr>
      <% } %>
      </tbody>
  </table>
  <script>

      function viewFilerMenu() {
          const filterMenu = document.getElementById('filterMenu');

          // Si le menu est déjà ouvert, il doit rester visible après rechargement
          if (filterMenu.style.display === 'none' && document.querySelectorAll('#filterMenu input[type="checkbox"]:checked').length > 0) {
              filterMenu.style.display = 'block';
          } else if (filterMenu.style.display === 'none') {
              filterMenu.style.display = 'block';
          } else {
              filterMenu.style.display = 'none';
          }
      }

      // Appliquer le filtre en fonction des promotions sélectionnées
      function applyFilter() {
          // Récupérer les cases à cocher sélectionnées
          const selectedPromos = [];
          const checkboxes = document.querySelectorAll('#filterMenu input[type="checkbox"]:checked');

          checkboxes.forEach(checkbox => {
              selectedPromos.push(checkbox.value);
          });

          // Construire la query string avec les promotions sélectionnées
          let query = '';
          if (selectedPromos.length > 0) {
              query = '?promo=' + selectedPromos.join(',');
          }

          // Rediriger vers la page avec les promotions filtrées
          window.location.href = window.location.pathname + query;
      }

      // Fonction pour maintenir l'état des checkboxes sélectionnées
      window.onload = function() {
          const promoParam = new URLSearchParams(window.location.search).get('promo');

          if (promoParam) {
              const selectedPromos = promoParam.split(',');
              const checkboxes = document.querySelectorAll('#filterMenu input[type="checkbox"]');

              checkboxes.forEach(checkbox => {
                  if (selectedPromos.includes(checkbox.value)) {
                      checkbox.checked = true;
                  }
              });
          }
      }


      // Fonction pour rediriger vers la page de profil
          function viewProfile(idStudent) {
              if (idStudent) {
                  // Créez un formulaire HTML de manière dynamique
                  const form = document.createElement("form");
                  form.method = "POST"; // Utiliser POST au lieu de GET
                  form.action = `${pageContext.request.contextPath}/StudentProfileAdminServlet`;

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

          // Fonction pour modifier un étudiant
          function editStudent(idStudent) {
              if (idStudent) {
                  // Créez un formulaire HTML de manière dynamique
                  const form = document.createElement("form");
                  form.method = "GET";
                  form.action = `${pageContext.request.contextPath}/UpdateStudentAdminServlet`;

                  // Ajoutez un champ caché contenant l'ID de l'enseignant
                  const input = document.createElement("input");
                  input.type = "hidden";
                  input.name = "idStudent";
                  input.value = idStudent;
                  form.appendChild(input);

                  // Ajoutez le formulaire à la page et soumettez-le
                  document.body.appendChild(form);
                  form.submit();
              } else {
                  console.error("Aucun ID étudiant n'a été transmis.");
              }
          }

          // Fonction pour supprimer un étudiant
          function deleteStudent(idStudent) {
              if (idStudent) {
                  // Créez un formulaire HTML de manière dynamique
                  const form = document.createElement("form");
                  form.method = "POST";
                  form.action = `${pageContext.request.contextPath}/DeleteStudentAdminServlet`;

                  // Ajoutez un champ caché contenant l'ID de l'enseignant
                  const input = document.createElement("input");
                  input.type = "hidden";
                  input.name = "idStudent";
                  input.value = idStudent;
                  form.appendChild(input);

                  // Ajoutez le formulaire à la page et soumettez-le
                  document.body.appendChild(form);
                  form.submit();
              } else {
                  console.error("Aucun ID étudiant n'a été transmis.");
              }
          }

          function performSearch(searchTerm) {
              const form = document.getElementById("searchForm");
              const formData = new FormData(form);

              // Utilisation de fetch pour envoyer une requête au serveur
              fetch(form.action + '?' + new URLSearchParams(formData), {
                  method: 'GET'
              })
                  .then(response => response.text())
                  .then(html => {
                      // Remplacement du contenu de la table
                      const parser = new DOMParser();
                      const doc = parser.parseFromString(html, "text/html");
                      const newTable = doc.querySelector("#studentsTable");
                      const oldTable = document.querySelector("#studentsTable");

                      if (newTable && oldTable) {
                          oldTable.innerHTML = newTable.innerHTML;
                      }
                  })
                  .catch(error => console.error('Error:', error));
          }
      </script>
  </body>
</html>

