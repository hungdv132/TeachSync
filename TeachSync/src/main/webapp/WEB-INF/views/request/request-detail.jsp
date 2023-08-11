<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Enroll detail</title>

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
        <li class="breadcrumb-item" aria-current="page">
          <c:url var="courseDetail" value="course-detail">
            <c:param name="id" value="${course.id}"/>
          </c:url>
          <a href="${courseDetail}">
            <c:out value="${course.courseName}"/>
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Đăng ký học
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
  <!-- Course List paging -->
  <div class="col-12 mb-3">
    <div class="row-cols-1">

      <h4>Khóa học: <c:out value="${course.courseName}"/></h4>
      <h5>Học kỳ:</h5>

      <c:forEach var="courseSemesterClazzList" items="${scheduleClazzListMap}">

        <div class="col card">
          <div class="card-header">
            <h6 class="card-title mb-0">
              Kỳ: <c:out value="${courseSemesterClazzList.key.scheduleAlias}"/><br/>
              Cơ sở: <c:out value="${courseSemesterClazzList.key.centerName}"/><br/>
              Từ: <c:out value="${courseSemesterClazzList.key.startDate}"/><br/>
              Đến: <c:out value="${courseSemesterClazzList.key.endDate}"/><br/>
            </h6>
          </div>

          <div class="card-body row flex-row flex-nowrap overflow-scroll">
            <c:forEach var="clazzDTO" items="${courseSemesterClazzList.value}">
              <div class="col-4">
                <div class="card">
                  <c:url var="enrollLink" value="enroll">
                    <c:param name="clazzId" value="${clazzDTO.id}"/>
                  </c:url>
                  <form action="${enrollLink}" method="post">
                    <div class="card-header">
                      <h6 class="card-subtitle">
                        Lớp: <c:out value="${clazzDTO.clazzName}"/>
                      </h6>
                    </div>

                    <c:set var="memberCount" value="0"/>
                    <c:if test="${clazzDTO.memberList ne null}">
                      <c:set var="memberCount" value="${clazzDTO.memberList.size()}"/>
                    </c:if>

                    <div class="card-body">
                      <p class="card-text">
                        Lịch học: <c:out value="${clazzDTO.clazzSchedule.scheduleType}"/><br/>
                        Slot: <c:out value="${clazzDTO.clazzSchedule.slot}"/><br/>
                        Từ: <c:out value="${clazzDTO.clazzSchedule.sessionStart}"/>&nbsp;
                        Đến: <c:out value="${clazzDTO.clazzSchedule.sessionEnd}"/><br/>
  
                        Thành viên: <c:out value="${memberCount}"/> &sol; <c:out value="${clazzDTO.clazzSize}"/>
                      </p>
                    </div>

                    <div class="card-footer">
                      <c:choose>
                        <c:when test="${memberCount lt clazzDTO.clazzSize}">
                          <button type="submit" class="btn btn-primary w-100" >Đăng ký</button>
                        </c:when>
                        <c:when test="${memberCount ge clazzDTO.clazzSize}">
                          <button type="submit" class="btn btn-primary w-100" disabled="disabled">Đăng ký</button>
                          <span class="ts-txt-orange">Lớp học đã đầy</span>
                        </c:when>
                      </c:choose>
                    </div>
                  </form>
                </div>

              </div>
            </c:forEach>
          </div>

        </div>

      </c:forEach>

    </div>
  </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>