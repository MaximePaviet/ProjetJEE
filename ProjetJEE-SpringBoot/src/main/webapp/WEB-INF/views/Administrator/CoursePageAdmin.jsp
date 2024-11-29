<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Page cours</title>
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
<a href="${pageContext.request.contextPath}/views/Administrator/HomeAdministrator"><</a>
<h1>Page cours</h1>
<c:if test="${empty courses}">
  <p style="text-align: center;">Aucun cours trouvé</p>
</c:if>
<c:if test="${not empty courses}">
  <div class="container">
    <div class="searchBar">
      <span>🔍︎</span>
      <input type="text" name="search" placeholder="Recherche">
    </div>
    <div class="right">
      <form action="${pageContext.request.contextPath}/AddCourseAdminController" method="POST">
        <input class="courseName" type="text" name="courseName" placeholder="Nom du cours" required>
        <button type="submit">Ajouter cours</button>
      </form>
    </div>
  </div>
  <table id="studentsTable">
    <thead>
    <tr>
      <th>Cours</th>
      <th>Professeur</th>
      <th>Nombre d'élèves</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="course" items="${courses}">
      <tr>
        <td>${course.name}</td>
        <td>${course.teacher != null ? course.teacher.name + " " + course.teacher.surname : "Aucun professeur"}</td>
        <td>${course.studentList.size()}</td>
        <td class="action">
          <button class="actionButton" onclick="editCourse(${course.idCourse})">
            <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
              <!-- Votre SVG ici -->
            </svg>
          </button>
          |
          <button class="actionButton" onclick="deleteCourse(${course.idCourse})">
            <svg width="20" height="21" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
              <!-- Votre SVG ici -->
            </svg>
          </button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</c:if>

<script>
  function editCourse(idCourse) {
    if (idCourse) {
      const form = document.createElement("form");
      form.method = "GET";
      form.action = "${pageContext.request.contextPath}/UpdateCourseAdminController";
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "idCourse";
      input.value = idCourse;
      form.appendChild(input);
      document.body.appendChild(form);
      form.submit();
    } else {
      console.error("Aucun ID cours n'a été transmis.");
    }
  }

  function deleteCourse(idCourse) {
    if (idCourse) {
      const form = document.createElement("form");
      form.method = "POST";
      form.action = "${pageContext.request.contextPath}/DeleteCourseAdminController";
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "idCourse";
      input.value = idCourse;
      form.appendChild(input);
      document.body.appendChild(form);
      form.submit();
    } else {
      console.error("Aucun ID cours n'a été transmis.");
    }
  }
</script>
</body>
</html>
