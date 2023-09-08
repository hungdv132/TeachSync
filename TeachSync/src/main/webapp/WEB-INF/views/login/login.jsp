<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE HTML>
<html lang="vi">
<html>
<head>
  <title>Login</title>
  
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta charset="utf-8">
  
  <link rel="stylesheet" type="text/css" href="../../../resources/css/login_style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <link href='https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
  <link href='https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
  
  <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.1/css/all.css"
      integrity="sha384-vp86vTRFVJgpjF9jiIGPEEqYqlDwgyBgEF109VFjmqGmIY/Y4HV4d3Gp2irVfcrp" crossorigin="anonymous">
  
  <script>
      function signIn() {
          $("#message").text("");
          
          let loginDTO = {
              "username": $("#username").val(),
              "password": $("#password").val()
          }

          $.post({
              type: "POST",
              url: "/sign-in",
              data: JSON.stringify(loginDTO),
              contentType: "application/json",
              success: function(response) {
                  if (response['view'] != null) {
                      location.href = response['view'];
                  }

                  $("#message").text(response['msg']);
              }
          });
          
          return false;
      }
  </script>
</head>

<body class="body">


<div class="login-page">
  <div class="form">

    <form onsubmit="return signIn()">
      <lottie-player
          src="https://assets4.lottiefiles.com/datafiles/XRVoUu3IX4sGWtiC3MPpFnJvZNq7lVWDCa8LSqgS/profile.json"
          background="transparent" speed="1" style="justify-content: center;" loop autoplay></lottie-player>
      <input type="text" required name="username" id="username" placeholder="&#xf007;  username"/>
      <input type="password" required name="password" id="password" placeholder="&#xf023;  password"/>
      <i class="fas fa-eye" onclick="show()"></i>
      <br>
  
      <p id="message"></p>
      
      <button type="submit">Đăng nhập</button>
    </form>

    <form class="login-form" action="/sign-up" method="get">
      <button type="submit">Đăng ký</button>
    </form>
    <a href="forgot_password"> Quên mật khẩu</a>
  </div>
</div>

<script>
  function show() {
    var password = document.getElementById("password");
    var icon = document.querySelector(".fas")

    // ========== Checking type of password ===========
    if (password.type === "password") {
      password.type = "text";
    } else {
      password.type = "password";
    }
  };
</script>
</body>
</html>

