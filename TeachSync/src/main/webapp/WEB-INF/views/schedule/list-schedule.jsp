<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <fmt:setLocale value="vi_VN" scope="session"/>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Lịch Học</title>
  
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
<div class="row">
  <!-- Breadcrumb -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal px-5 mb-3">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
        <li class="breadcrumb-item">
          <a href="/index">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          QL Lịch Học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  

  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <h5></h5>
    
    <table class="table table-striped">
      <thead class="table-primary">
      <tr>
        <th scope="col">ID</th>
        <th scope="col">Tên lớp</th>
        <th scope="col">Phòng học</th>
        <th scope="col">Tiết</th>
        <th scope="col">Lịch học</th>
        <th scope="col">Thời gian bắt đầu</th>
        <th scope="col">Thời gian kết thúc</th>
      
        <c:if test="${isAdmin}">
          <th scope="col">Chức năng</th>
        </c:if>
      </tr>
      </thead>
    
      <tbody class="align-middle">
      <c:forEach var="request" items="${clazzList}">
        <tr>
          <th scope="row">${request.id}</th>
          <c:set var="clazzSchedule" value="${request.clazzSchedule}"/>
        
          <c:if test="${not empty clazzSchedule}">
            <td>${request.clazzName}</td>
            <td>${clazzSchedule.roomName}</td>
            <td>${clazzSchedule.slot}</td>
            <td>${clazzSchedule.scheduleCategory.categoryDesc}</td>
            <fmt:parseDate value="${clazzSchedule.startDate}" type="date"
                           pattern="yyyy-MM-dd" var="parsedStartDate" />
            <fmt:parseDate value="${clazzSchedule.endDate}" type="date"
                           pattern="yyyy-MM-dd" var="parsedEndDate" />
            <td><fmt:formatDate value="${parsedStartDate}" type="date" pattern="dd/MM/yyyy"/></td>
            <td><fmt:formatDate value="${parsedEndDate}" type="date" pattern="dd/MM/yyyy"/></td>
          
            <c:if test="${isAdmin}">
              <c:if test="${localDateNow.isAfter(clazzSchedule.endDate)}">
                <td>
                  <a href="#">
                    <button type="button" class="btn btn-warning w-100" disabled="disabled">Sửa</button>
                  </a>
                </td>
              </c:if>
              <c:if test="${not localDateNow.isAfter(clazzSchedule.endDate)}">
                <td>
                  <a href="/edit-schedule?id=${request.id}">
                    <button type="button" class="btn btn-warning w-100">Sửa</button>
                  </a>
                </td>
              </c:if>
              
            </c:if>
          </c:if>
        
          <c:if test="${empty clazzSchedule}">
            <td>${request.clazzName}</td>
            <td>Thiếu</td>
            <td>Thiếu</td>
            <td>Thiếu</td>
            <td>Thiếu</td>
            <td>Thiếu</td>
          
            <c:if test="${isAdmin}">
              <td>
                <a href="/add-schedule?id=${request.id}">
                  <button type="button" class="btn btn-success w-100">Thêm</button>
                </a>
              </td>
            </c:if>
          </c:if>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    var mess = <c:out value="${mess}"/>
    if (mess != '') {
        alert(mess);
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>