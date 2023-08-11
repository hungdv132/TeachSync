<%@ page import="java.lang.Long" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Semester List</title>

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
        <li class="breadcrumb-item active" aria-current="page">
          Học kỳ
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
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-4 mx-2 mb-3">
  <div class="col-12 px-4 mb-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h4>Học kỳ</h4>
      
      <form action="/semester" method="post">
        <button type="submit" class="btn btn-success">Thêm mới</button>
      </form>
    </div>
    
    <!-- Paging navigation -->
    <div class="d-flex align-items-center mb-3">
      <a href="/semester?pageNo=0" class="btn btn-secondary"><i class="bi-chevron-bar-left"></i></a>
      <a href="/semester?pageNo=${pageNo}" class="btn btn-secondary mx-2"><i class="bi-chevron-left"></i></a>
      Page: <c:out value="${pageNo + 1}"/> &sol; <c:out value="${pageTotal}"/>
      <a href="/semester?pageNo=${pageNo + 1}" class="btn btn-secondary mx-2"><i class="bi-chevron-right"></i></a>
      <a href="/semester?pageNo=${pageTotal-1}" class="btn btn-secondary"><i class="bi-chevron-bar-right"></i></a>
    </div>
    
    <c:if test="${not empty semesterIdSemesterDTOMap}">
  
      <!-- Semester Tab -->
      <ul class="nav nav-tabs align-items-center" id="semesterTab" role="tablist">
        <c:forEach var="semesterIdSemesterDTO" items="${semesterIdSemesterDTOMap}" varStatus="counter">
          <c:set var="semesterIdString" value="${semesterIdSemesterDTO.key.toString()}"/>
          <li class="nav-item" role="presentation">
            <button type="button" class="nav-link" id="${semesterIdString}-se-tab"
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

      <!-- Semester TabPane -->
      <div class="tab-content border rounded-bottom-3 p-3" id="semesterTabContent">
        <c:forEach var="semesterIdSemesterDTO" items="${semesterIdSemesterDTOMap}" varStatus="counter">
          <c:set var="semesterId" value="${semesterIdSemesterDTO.key.toString()}"/>
          
          <div class="tab-pane fade" id="${semesterIdString}-se-tab-pane"
               role="tabpanel" aria-labelledby="${semesterIdString}-se-tab" tabindex="0">
  
            <div class="">
              <p>Mã: <c:out value="${semesterIdSemesterDTO.value.semesterAlias}"/>&nbsp;&nbsp;&nbsp;
                 Tên: <c:out value="${semesterIdSemesterDTO.value.semesterName}"/></p>
              <p>Bắt đầu: <c:out value="${semesterIdSemesterDTO.value.startDate}"/>&nbsp;&nbsp;&nbsp;
                 Kết thúc: <c:out value="${semesterIdSemesterDTO.value.endDate}"/></p>
            </div>
  
            <!-- Center Tab -->
            <ul class="nav nav-tabs" id="semesterTab${semesterIdString}" role="tablist">
              <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter2">
                <c:set var="centerId" value="${centerIdCenterDTO.key.toString()}"/>
              
                <li class="nav-item" role="presentation">
                  <button type="button" class="nav-link" id="${semesterIdString}-se-${centerId}-ce-tab"
                          data-bs-toggle="tab" data-bs-target="#${semesterIdString}-se-${centerId}-ce-tab-pane" role="tab"
                          aria-controls="${semesterIdString}-se-${centerId}-ce-tab-pane" aria-selected="true">
                    <c:out value="${centerIdCenterDTO.value.centerName}"/>
                  </button>
                </li>
                
                <c:if test="${counter2.first}">
                  <script id="script2">
                      $("#${semesterIdString}-se-${centerId}-ce-tab").addClass("active").attr("aria-selected", "true");
                      $("#script2").remove(); /* Xóa thẻ <script> sau khi xong */
                  </script>
                </c:if>
                
              </c:forEach>
            </ul>

            <!-- Center TabPane -->
            <div class="tab-content border rounded-bottom-3 p-3" id="semesterTab${semesterIdString}Content">
              <c:forEach var="centerIdCenterDTO" items="${centerIdCenterDTOMap}" varStatus="counter2">
                <c:set var="centerId" value="${centerIdCenterDTO.key.toString()}"/>
                
<%--                <div class="">--%>
<%--                  <p>Tên: <c:out value="${centerIdCenterDTO.value.centerName}"/></p>--%>
<%--                  <p>Địa chỉ: <c:out value="${centerIdCenterDTO.value.addressId}"/></p>--%>
<%--                </div>--%>
                
                <div class="tab-pane fade" id="${semesterIdString}-se-${centerId}-ce-tab-pane" role="tabpanel"
                     aria-labelledby="${semesterIdString}-se-${centerId}-ce-tab" tabindex="0">
                  <div class="row px-2">
                    <c:forEach var="courseIdCourseDTO" items="${courseIdCourseDTOMap}" varStatus="counter4">
                      <c:set var="courseId" value="${courseIdCourseDTO.key.toString()}"/>
                      <div class="col-2 py-2">
                        <input type="checkbox" name="courseId"
                               id="${semesterIdString}-se-${centerId}-ce-${courseId}-co"
                               value="${courseIdCourseDTO.value.id}" disabled="disabled">
      
                        <c:if test="${distributionMap.get(Long.parseLong(semesterIdString)) ne null}">
                          <c:if test="${distributionMap.get(Long.parseLong(semesterIdString))
                                                        .get(Long.parseLong(centerId)) ne null}">
                            <c:if test="${distributionMap.get(Long.parseLong(semesterIdString))
                                                          .get(Long.parseLong(centerId))
                                                          .contains(Long.parseLong(courseId))}">
                              <script id="script3">
                                  $("#${semesterIdString}-se-${centerId}-ce-${courseId}-co").prop("checked", true);
                                  $("#script3").remove(); /* Xóa thẻ <script> sau khi xong */
                              </script>
                            </c:if>
                          </c:if>
                        </c:if>
      
                        <c:out value="${courseIdCourseDTO.value.courseAlias}"/>
                      </div>
                    </c:forEach>
                    
                    <div class="col-12 my-2"></div>
                    
                    <div id="${semesterIdString}-se-${centerId}-ce-tab-pane-btn-edit" class="btn btn-warning col-2"
                         onclick="openEditCourseSemester('${semesterIdString}-se-${centerId}-ce-tab-pane',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-edit',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-cancel',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-save')">
                      Chỉnh sửa
                    </div>
  
                    <div id="${semesterIdString}-se-${centerId}-ce-tab-pane-btn-cancel" class="btn btn-danger col-2 visually-hidden"
                         onclick="cancelEditCourseSemester('${semesterIdString}-se-${centerId}-ce-tab-pane',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-edit',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-cancel',
                                '${semesterIdString}-se-${centerId}-ce-tab-pane-btn-save')">
                      Hủy
                    </div>
  
                    <div id="${semesterIdString}-se-${centerId}-ce-tab-pane-btn-save" class="btn btn-primary col-2 visually-hidden ms-2"
                         onclick="sendRequestEditCourseSemester('${semesterIdString}-se-${centerId}-ce-tab-pane')">
                      Lưu
                    </div>
                  </div>
                </div>
  
                <c:if test="${counter2.first}">
                  <script id="script4">
                      $("#${semesterIdString}-se-${centerId}-ce-tab-pane").addClass("show active");
                      $("#script4").remove(); /* Xóa thẻ <script> sau khi xong */
                  </script>
                </c:if>
                
              </c:forEach>
            </div>
            
          </div>
          
          <c:if test="${counter.first}">
            <script id="script5">
                $("#${semesterIdString}-se-tab-pane").addClass("show active");
                $("#script5").remove(); /* Xóa thẻ <script> sau khi xong */
            </script>
          </c:if>
      
        </c:forEach>
      </div>
  
    </c:if>
  
    
  
    <c:if test="${empty semesterIdSemesterDTOMap}">
<%--      TODO:--%>
    </c:if>
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

  function openEditCourseSemester(divId, editBtnId, cancelBtnId, saveBtnId) {
      enableAllInputIn(divId, 'checkbox');
      hideById(editBtnId);
      showById(cancelBtnId);
      showById(saveBtnId);
  }

  function cancelEditCourseSemester(divId, editBtnId, cancelBtnId, saveBtnId) {
      disableAllInputIn(divId, 'checkbox');
      showById(editBtnId);
      hideById(cancelBtnId);
      hideById(saveBtnId);
  }

  function sendRequestEditCourseSemester(divId) {
      let semesterId = Number(${semesterIdString});
      let centerId = Number(${centerId});

      let courseIdList = $("#"+divId+" input[type=checkbox]:checked").val();
      courseIdList = courseIdList.map(Number);

      let courseSemester = [];
      for (const courseId of courseIdList) {
          courseSemester.push({
              "semesterId": semesterId,
              "centerId": centerId,
              "courseId": courseId});
      }

      $.ajax({
          type: "POST",
          url: "/semester",
          data: JSON.stringify(courseSemester),
          contentType: "application/json",
          success: function(response) {
              if (response['view'] != null) {
                  location.href = response['view'];
              }


          }
      });
  }
</script>
</html>