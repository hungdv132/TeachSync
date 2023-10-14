<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Home</title>

  <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">

  <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

  <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

  <script src="../../resources/js/common.js"></script>
</head>
<body class="min-vh-100 container-fluid d-flex flex-column ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->


<!-- ================================================== Main Body ================================================== -->
<div class="row">
  <!-- Breadcrumb -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal px-5 mb-3">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
        <li class="breadcrumb-item">
          <a href="#">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
      </ol>
    </nav>
  </div>
  
  <c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
  <c:set var="queryString" value="${requestScope['jakarta.servlet.forward.query_string']}"/>
  <c:set var="targetUrl" scope="session" value="${currentUri}${not empty queryString ? '?'.concat(queryString) : ''}"/>
  <!-- Breadcrumb -->


  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <c:if test="${isGuest}">
      <%@ include file="/WEB-INF/fragments/guest/guest-home.jspf" %>
    </c:if>

    <c:if test="${isStudent}">
      <%@ include file="/WEB-INF/fragments/student/student-home.jspf" %>
    </c:if>

    <c:if test="${isParent}">
      <%@ include file="/WEB-INF/fragments/parent/parent-home.jspf" %>
    </c:if>

    <c:if test="${isTeacher}">
      <%@ include file="/WEB-INF/fragments/teacher/teacher-home.jspf" %>
    </c:if>

    <c:if test="${isAdmin}">
      <%@ include file="/WEB-INF/fragments/admin/admin-home.jspf" %>
    </c:if>

    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    var mess = `<c:out value="${mess}"/>`;
    if (mess !== '') {
        alert(mess);
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>