<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 20/6/2023
  Time: 9:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title><c:out value="${title}"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta charset="utf-8">
  <link rel="stylesheet" type="text/css" href="../../../resources/css/login_style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <link href='https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
  <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.1/css/all.css"
      integrity="sha384-vp86vTRFVJgpjF9jiIGPEEqYqlDwgyBgEF109VFjmqGmIY/Y4HV4d3Gp2irVfcrp" crossorigin="anonymous">
  <style>
    .form button .home {
      font-family: "Titillium Web", sans-serif;
      font-size: 14px;
      font-weight: bold;
      letter-spacing: .1em;
      outline: 0;
      background: #17a589;
      width: 100%;
      border: 0;
      border-radius: 30px;
      margin: 0px 0px 8px;
      padding: 15px;
      color: #FFFFFF;
      -webkit-transition: all 0.3 ease;
      transition: all 0.3 ease;
      cursor: pointer;
      transition: all 0.2s;
      text-decoration: none;
    }

  </style>
</head>
<body class="body">
</div>
<div class="login-page">
  <div class="form">

    <h1>
      <c:out value="${message}"/>

    </h1>
    <button>
      <a href="/index" class="home">
        <i class="bi-house-door"></i>
        &nbsp;Trang chủ
      </a>
    </button>
  </div>
</div>
</body>
</html>
