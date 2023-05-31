<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <html>
    <head>
      <title>Sign Up</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <meta charset="utf-8" />
      <link rel="stylesheet" type="text/css" href="../../resources/css/signup_style.css" />
      <link
        rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
      />
      <link
        href="https://fonts.googleapis.com/css?family=Titillium+Web:400,300,600"
        rel="stylesheet"
        type="text/css"
      />
      <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
      <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.1/css/all.css" integrity="sha384-vp86vTRFVJgpjF9jiIGPEEqYqlDwgyBgEF109VFjmqGmIY/Y4HV4d3Gp2irVfcrp" crossorigin="anonymous">
    </head>

    <body class="body">
      <a href="https://github.com/Mehedi61/Login-form-Sign-up-form"
        ><img
          style="position: absolute; top: 0; left: 0; border: 0"
          src="https://camo.githubusercontent.com/c6625ac1f3ee0a12250227cf83ce904423abf351/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f6c6566745f677261795f3664366436642e706e67"
          alt="Fork me on GitHub"
          data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_left_gray_6d6d6d.png"
      /></a>

      <div class="login-page">
<%--        TODO: signup--%>
        <div class="form">
          <form class="login-form" action="signup" method="post">
            <lottie-player
              src="https://assets4.lottiefiles.com/datafiles/XRVoUu3IX4sGWtiC3MPpFnJvZNq7lVWDCa8LSqgS/profile.json"
              background="transparent"
              speed="1"
              style="justify-content: center"
              loop
              autoplay
            ></lottie-player>
            <input type="text" name="fullName" placeholder="full name" />
            <input type="email" name="email" placeholder="email address" />
            <input type="text" name="username" placeholder="pick a username" />
            <input type="password" name="password" id="password" placeholder="set a password" />
            <i class="fas fa-eye" onclick="show()"></i>
            <br>
            <br>

            <button type="submit">
              SIGN UP
            </button>
            <c:if test="${errorIllegalMsg ne null}">
              <p><c:out value="${errorIllegalMsg}"/></p>
            </c:if>
            <c:if test="${errorMsg ne null}">
              <p><c:out value="${errorMsg}"/></p>
            </c:if>
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
