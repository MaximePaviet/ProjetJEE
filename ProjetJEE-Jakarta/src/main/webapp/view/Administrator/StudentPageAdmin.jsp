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

    </style>
  </head>
  <body>
      <a href="${pageContext.request.contextPath}/view/Administrator/HomeAdministrator.jsp"><</a>
      <h1>Page étudiants</h1>
      <div class="container">
          <div class="searchBar">
              <span>🔍︎</span>
              <input type="text" name="search" placeholder="Recherche">
          </div>
          <div class="right">
              <button>Filtre</button>
              <a href="${pageContext.request.contextPath}/view/Administrator/AddStudentAdmin.jsp" class="button">Ajouter Étudiant</a>
          </div>
      </div>
      <%
          java.util.List<models.Student> students = (java.util.List<Student>) request.getAttribute("students");
          if (students == null || students.isEmpty()) {
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
              <th>Date de naissance</th>
              <th>Contact</th>
              <th>Promo</th>
              <th>Action</th>
          </tr>
          </thead>
          <tbody>
          <% for (models.Student student : students) { %>
          <tr onclick="viewProfile(<%= student.getIdStudent() %>)" style="cursor: pointer;">
              <td><%= student.getSurname() %></td>
              <td><%= student.getName()%></td>
              <td><%= student.getDateBirth()%></td>
              <td><%= student.getContact() %></td>
              <td><%= student.getSchoolYear() %></td>

              <td>
                  <button onclick="event.stopPropagation(); editTeacher(<%= student.getIdStudent() %>)">
                      Modifier
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

          // Fonction pour modifier un enseignant
          function editStudent(idStudent) {
              window.location.href = `${pageContext.request.contextPath}${window.location.pathname.replace("TeacherPageAdmin.jsp", "EditTeacher.jsp")}?idTeacher=${idTeacher}`;
          }

          </script>
  </body>
</html>

