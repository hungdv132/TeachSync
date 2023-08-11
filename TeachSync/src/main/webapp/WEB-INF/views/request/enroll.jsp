<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Enroll</title>

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
          <c:url var="courseDetail" value="/course-detail">
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
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
  <div class="col-12 mb-3">
    <div class="row-cols-1">

      <h4>Khóa học: <c:out value="${course.courseName}"/></h4>
      <h5>Học kỳ: </h5>
  
      <!-- Semester Tab -->
      <ul class="nav nav-tabs align-items-center" id="semesterTab" role="tablist">
        <c:forEach var="semesterIdSemesterDTO" items="${semesterIdSemesterDTOMap}" varStatus="counter">
          <c:set var="semesterIdString" value="${semesterIdSemesterDTO.key.toString()}"/>
          <li class="nav-item" role="presentation">
            <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue" id="${semesterIdString}-se-tab"
                    data-bs-toggle="tab" data-bs-target="#${semesterIdString}-se-tab-pane" role="tab"
                    aria-controls="${semesterIdString}-se-tab-pane" aria-selected="false">
              <c:out value="${semesterIdSemesterDTO.value.semesterAlias}"/>
            </button>
          </li>
    
          <c:if test="${counter.first}">
            <script id="script1">
                $("#${semesterIdString}-se-tab").addClass("active").attr("aria-selected", "true");
                $("#script1").remove(); /* Xóa thẻ <script> sau khi xong */
            </script>
          </c:if>
        </c:forEach>
      </ul>
      
      <!-- Semester TabPane List -->
      <div class="tab-content border border-top-0 rounded-bottom-3 pt-3" id="semesterTabContent">
        <c:forEach var="semesterIdSemesterDTO" items="${semesterIdSemesterDTOMap}" varStatus="counter">
          <c:set var="semesterIdString" value="${semesterIdSemesterDTO.key.toString()}"/>
          
          <!-- Semester ${semesterIdString} TabPane -->
          <div class="tab-pane fade" id="${semesterIdString}-se-tab-pane"
               role="tabpanel"
               aria-labelledby="${semesterIdString}-se-tab">
  
            <!-- Semester ${semesterIdString} detail -->
            <div class="border-bottom px-3 mb-3">
              <p class="mb-1">Kỳ: <c:out value="${semesterIdSemesterDTO.value.semesterAlias}"/>&nbsp;-&nbsp;
                 <c:out value="${semesterIdSemesterDTO.value.semesterName}"/></p>
              <p>Từ: <c:out value="${semesterIdSemesterDTO.value.startDate}"/>&nbsp;&nbsp;&nbsp;
                 Đến: <c:out value="${semesterIdSemesterDTO.value.endDate}"/></p>
            </div>
            
            <!-- Center Tab & TabPane -->
            <div class="px-3 mb-3">
              
              <!-- Center Tab -->
              <ul class="nav nav-tabs" id="semesterTab${semesterIdString}" role="tablist">
                <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter2">
                  <c:set var="centerIdString" value="${centerIdCenterDTO.key.toString()}"/>
  
                  <li class="nav-item" role="presentation">
                    <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue" id="${semesterIdString}-se-${centerIdString}-ce-tab"
                            data-bs-toggle="tab" data-bs-target="#${semesterIdString}-se-${centerIdString}-ce-tab-pane" role="tab"
                            aria-controls="${semesterIdString}-se-${centerIdString}-ce-tab-pane" aria-selected="true">
                      <c:out value="${centerIdCenterDTO.value.centerName}"/>
                    </button>
                  </li>
              
                  <c:if test="${counter2.first}">
                    <script id="script2">
                        $("#${semesterIdString}-se-${centerIdString}-ce-tab").addClass("active").attr("aria-selected", "true");
                        $("#script2").remove(); /* Xóa thẻ <script> sau khi xong */
                    </script>
                  </c:if>
            
                </c:forEach>
              </ul>
          
              <!-- Center TabPane List -->
              <div class="tab-content border border-top-0 rounded-bottom-3 pt-3"
                   id="semesterTab${semesterIdString}Content">
                <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter2">
                  <c:set var="centerIdString" value="${centerIdCenterDTO.key.toString()}"/>
  
                  <!-- Semester ${semesterIdString} > Center ${centerIdString} TabPane -->
                  <div class="tab-pane fade row mx-0" id="${semesterIdString}-se-${centerIdString}-ce-tab-pane"
                       role="tabpanel"
                       aria-labelledby="${semesterIdString}-se-${centerIdString}-ce-tab">
        
                    <c:set var="semesterIdCenterIdString" value="${semesterIdString.concat(centerIdString)}"/>
                    <c:set var="clazzList" value="${semesterIdCenterIdStringClazzListMap.get(semesterIdCenterIdString)}"/>
                    
                    <!-- Center ${centerIdString} detail -->
                    <div class="border-bottom px-3 mb-3">
                      <p class="mb-1">Tên: <c:out value="${centerIdCenterDTO.value.centerName}"/></p>
                      <p>Địa chỉ: <c:out value="${centerIdCenterDTO.value.address.addressString}"/></p>
                    </div>
    
                    <!-- clazzList -->
                    <div class="px-3 mb-3">
                      <!-- If clazzList != null => show result -->
                      <c:if test="${not empty clazzList}">
                        <c:forEach var="clazzDTO" items="${clazzList}" varStatus="counter4">
            
                          <c:url var="enrollLink" value="/enroll">
                            <c:param name="clazzId" value="${clazzDTO.id}"/>
                          </c:url>
                          <form action="${enrollLink}" method="post" class="col-3 card px-0">
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
                                  <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
                                </c:when>
                                <c:when test="${memberCount ge clazzDTO.clazzSize}">
                                  <button type="submit" class="btn btn-primary w-100" disabled="disabled">Đăng ký
                                  </button>
                                  <span class="ts-txt-orange">Lớp học đã đầy</span>
                                </c:when>
                              </c:choose>
                            </div>
                          </form>
          
                        </c:forEach>
                      </c:if>
          
                      <!-- If clazzList == null -->
                      <c:if test="${empty clazzList}">
                        <div class="col-12 d-flex justify-content-center">
                          <h5 class="ts-txt-orange">
                            Cơ sở ${centerIdCenterDTO.value.centerName} chưa có lớp nào mở cho học
                            kỳ ${semesterIdSemesterDTO.value.semesterAlias}.
                          </h5>
                        </div>
                      </c:if>
                    </div>
                  </div>
                  
                  <c:if test="${counter2.first}">
                    <script id="script3">
                        $("#${semesterIdString}-se-${centerIdString}-ce-tab-pane").addClass("show active");
                        $("#script3").remove(); /* Xóa thẻ <script> sau khi xong */
                    </script>
                  </c:if>
            
                </c:forEach>
              </div>
            </div>
          </div>
      
          <c:if test="${counter.first}">
            <script id="script4">
                $("#${semesterIdString}-se-tab-pane").addClass("show active");
                $("#script4").remove(); /* Xóa thẻ <script> sau khi xong */
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