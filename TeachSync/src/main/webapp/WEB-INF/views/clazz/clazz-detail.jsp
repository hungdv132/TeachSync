<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Chi tiết lớp học</title>

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
  <div class="col-12">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 class="mb-0">Tên lớp: ${clazz.clazzName}</h5>
  
      <c:if test="${isAdmin}">
        <c:url var="addClazz" value="/add-clazz">
          <c:param name="id" value="${clazz.id}"/>
          <c:param name="option" value="add"/>
        </c:url>
        <a href="${addClazz}" class="btn btn-warning">Sửa</a>
      </c:if>
    </div>
  
    <p>Cơ sở: ${clazz.courseSemester.center.centerName}</p>
    
    <p>Giáo viên: ${clazz.staff.user.fullName}</p>
    
    <p>Khóa học: ${clazz.courseSemester.courseAlias} - ${clazz.courseSemester.courseName}</p>
  
    <p>Học kỳ: ${clazz.courseSemester.semester.semesterAlias} - ${clazz.courseSemester.semester.semesterName}</p>
  
    <p>Miêu tả: ${clazz.clazzDesc}</p>
    
    <p>Dung lượng học sinh: ${clazz.clazzSize}</p>
  
    <%-- TODO: homeworkList, testList, schedule, sessionList --%>
  </div>
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