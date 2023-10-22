<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Danh sách lớp học</title>

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

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
  <c:if test="${isAdmin}">
    <a href="/add-clazz">
      <button type="button" class="btn btn-primary">Thêm mới class</button>
    </a>
  </c:if>
  <table class="table">
    <thead class="thead-dark">
    <tr>
      <th scope="col">ID</th>
      <th scope="col">Tên lớp</th>
      <th scope="col">Khóa học</th>
      <c:if test="${isAdmin}">
        <th scope="col">Trạng thái</th>
        <th scope="col">Chức năng</th>
      </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="clazzDTO" items="${clazzList}">
      <tr>
        <th scope="row">${clazzDTO.id}</th>
        <td><a style="font-weight: bold;" href="/clazz-detail?id=${clazzDTO.id}">${clazzDTO.clazzName}</a></td>
        <td>${clazzDTO.courseAlias} - ${clazzDTO.courseName}</td>
        <c:if test="${isAdmin}">
          <td>${clazzDTO.status.stringValueVie}</td>
          <td>
            <a href="/edit-clazz?id=${clazzDTO.id}">
              <button type="button" class="btn btn-success">Sửa</button>
            </a>
<%--              <button type="button" class="btn btn-danger" onclick="confirmDelete(${clazzDTO.id})">Xóa</button>--%>
          </td>
        </c:if>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  
  <!-- Navigate page button -->
  <form action="/clazz" method="get"
        id="formNavTable" class="d-flex align-items-center mb-3">
    <input type="hidden" id="txtPageNo" name="pageNo" value="${pageNo}">
    
    <button type="button" class="btn btn-secondary" onclick="toPage(0)">
      <i class="bi-chevron-bar-left"></i>
    </button>
    <button type="button" class="btn btn-secondary mx-2" onclick="toPage(${pageNo - 1})">
      <i class="bi-chevron-left"></i>
    </button>
    
    Trang: <c:out value="${not empty pageNo ? pageNo + 1 : 1}"/> &sol; <c:out value="${not empty pageTotal ? pageTotal : 1}"/>
    
    <button type="button" class="btn btn-secondary mx-2" onclick="toPage(${pageNo + 1})">
      <i class="bi-chevron-right"></i>
    </button>
    <button type="button" class="btn btn-secondary" onclick="toPage(${pageTotal-1})">
      <i class="bi-chevron-bar-right"></i>
    </button>
  </form>
</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
<script>
  function confirmDelete(clazzId) {
    var result = confirm("Bạn có chắc muốn xóa lớp học này?");
    if (result) {
      // Nếu người dùng nhấn "Yes", chuyển họ đến trang xóa lớp học với thông tin clazzId
      window.location.href = "/delete-clazz?id=" + clazzId;
    }
    // Nếu người dùng nhấn "No", không thực hiện gì cả
  }
  
  function toPage(pageNo) {
      $("#txtPageNo").val(pageNo);
      const form = document.getElementById('formNavTable');
      form.requestSubmit();
  }
</script>
<script>
    var mess = `${mess}`
    if (mess != '') {
        alert(mess);
    }
</script>
</html>