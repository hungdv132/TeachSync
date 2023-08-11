<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
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
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
  <c:if test="${isAdmin}">
    <a href="add-clazz?option=add">
      <button type="button" class="btn btn-primary">Thêm mới class</button>
    </a>
  </c:if>
  <table class="table">
    <thead class="thead-dark">
    <tr>
      <th scope="col">ID</th>
      <th scope="col">Tên lớp</th>
      <th scope="col">Khóa học</th>
      <th scope="col">Học kỳ</th>
      <c:if test="${isAdmin}">
        <th scope="col">Chức năng</th>
      </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="clazz" items="${clazzList}">
      <tr>
        <th scope="row">${clazz.id}</th>
        <td><a style="font-weight: bold;" href="/clazz-detail?id=${clazz.id}">${clazz.clazzName}</a></td>
        <td>${clazz.courseSemester.courseAlias} - ${clazz.courseSemester.courseName}</td>
        <td>${clazz.courseSemester.semester.semesterAlias}</td>
        <c:if test="${isAdmin}">
          <td>
            <a href="/add-clazz?id=${clazz.id}&option=edit">
              <button type="button" class="btn btn-success">Sửa</button>
            </a>
            <a href="/delete-clazz?id=${clazz.id}">
              <button type="button" class="btn btn-danger">Xóa</button>
            </a>
          </td>
        </c:if>
      </tr>
    </c:forEach>
    </tbody>
  </table>

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