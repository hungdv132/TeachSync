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
        <li class="breadcrumb-item active" aria-current="page">
          Đơn xin
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 px-5 pt-3 mx-2 mb-3">
  <!-- Title & create button -->
  <div class="col-12 d-flex justify-content-between align-items-center mb-3">
    <h5 class="mb-0">Danh sách đơn xin</h5>
    
    <c:if test="${isStudent}">
      <a href="/add-request" class="btn btn-success">Tạo mới</a>
    </c:if>
  </div>
  
  <!-- Request list -->
  <div class="col-12 overflow-auto mb-3">
    <table class="table table-striped table-bordered table-hover mb-0">
      <thead class="table-primary ts-border-blue">
      <tr>
        <th scope="col" class="text-center">ID</th>
        
        <th scope="col">Đơn</th>
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
      
      <tbody class="table-hover ts-border-blue align-middle">
      <c:forEach var="request" items="${requestList}">
        <c:url var="requestDetail" value="/edit-request">
          <c:param name="id" value="${request.id}"/>
        </c:url>
        
        <tr>
          <th scope="row" class="text-center"><a href="${requestDetail}">${request.id}</a></th>
          
          <td><a href="${requestDetail}">${request.requestType.stringValueVie}</a></td>
          <c:if test="${isAdmin}">
            <td>${request.requesterFullName}</td>
          </c:if>
          <td>${request.clazz.clazzName}</td>
          <td>${request.clazz.courseSemester.courseAlias}</td>
          <td>${request.clazz.courseSemester.semesterAlias}</td>
          <td>${request.clazz.courseSemester.centerName}</td>
          <td>${request.status.stringValueVie}</td>
          
          <td class=text-center"">
            <a href="${requestDetail}" class="btn btn-warning">Sửa</a>
            <c:if test="${isStudent}">
              <a href="/delete-request?id=${request.id}" class="btn btn-danger ms-2">Xóa</a>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  
  <!-- Navigate page button -->
  <form action="/request" method="get"
        id="formNavTable" class="d-flex align-items-center mb-3">
    <input type="hidden" id="txtPageNo" name="pageNo" value="${pageNo}">
    
    <button type="button" class="btn btn-secondary" onclick="toPage(0)">
      <i class="bi-chevron-bar-left"></i>
    </button>
    <button type="button" class="btn btn-secondary mx-2" onclick="toPage(${pageNo - 1})">
      <i class="bi-chevron-left"></i>
    </button>
    
    Page: <c:out value="${not empty pageNo ? pageNo + 1 : 1}"/> &sol; <c:out value="${not empty pageTotal ? pageTotal : 1}"/>
    
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


<!-- ================================================== Script ===================================================== -->
<script id="script1">
    let pageTotal = ${empty pageTotal ? 1 : pageTotal};
    if (pageTotal === 1) {
        disableAllButtonIn("formNavTable");
    }
    // $("#script1").remove(); /* Xóa thẻ <script> sau khi xong */
</script>
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }

    function toPage(pageNo) {
        $("#txtPageNo").val(pageNo);
        const form = document.getElementById('formNavTable');
        form.requestSubmit();
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>