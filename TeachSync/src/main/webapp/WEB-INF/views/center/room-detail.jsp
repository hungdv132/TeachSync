<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Room detail</title>

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../../resources/css/certificate_style.css">
  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  <script src="../../../resources/js/common.js"></script>
  <style>
    .detail-container {
      display: flex;
      align-items: flex-start;
      padding: 20px;
    }

    #centerImage {
      width: 550px;
      height: auto;
      object-fit: cover;
      border-radius: 10px;
      margin-right: 20px;
    }

    .info-container {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      margin-left: 40px;
    }

    h2 {
      font-size: 24px;
      margin-top: 0;
    }

    p {
      font-size: 18px;
      margin-top: 10px;
      margin-bottom: 0;
    }
  </style>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>

<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<!-- ================================================== Breadcrumb ================================================= -->
<div class="row ts-bg-white border ts-border-teal rounded-3 mx-2 mb-3">
  <div class="col">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
        <li class="breadcrumb-item">
          <a href="/index">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
        <li class="breadcrumb-item" aria-current="page">
          <a href="/center">
            Phòng học
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          TeachSync Doi Can
        </li>
      </ol>
    </nav>
  </div>
</div>


<!-- ================================================== Breadcrumb ================================================= -->


<div class="detail-container">
  <img src="https://baochauelec.com/cdn/images/phong-hoc-1.jpg" id="centerImage">
  <div class="info-container">
    <h2 id="centerName">Phòng 101</h2>
    <p id="functionInfo">Loại phòng: CLASSROOM</p>
    <p id="centerAddress">Sức chứa: 30 người</p>


  </div>
</div>

<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>
