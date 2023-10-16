<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <fmt:setLocale value="vi_VN" scope="session"/>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Đăng ký học</title>

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
        <li class="breadcrumb-item" aria-current="page">
          <a href="/course-detail?id=${course.id}">
            <c:out value="${course.courseName}"/>
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Đăng ký học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->


  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <div class="row-cols-1">
  
      <!-- Course detail -->
      <h4>Khóa học: <c:out value="${course.courseAlias.concat(' - ').concat(course.courseName)}"/></h4>
      
      
      <h5>Cơ sở: </h5>

      <!-- Center Tab & TabPane -->
      <!-- Center Tab -->
      <ul id="centerTab"
          role="tablist"
          class="nav nav-tabs">
        <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter">
          <c:set var="centerIdStr" value="${centerIdCenterDTO.key.toString()}"/>

          <li role="presentation"
              class="nav-item" >
            <button id="${centerIdStr}-ce-tab"
                    class="nav-link ts-txt-grey ts-txt-hover-blue"
                    type="button" role="tab"
                    data-bs-toggle="tab" data-bs-target="#${centerIdStr}-ce-tab-pane"
                    aria-controls="${centerIdStr}-ce-tab-pane">
              <c:out value="${centerIdCenterDTO.value.centerName}"/>
            </button>
          </li>

          <c:if test="${counter.first}">
            <script id="script1">
                $("#${centerIdStr}-ce-tab").addClass("active").attr("aria-selected", "true");
                $("#script1").remove(); /* Xóa thẻ <script> sau khi xong */
            </script>
          </c:if>
          
        </c:forEach>
      </ul>
  
      <!-- Center TabPane List -->
      <div id="centerTabContent"
           class="tab-content border border-top-0 rounded-bottom-3 pt-3 mb-3">
        
        <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter1">
          <c:set var="centerIdStr" value="${centerIdCenterDTO.key.toString()}"/>

          <!-- Center ${centerIdStr} TabPane -->
          <div id="${centerIdStr}-ce-tab-pane"
               class="tab-pane fade mx-0"
               role="tabpanel"
               aria-labelledby="${centerIdStr}-ce-tab">

            <c:set var="clazzList" value="${centerIdClazzListMap.get(centerIdCenterDTO.key)}"/>
            
            <!-- Center ${centerIdStr} detail -->
            <div class="border-bottom px-3 mb-3">
              <p class="mb-1">Tên: <c:out value="${centerIdCenterDTO.value.centerName}"/></p>
              <p>Địa chỉ: <c:out value="${centerIdCenterDTO.value.address.addressString}"/></p>
            </div>

            <!-- clazzList -->
            <div class="row px-3">
              <!-- If clazzList != null => show result -->
              <c:if test="${not empty clazzList}">
                <c:forEach var="request" items="${clazzList}" >
    
                  <form action="/enroll?clazzId=${request.id}" method="post" class="col-sm-4 col-md-3 mb-3">
                    <div class="card">
                      
                      <div class="card-header">
                        <h6 class="card-subtitle">
                          Lớp: <c:out value="${request.clazzAlias.concat(' - ').concat(request.clazzName)}"/>
                        </h6>
                      </div>
  
                      <c:set var="memberCount" value="0"/>
                      <c:if test="${request.memberList ne null}">
                        <c:set var="memberCount" value="${request.memberList.size()}"/>
                      </c:if>
  
                      <div class="card-body">
                        <p class="card-text">
                          <c:set var="clazzSchedule" value="${request.clazzSchedule}"/>
                          
                          <fmt:parseDate value="${clazzSchedule.startDate}" type="date"
                                         pattern="yyyy-MM-dd" var="parsedStartDate" />
                          <fmt:parseDate value="${clazzSchedule.endDate}" type="date"
                                         pattern="yyyy-MM-dd" var="parsedEndDate" />
                          Bắt đầu: <fmt:formatDate value="${parsedStartDate}" type="date" pattern="dd/MM/yyyy"/><br/>
                          Kết thúc: <fmt:formatDate value="${parsedEndDate}" type="date" pattern="dd/MM/yyyy"/><br/>
                          <br/>
                          
                          Lịch học: <c:out value="${clazzSchedule.scheduleCategory.scheduleName}"/><br/>
                          
                          Tiết: <c:out value="${clazzSchedule.slot}"/><br/>
                          Từ: <c:out value="${clazzSchedule.sessionStart}"/>&nbsp;
                          Đến: <c:out value="${clazzSchedule.sessionEnd}"/><br/>
                          <br/>
      
                          Thành viên: <c:out value="${memberCount}"/> &sol; <c:out value="${request.maxCapacity}"/>
                        </p>
                      </div>
  
                      <div class="card-footer">
                        <c:choose>
                          <c:when test="${memberCount lt request.maxCapacity}">
                            <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
                          </c:when>
                          <c:when test="${memberCount ge request.maxCapacity}">
                            <button type="button" class="btn btn-primary w-100" disabled="disabled">Đăng ký
                            </button>
                            <span class="ts-txt-orange">Lớp học đã đầy</span>
                          </c:when>
                        </c:choose>
                      </div>
                      
                    </div>
                  </form>
  
                </c:forEach>
              </c:if>
  
              <!-- If clazzList == null -->
              <c:if test="${empty clazzList}">
                <div class="col-12 d-flex justify-content-center">
                  <h5 class="ts-txt-orange">
                    Cơ sở <c:out value="${centerIdCenterDTO.value.centerName}"/> 
                    hiện chưa có Lớp nào đang mở cho Khóa <c:out value="${course.courseName}"/>
                  </h5>
                </div>
              </c:if>
            </div>
          </div>
          
          <c:if test="${counter1.first}">
            <script id="script3">
                $("#${centerIdStr}-ce-tab-pane").addClass("show active");
                $("#script3").remove(); /* Xóa thẻ <script> sau khi xong */
            </script>
          </c:if>
    
        </c:forEach>
      </div>
      
    </div>
      
  </div>
  
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>

</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>