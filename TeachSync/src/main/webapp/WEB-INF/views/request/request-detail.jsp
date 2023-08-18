<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Enroll detail</title>

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
        <li class="breadcrumb-item" aria-current="page">
          <a href="/course">
            Khóa học
          </a>
        </li>
        <li class="breadcrumb-item" aria-current="page">
          <c:url var="courseDetail" value="course-detail">
            <c:param name="id" value="${course.id}"/>
          </c:url>
          <a href="${courseDetail}">
            <c:out value="${course.courseName}"/>
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Đăng ký học
        </li>
      </ol>
    </nav>
  </div>
</div>

<c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
<c:set var="queryString" value="${requestScope['jakarta.servlet.forward.query_string']}"/>
<c:set var="targetUrl" scope="session" value="${currentUri}${not empty queryString ? '?'.concat(queryString) : ''}"/>
<!-- ================================================== Breadcrumb ================================================= -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 px-5 pt-3 mx-2 mb-3">
  <h5>Đơn: <c:out value="${request.requestName}"/></h5>
  
  <h6>Chi tiết: </h6>
  <p><c:out value="${request.requestDesc}"/></p>
  
  <h6>Học tại: </h6>
  <p>Cơ sở: <c:out value="${request.clazz.courseSemester.centerName}"/></p>
  <p>Phòng: <c:out value="${request.clazz.clazzSchedule.roomName}"/></p>
  
  <h6>Học kỳ </h6>
  <p><c:out value="${request.clazz.courseSemester.semester.semesterName}"/></p>
  
  <h6>Khóa học: </h6>
  <p><c:out value="${request.clazz.courseSemester.course.courseName}"/></p>
  
  <h6>Lớp: </h6>
  <p><c:out value="${request.clazz.clazzName}"/></p>
  
  
  
  
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>