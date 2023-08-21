<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<html>
<head>
  <title>Sign Up</title>
  
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta http-equiv="content-type" content="text/html; charset=utf-8">
  
  <link rel="stylesheet" type="text/css" href="../../../resources/css/signup_style.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"/>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600"/>
  
  <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.1/css/all.css"
        integrity="sha384-vp86vTRFVJgpjF9jiIGPEEqYqlDwgyBgEF109VFjmqGmIY/Y4HV4d3Gp2irVfcrp" crossorigin="anonymous">

  <script>
      function signUp() {
          let createDTO = {
              "username" : $("#username").val(),
              "password" : $("#password").val(),
              "fullName" : $("#fullName").val(),
              "email" : $("#email").val()
          }
        let username = $("#username").val();
        let password = $("#password").val();
        let fullName = $("#fullName").val();
        let email = $("#email").val();

        if (!username || !password || !email || !fullName) {
          $("#message").text("Vui lòng điền đầy đủ thông tin.");
          return;
        }

        if (!isValidEmail(email)) {
          $("#message").text("Email không hợp lệ.");
          return;
        }

        if (!isValidInput(username) || !isValidInput(password)) {
          $("#message").text("User or password tối thiểu 4 ký tự có thể là số hoặc chữ cái, viết liền, viết thường không dấu");
          return;
        }

          $.get({
              type: "POST",
              url: "/sign-up",
              data: JSON.stringify(createDTO),
              contentType: "application/json",
              success: function(response) {
                  if (response['view'] != null) {
                      location.href = response['view'];
                  }

                  $("#message").text(response['msg']);
              }
          });
      }

      function isValidInput(input) {
        let pattern = /^[a-z0-9_-]{4,45}$/;
        return pattern.test(input) && input === input.toLowerCase();
      }
      function isValidEmail(email) {
        // Kiểm tra email dựa trên định dạng abc@xyz
        let emailRegex = /^[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,4}$/;
        return emailRegex.test(email) && email === email.toLowerCase();
      }
  </script>
</head>

<body class="body">


<div class="login-page">
  <div class="form">
    <form class="login-form">
      <lottie-player
          src="https://assets4.lottiefiles.com/datafiles/XRVoUu3IX4sGWtiC3MPpFnJvZNq7lVWDCa8LSqgS/profile.json"
          background="transparent"
          speed="1"
          style="justify-content: center"
          loop
          autoplay
      ></lottie-player>
      <input type="text" required id="fullName" name="fullName" placeholder="full name"/>
      <input type="email" required id="email" name="email" placeholder="email address"/>
      <input required="true" id="username" type="text" placeholder="pick a username" name="username"
             class="form-control form--control" pattern="^[a-z0-9_-]{4,45}$"
             title="Tối thiểu 4 ký tự có thể là số hoặc chữ cái, viết liền, viết thường không dấu">
      
      <input type="password" required name="password" id="password" placeholder="set a password"
             pattern="^[a-z0-9]{4,45}$"
             title="Tối thiểu 4 ký tự có thể là số hoặc chữ cái, viết liền, viết thường không dấu"/>
      <i class="fas fa-eye" onclick="show()"></i>
      <br>
  
      <p id="message"></p>
      
      <button type="button" onclick="signUp()">
        Đăng ký
      </button>
    </form>
  </div>
</div>
</body>
<script>
    function show() {
        var password = document.getElementById("password");
        var icon = document.querySelector(".fas");

        // ========== Checking type of password ===========
        if (password.type === "password") {
            password.type = "text";
        } else {
            password.type = "password";
        }
    }
</script>
</html>
</html>
