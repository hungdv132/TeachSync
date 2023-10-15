<%@ page import="com.teachsync.utils.enums.PromotionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <fmt:setLocale value="vi_VN" scope="session"/>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Chi tiết khóa học</title>
  
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

  <c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
  <c:set var="queryString" value="${requestScope['jakarta.servlet.forward.query_string']}"/>
  <c:set var="targetUrl" scope="session" value="${currentUri}${not empty queryString ? '?'.concat(queryString) : ''}"/>
  <!-- Breadcrumb -->

  
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <div class="row gy-3">
      <!-- Course Img -->
      <div class="col-sm-12 col-md-3 px-sm-3 pe-md-0 mb-3">
        <img src="${course.courseImg}" alt="courseImg"
             class="rounded-2 border ts-border-blue w-100 h-auto">
      </div>

      <!-- Course Detail -->
      <div class="col-sm-12 col-md-9 px-3 mb-3">
        <div class="card ts-border-yellow h-100">
          
          <!-- Course name -->
          <div class="card-header">
            <h3 class="card-title d-flex justify-content-between align-items-center">
              <span class="ts-txt-bold"><c:out value="${course.courseName}"/></span>
  
              <c:if test="${isAdmin}">
                <!-- Edit button -->
                <span>
                  <a href="/edit-course?id=${course.id}" class="btn btn-warning">
                    Chỉnh sửa
                  </a>
                </span>
              </c:if>
            </h3>
            <!-- Course Status -->
            <h6 class="card-subtitle">
              Trạng thái: <c:out value="${course.status.stringValueVie}"/>
            </h6>
          </div>

          <!-- Course detail -->
          <div class="card-body">
  
            <!-- Course price -->
            <h5 class="card-text">
              <c:set var="currentPrice" value="${course.currentPrice}"/>
              <c:set var="isPromotion" value="${currentPrice.isPromotion}"/>
              
              <c:if test="${!isPromotion}">
                <fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/>
              </c:if>
  
              
              <c:if test="${isPromotion}">
                <span class="ts-txt-orange ts-txt-bold">
                  <fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/>
                </span>
                <br/>
                
                <span class="ts-txt-grey ts-txt-light ts-txt-sm ts-txt-italic ts-txt-line-through">
                  <fmt:formatNumber value="${currentPrice.price}" type="currency"/>
                </span>
                
                <span class="ts-txt-orange ts-txt-sm">
                  &nbsp;Giảm&nbsp;
                  <c:choose>
                    <c:when test="${currentPrice.promotionType eq PromotionType.AMOUNT}">
                      <fmt:formatNumber value="${currentPrice.promotionAmount}" type="currency"/>
                    </c:when>
                    <c:when test="${currentPrice.promotionType eq PromotionType.PERCENT}">
                      <fmt:formatNumber value="${currentPrice.promotionAmount}" type="number"/>%
                    </c:when>
                  </c:choose>
                </span>
              </c:if>
            </h5>

            <!-- Course desc -->
            <p class="card-text">
              <c:out value="${course.courseDesc}"/>
            </p>
          </div>

          <!-- Course Message / Enroll button / Add clazz button -->
          <div class="card-footer text-center">
            
            
            <%-- Có lớp --%>
            <c:if test="${hasClazz}">
              <c:if test="${isGuest}">
                <a href="/sign-in" class="btn btn-primary w-25">Đăng ký học</a>
              </c:if>
              <c:if test="${isStudent}">
                <!-- TODO: Ai học qua rồi hoặc đang học thì xóa nút -->
                <a href="/enroll?id=${course.id}" class="btn btn-primary w-25">Đăng ký học</a>
              
              
              </c:if>
              <c:if test="${isAdmin}">
                <a href="/add-clazz" class="btn btn-primary w-25">Thêm lớp</a>
              </c:if>
            </c:if>
          
            <%-- Ko lớp --%>
            <c:if test="${!hasClazz}">
              <c:if test="${!isAdmin}">
                <p class="card-text text-danger">
                  Khóa học này hiện chưa có lớp nào đang mở để đăng ký, xin vui lòng quay lại sau
                </p>
              </c:if>
            
              <c:if test="${isAdmin}">
                <p class="card-text text-danger">
                  Khóa học này hiện chưa có lớp nào đang mở để đăng ký
                </p>
                <a href="/add-clazz" class="btn btn-primary w-25">Thêm lớp</a>
              </c:if>
            </c:if>
            
          </div>
      
        </div>
      </div>
    </div>
  </div>
  
  <!-- Dependency -->
<%--  <div class="col-12 mb-3">--%>
<%--    <!-- Course dependency tab -->--%>
<%--    <ul class="nav nav-tabs align-items-center" id="courseDependencyTab" role="tablist">--%>
<%--      <!-- Tab Certificate -->--%>
<%--      <li class="nav-item" role="presentation">--%>
<%--        <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue active"--%>
<%--                data-bs-toggle="tab" role="tab" aria-selected="true"--%>
<%--                id="certificate-tab" data-bs-target="#certificate-tab-pane" aria-controls="certificate-tab-pane">--%>
<%--          Bằng cấp--%>
<%--        </button>--%>
<%--      </li>--%>
<%--      --%>
<%--      <c:if test="${!isGuest}">--%>
<%--        <!-- Tab Material -->--%>
<%--        <li class="nav-item" role="presentation">--%>
<%--          <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"--%>
<%--                  data-bs-toggle="tab" role="tab" aria-selected="false"--%>
<%--                  id="material-tab" data-bs-target="#material-tab-pane" aria-controls="material-tab-pane">--%>
<%--            Tài liệu--%>
<%--          </button>--%>
<%--        </li>--%>
<%--      </c:if>--%>
<%--      --%>
<%--      <c:if test="${isTeacher || isAdmin}">--%>
<%--        <!-- Tab Test -->--%>
<%--        <li class="nav-item" role="presentation">--%>
<%--          <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"--%>
<%--                  data-bs-toggle="tab" role="tab" aria-selected="false"--%>
<%--                  id="test-tab" data-bs-target="#test-tab-pane" aria-controls="test-tab-pane">--%>
<%--            Bài kiểm tra--%>
<%--          </button>--%>
<%--        </li>--%>
<%--      </c:if>--%>
<%--    </ul>--%>
<%--    --%>
<%--    <!-- Course dependency tab content -->--%>
<%--    <div class="tab-content border border-top-0 rounded-bottom-3 pt-3 px-3" id="semesterTabContent">--%>
<%--      <!-- Tab Certificate TabPane -->--%>
<%--      <div class="tab-pane fade active show" role="tabpanel"--%>
<%--           id="certificate-tab-pane" aria-labelledby="certificate-tab">--%>
<%--        <p>Bằng cấp 1: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--      </div>--%>
<%--      --%>
<%--      <!-- Tab Material TabPane -->--%>
<%--      <div class="tab-pane fade" role="tabpanel"--%>
<%--           id="material-tab-pane" aria-labelledby="material-tab">--%>
<%--        <p>E-Book: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--        --%>
<%--        <p>NYT article: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--      </div>--%>
<%--      --%>
<%--      <!-- Tab Test TabPane -->--%>
<%--      <div class="tab-pane fade" role="tabpanel"--%>
<%--           id="test-tab-pane" aria-labelledby="test-tab">--%>
<%--        <p>Bài 1: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--        --%>
<%--        <p>Bài 2: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--        --%>
<%--        <p>Bài 3: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--        --%>
<%--        <p>Bài 4: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>--%>
<%--      </div>--%>
<%--    </div>--%>
<%--  </div>--%>

</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    var mess = `<c:out value="${mess}"/>`;
    if (mess != '') {
        alert(mess);
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>