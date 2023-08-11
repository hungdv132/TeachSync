<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Course Detail</title>
  
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
        <li class="breadcrumb-item active" aria-current="page">
          <c:out value="${course.courseName}"/>
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
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
  <div class="col-12 mb-3">
    <div class="row gy-3">
      <div class="col-sm-12 col-md-3 px-sm-3 pe-md-0">
        <img src="${course.courseImg}" class="rounded-2 border ts-border-blue w-100 h-auto">
      </div>
      
      <div class="col-sm-12 col-md-9 px-3">
        <div class="card ts-border-yellow h-100">
          
          <div class="card-header">
            <h4 class="card-title d-flex justify-content-between align-items-center mb-0">
              <span><c:out value="${course.courseName}"/></span>
              <c:if test="${isAdmin}">
                <span>
                  <a href="/edit-course?id=${course.id}" class="btn btn-warning">
                    Chỉnh sửa
                  </a>
                  <a href="/delete-course?id=${course.id}" class="btn btn-danger">
                    Xóa
                  </a>
                </span>
              </c:if>
            </h4>
          </div>
          
          <div class="card-body d-flex">
            <c:set var="currentPriceDTO" value="${course.currentPrice}"/>
            <c:set var="isPromotion" value="${currentPriceDTO.isPromotion}"/>
            <h5 class="card-subtitle">
              <c:if test="${!isPromotion}">
                <c:out value="${currentPriceDTO.price}"/> ₫
              </c:if>
              
              <c:if test="${isPromotion}">
                <span class="ts-txt-orange ts-txt-bold"><c:out value="${currentPriceDTO.finalPrice}"/>&nbsp;₫</span>
                <br/>
                <span class="ts-txt-grey ts-txt-light ts-txt-sm ts-txt-italic ts-txt-line-through">
                  &nbsp;<c:out value="${currentPriceDTO.price}"/>&nbsp;₫
                </span>
                <span class="ts-txt-orange ts-txt-sm">
                  &nbsp;-<c:out value="${currentPriceDTO.promotionAmount}"/>
                  <c:choose>
                    <c:when test="${currentPriceDTO.promotionType eq 'PERCENT'}">%</c:when>
                    <c:when test="${currentPriceDTO.promotionType eq 'AMOUNT'}">₫</c:when>
                  </c:choose>
                </span>
              </c:if>
            </h5>
            
            <p class="card-text">
              <c:out value="${course.courseDesc}"/>
            </p>
            
            <!-- Course schedule -->
          
          </div>
          
          
          <c:if test="${hasLatestSchedule}">
            <c:if test="${isGuest}">
              <div class="card-footer text-center">
                <a href="/sign-in" class="btn btn-primary w-25">Đăng ký học</a>
              </div>
            </c:if>
            
            <c:if test="${isStudent}">
              <div class="card-footer text-center">
                <c:url var="enrollLink" value="enroll">
                  <c:param name="id" value="${course.id}"/>
                </c:url>
                <a href="${enrollLink}" class="btn btn-primary w-25">Đăng ký học</a>
              </div>
            </c:if>
          </c:if>
          
          <c:if test="${!hasLatestSchedule}">
            <c:if test="${!isAdmin}">
              <div class="card-footer text-center">
                <p class="card-text text-danger">
                  Khóa học này hiện chưa có kỳ học nào sắp tới để đăng ký
                </p>
              </div>
            </c:if>
            
            <c:if test="${isAdmin}">
              <div class="card-footer text-center">
                <p class="card-text text-danger">
                  Khóa học này hiện chưa có kỳ học nào sắp tới để đăng ký
                </p>
                <a href="/add-clazz" class="btn btn-primary w-25">Thêm lớp</a>
              </div>
            </c:if>
          </c:if>
        
        </div>
      </div>
      
      <%--      <c:if test="${isAdmin}">--%>
      <%--        <div class="col-12 border-top ts-border-teal">--%>
      <%--          <h5 class="d-flex justify-content-between align-items-center">--%>
      <%--            <span>Lịch sử giá cả: </span>--%>
      <%--            --%>
      <%--          </h5>--%>
      <%--          --%>
      <%--          <c:forEach var="price" items="${}">--%>
      <%--            <div class=" ">--%>
      <%--              --%>
      <%--            </div>--%>
      <%--          </c:forEach>--%>
      <%--          --%>
      <%--        </div>--%>
      <%--      </c:if>--%>
    </div>
  </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>