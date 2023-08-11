<%--
  Created by IntelliJ IDEA.
  User: Tha
  Date: 6/11/2023
  Time: 11:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Thêm bài báo mới</title>

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">

  <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

  <script src="../../../resources/js/common.js"></script>
  <style>
    /* Áp dụng CSS để tùy chỉnh giao diện form */
    .container {
      width: 600px;
      margin: 0 auto;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    label {
      display: block;
      font-weight: bold;
      margin-bottom: 5px;
    }

    input[type="text"],
    textarea {
      width: 100%;
      padding: 10px;
      border-radius: 3px;
      border: 1px solid #ccc;
    }

    textarea {
      height: 150px;
    }

    button {
      padding: 10px 20px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    button:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<div class="container">
  <h2>Thêm bài báo mới</h2>
  <form action="submitcreatenews" method="post">
    <div class="form-group">
      <label for="title">Tiêu đề:</label>
      <input type="text" id="title" name="title" required>
    </div>

    <div class="form-group">
      <label for="description">Tóm tắt:</label>
      <textarea id="description" name="description" required></textarea>
    </div>

    <div class="form-group">
      <label for="content">Nội dung:</label>
      <textarea id="content" name="content" required></textarea>
    </div>

    <button type="submit">Lưu</button>
  </form>
</div>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>


</body>
</html>
