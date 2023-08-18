<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Chi tiết đơn</title>

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

  <script src="../../../resources/js/common.js"></script>
</head>
<body class="min-vh-100 container-fluid d-flex flex-column ts-bg-white-subtle">
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
        <li class="breadcrumb-item" aria-current="page">
          <a href="/request">
            Đơn xin
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Chi tiết đơn
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 px-5 pt-3 mx-2 mb-3">
  <h5>Mã đơn: <c:out value="${request.id}"/></h5>
  <h6>Loại đơn: <c:out value="${request.requestType.stringValueVie}"/></h6>
  
  <h6>Chi tiết: </h6>
  <p><c:out value="${request.requestDesc}"/></p>
  
  <div class="col-sm-12 col-md-6 mb-3">
  
    <h6>Học tại: </h6>
    <p>Cơ sở: <c:out value="${request.clazz.courseSemester.center.centerName}"/></p>
    <p>Phòng: <c:out value="${request.clazz.clazzSchedule.roomName}"/></p>
  
    <h6>Học kỳ </h6>
    <p><c:out value="${request.clazz.courseSemester.semester.semesterName}"/></p>
  
    <h6>Khóa học: </h6>
    <p><c:out value="${request.clazz.courseSemester.course.courseName}"/></p>
  
    <h6>Lớp: </h6>
    <p><c:out value="${request.clazz.clazzName}"/></p>
  </div>
  
  <div class="col-sm-12 col-md-6 mb-3">
    
  </div>
  
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>