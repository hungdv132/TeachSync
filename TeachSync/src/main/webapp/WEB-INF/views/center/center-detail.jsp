<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Chi tiết Cơ Sở</title>
  
  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
  
  <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">
  
  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  
  <script src="../../../resources/js/common.js"></script>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->


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
        <li class="breadcrumb-item">
          <a href="/center">Danh sách Cơ Sở</a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Chi tiết Cơ sở
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
  <div class="col-12 d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">${center.centerName}</h4>
    
    <c:if test="${isAdmin}">
      <a href="/edit-center?id=${center.id}" class="btn btn-warning">Chỉnh sửa</a>
    </c:if>
  </div>
  
  <!-- Center Img -->
  <div class="col-sm-12 col-md-4 mb-3">
    <img src="${empty center.centerImg ? '../../../resources/img/no-img.jpg' : center.centerImg}" alt="courseImg"
         class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
  </div>

  <!-- Center detail -->
  <div class="col-sm-12 col-md-8 mb-3">
    <p>Địa chỉ: ${center.address.addressString}</p>
    <p>Chuyên môn: ${center.centerType.stringValueVie}</p>
    <p>Số phòng: ${center.centerSize} phòng</p>
    <p>Mô tả: <br>${center.centerDesc}</p>
  </div>

</div>

<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>
