<%@ page import="com.teachsync.utils.enums.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Danh sách bài kiểm tra</title>
  
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

<div class="page-header">
  <div class="row">
    <div class="col">
      <h3 class="page-title">Danh sách bài test</h3>
    </div>
    <div class="top-nav-search">
      <form id="login-form" name="myform" action="/searchbycourse" method="get">
<%--            onsubmit="return validateform()">--%>
        
        <input type="text" class="form-control" placeholder="Search here" name="searchText">
        <button class="btn" type="submit"><i class="fas fa-search"></i></button>
      </form>
    </div>
  </div>
</div>
<table>
  <tbody>
  
  <div class="row">
    <div class="col-sm-12">
      <div class="card">
        <div class="card-body">
          <div class="table-responsive">
            <table class="datatable table table-stripped">
              <thead>
              <tr>
                <th>ID</th>
                <th>Môn học</th>
                <th>Loại kiểm tra</th>
                <th>Thời gian</th>
                <th>Trạng Thái</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${tests.content}" var="test">
                <tr>
                  <td>
                      ${test.id}
                  </td>
                  <td>
                <span
                    class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md ">
                <a href="/edit-test?id=${tests.id}"> ${test.testName}</a>
                </span>
                  </td>
                  <td>
                    <span class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md ">
                      <c:if test="${test.testType == 'FIFTEEN_MINUTE'}">
                        <a> Giữa giờ</a>
                      </c:if>
                      <c:if test="${test.testType == 'MIDTERM'}">
                        <a> Giữa kỳ</a>
                      </c:if>
                      <c:if test="${test.testType == 'FINAL'}">
                        <a> Cuối kỳ</a>
                      </c:if>
                    </span>
                  </td>
                  <td>
                      ${test.timeLimit}
                  </td>
                  <td>
                    <span class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md ">
                      <c:if test="${test.status == Status.CREATED}">
                          <a> Đã tạo</a>
                      </c:if>
                      <c:if test="${test.status == Status.UPDATED}">
                          <a> Đã chỉnh sửa</a>
                      </c:if>
                      <c:if test="${test.status == Status.DELETED}">
                          <a> Đã xóa</a>
                      </c:if>
                    </span>
                  </td>
                </tr>
              </c:forEach>
              
              </tbody>
            </table>
          </div>
          
          <a href="/create-test" class="btn btn-outline-primary mr-2"><i class="fas fa-plus"></i>
            Tạo mới</a>
        </div>
      
      </div>
    </div>
  </div>
  
  </tbody>
</table>

<div class="d-flex align-items-center mb-3">
  <a href="/tests?page=0" class="btn btn-secondary"><i class="bi-chevron-bar-left"></i></a>
  <a href="/tests?page=${pageNo-1}" class="btn btn-secondary mx-2"><i class="bi-chevron-left"></i></a>
  Page: <c:out value="${pageNo + 1}"/> &sol; <c:out value="${pageTotal}"/>
  <a href="/tests?page=${pageNo + 1}" class="btn btn-secondary mx-2"><i class="bi-chevron-right"></i></a>
  <a href="/tests?page=${pageTotal-1}" class="btn btn-secondary"><i class="bi-chevron-bar-right"></i></a>
</div>
</body>



<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>