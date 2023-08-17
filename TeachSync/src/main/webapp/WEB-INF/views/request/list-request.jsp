<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Danh sách đơn xin</title>
  
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
        <li class="breadcrumb-item active" aria-current="page">
          Danh sách Đơn xin
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 px-5 pt-3 mx-2 mb-3">
  
  <div class="col-12 d-flex justify-content-between align-items-center mb-3">
    <h5 class="mb-0">Danh sách đơn xin</h5>
    
    <c:if test="${isStudent}">
      <a href="/add-request" class="btn btn-success">Tạo mới</a>
    </c:if>
  </div>
  
  <table class="table table-striped">
    <thead class="table-primary">
      <tr>
        <th scope="col">ID</th>
        <c:if test="${isAdmin}">
          <th scope="col">Xin bởi</th>
        </c:if>
        <th scope="col">Lớp</th>
        <th scope="col">Khóa</th>
        <th scope="col">Kỳ</th>
        <th scope="col">Cơ sở</th>
        <th scope="col">Trạng thái</th>
        
        <th scope="col">Chức năng</th>
      </tr>
    </thead>
    
    <tbody>
    <c:forEach var="request" items="${requestList}">
      <tr>
        <c:url var="requestDetail" value="/request-detail">
          <c:param name="id" value="${request.id}"/>
        </c:url>
        <th scope="row">
          <a href="${requestDetail}">${request.id}</a>
        </th>
        <c:if test="${isAdmin}">
          <td>${request.requesterFullName}</td>
        </c:if>
        <td><a href="${requestDetail}">${request.clazz.clazzName}</a></td>
        <td><a href="${requestDetail}">${request.clazz.courseSemester.courseAlias}</a></td>
        <td><a href="${requestDetail}">${request.clazz.courseSemester.semesterAlias}</a></td>
        <td><a href="${requestDetail}">${request.clazz.courseSemester.centerName}</a></td>
        <td><a href="${requestDetail}">${request.status.stringValueVie}</a></td>
        <td>
          <a href="/edit-request?id=${request.id}" class="btn btn-warning">Sửa</a>
          <c:if test="${isStudent}">
            <a href="/delete-request?id=${request.id}" class="btn btn-danger ms-2">Xóa</a>
          </c:if>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  
  <c:if test="${empty requestList}">
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
  </c:if>

</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }
</script>
</html>