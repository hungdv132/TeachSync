<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Danh sách lớp học</title>

  <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

  <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../resources/js/bootstrap-5.3.0/bootstrap.js"></script>
  <script src="../../resources/js/common.js"></script>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
  <a href="add-classroom?option=add">
    <button type="button" class="btn btn-primary">Thêm mới class</button>
  </a>
  <table class="table">
    <thead class="thead-dark">
    <tr>
      <th scope="col">ID</th>
      <th scope="col">Tên lớp</th>
      <th scope="col">Tên khóa học</th>
      <th scope="col">Miêu tả</th>
      <th scope="col">Chức năng</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="classroom" items="${clazzList}">
      <tr>
        <th scope="row">${classroom.id}</th>
        <td><a style="font-weight: bold;"
             href="add-classroom?Id=${classroom.id}&option=detail">${classroom.clazzName}</a></td>
<%--    TODO: fix    <td>${classroom.course.courseName}</td>--%>
        <td>${classroom.clazzDesc}</td>
        <td>
          <a href="add-classroom?Id=${classroom.id}&option=edit">
            <button type="button" class="btn btn-success">Sửa</button>
          </a>
          <a href="delete-classroom?Id=${classroom.id}">
            <button type="button" class="btn btn-danger">Xóa</button>
          </a>
        </td>
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