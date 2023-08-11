<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Certificate</title>

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../../resources/css/certificate_style.css">
  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  <script src="../../../resources/js/common.js"></script>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="container">
  <p style="color:black;font-size:30px;font-weight: bold">Danh sách chứng chỉ</p>
  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Appreciation</div>
    </div>
  </a>

  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Service</div>
    </div>
  </a>

  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Completion</div>
    </div>
  </a>

  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Appreciation</div>
    </div>
  </a>

  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Service</div>
    </div>
  </a>
  <a href="certificate-detail">
    <div class="certificate">
      <img src="https://marketplace.canva.com/EAFIEvneNCM/1/0/1600w/canva-golden-elegant-certificate-of-appreciation-0bN-aLORS9U.jpg">
      <div class="cer-name">Certificate of Completion</div>
    </div>
  </a>
  <a href="add-certificate">
    <button type="button" class="btn btn-primary">Thêm chứng chỉ</button>
  </a>
</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>
