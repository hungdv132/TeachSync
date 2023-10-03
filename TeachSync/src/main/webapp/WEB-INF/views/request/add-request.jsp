<%@ page import="com.teachsync.utils.enums.RequestType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <fmt:setLocale value="vi_VN" scope="session"/>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Tạo đơn</title>

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">

  <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  
  <!-- Import the SDKs you need -->
  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js"></script>
  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-storage.js"></script>
  <script src="../../../resources/js/firebase.js"></script>
  
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
        <li class="breadcrumb-item" aria-current="page">
          <a href="/request">
            Đơn xin
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Tạo mới
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
  <div class="col-12 mb-3">
    <label class="border-bottom ts-border-blue ts-txt-lg w-100 pb-3 mb-3">Loại đơn: <br/>
      <select name="requestType" id="selRequestType" class="py-1" onchange="toggleRequestForm()">
        <option value="0" selected disabled hidden>-- Xin chọn loại đơn --</option>
        <option value="${RequestType.ENROLL}">Đăng ký học</option>
<%--        <option value="${RequestType.CHANGE_CLASS}">Xin chuyển lớp</option>--%>
      </select>
    </label>
  
    <form action="/add-request/enroll" method="POST"
          id="enrollForm" class="row visually-hidden"
          onsubmit="enableParamBeforeFormSubmit('enrollForm')">
      <input type="hidden" name="requesterId" value="${user.id}">
      <input type="hidden" name="requestType" value="${RequestType.ENROLL}">
      
      <!-- Course, Semester, Center -->
      <div class="col-sm-12 col-md-6">
        <div class="row">
          <!-- Select Course -->
          <label class="col-12 mb-1">Khóa học: <br/>
            <select name="course" id="selCourse" class="w-100 py-1" onchange="loadClassList(); loadCourseDetail();">
              <option value="0" selected disabled hidden>- Xin chọn khóa học -</option>
              <c:forEach var="courseDTO" items="${courseList}">
                <option value="${courseDTO.id}">
                  <c:out value="${courseDTO.courseAlias.concat(' - ').concat(courseDTO.courseName)}"/>
                </option>
              </c:forEach>
            </select>
          </label>

          <!-- Course Detail -->
          <!-- Course Price -->
          <p id="txtCoursePrice" class="col-4 mb-3">
            Giá tiền: <br/>
          </p>
          <!-- Course Discount -->
          <p id="txtCourseDiscount" class="col-4 visually-hidden mb-3">
            Giảm giá: <br/>
          </p>
          <!-- Course FinalPrice -->
          <div id="txtCourseFinalPrice" class="col-4 visually-hidden mb-3">
            Giá cuối: <br/>
          </div>

          <!-- Select Semester -->
          <label class="col-12 mb-1">Học kỳ: <br/>
            <select name="semester" id="selSemester" class="w-100 py-1" onchange="loadClassList(); loadSemesterDetail();">
              <option value="0" selected disabled hidden>- Xin chọn học kỳ -</option>
              
              <c:forEach var="semesterDTO" items="${semesterList}">
                <option value="${semesterDTO.id}">
                  <c:out value="${semesterDTO.semesterAlias.concat(' - ').concat(semesterDTO.semesterName)}"/>
                </option>
              </c:forEach>
            </select>
          </label>

          <!-- Semester Detail -->
          <!-- Semester Start -->
          <p id="txtSemesterStart" class="col-6 mb-3">
            Bắt đầu: <br/>
          </p>
          <!-- Semester End -->
          <p id="txtSemesterEnd" class="col-6 mb-3">
            Kết thúc: <br/>
          </p>

          <!-- Select Center -->
          <label class="col-12 mb-1">Cơ sở: <br/>
            <select name="center" id="selCenter" class="w-100 py-1" onchange="loadClassList(); loadCenterDetail();">
              <option value="0" selected disabled hidden>- Xin chọn cơ sở -</option>
              
              <c:forEach var="centerDTO" items="${centerList}">
                <option value="${centerDTO.id}">
                  <c:out value="${centerDTO.centerName}"/>
                </option>
              </c:forEach>
            </select>
          </label>
          
          <!-- Center Detail -->
          <!-- Center Address -->
          <p id="txtCenterAddr" class="col-12 mb-3">
            Địa chỉ: <br/>
          </p>
          
        </div>
      </div>
      
      <!-- Class -->
      <div id="classDetail" class="col-sm-12 col-md-6 visually-hidden">
        <div class="row">
          <!-- Select Class -->
          <label class="col-12 mb-1">Lớp học: <br/>
            <select id="selClazz" name="clazzId" class="w-100 py-1" onchange="loadClassDetail()">
              <option value="0" selected disabled hidden>- Xin chọn lớp học -</option>
              <c:forEach var="clazzDTO" items="${clazzList}">
                <option value="${clazzDTO.id}">
                  <c:out value="${clazzDTO.clazzName}"/>
                </option>
              </c:forEach>
            </select>
            <b class="ts-txt-orange visually-hidden" id="selClazz404">
              Hiện không có lớp học nào trong khoảng Khóa học & Học kỳ & Cơ sở đã chọn
            </b>
          </label>

          <!-- Class Detail -->
          <!-- Class Schedule -->
          <p id="txtClazzSchedule" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Lịch học: <br/>
          </p>
          <!-- Class Slot -->
          <p id="txtClazzSlot" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Tiết học: <br/>
          </p>
          <!-- Class Start (time) -->
          <p id="txtClazzFrom" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Bắt đầu: <br/>
          </p>
          <!-- Class end (time) -->
          <p id="txtClazzTo" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Kết thúc: <br/>
          </p>
          
          <!-- Class's room -->
          <p id="txtClazzRoom" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Phòng học: <br/>
          </p>
          <!-- Class teacher -->
          <p id="txtClazzTeacher" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Giáo viên: <br/>
          </p>
          <!-- Class teacher -->
          <p id="txtClazzMember" class="col-sm-6 col-md-3 visually-hidden mb-3">
            Thành viên: <br/>
          </p>
          
        </div>
      </div>
      
      <!-- Button / Message -->
      <div id="submitSection" class="col-12 d-flex justify-content-center" >
      </div>
    </form>
  
    <form action="/add-request/change-class" method="POST"
          id="changeClassForm" class="row visually-hidden">
  
    </form>
    
  </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    function toggleRequestForm() {
        let requestType = $("#selRequestType").val();

        switch (requestType) {
            case '${RequestType.ENROLL}':
                showById("enrollForm");
                hideById("changeClassForm");
                break;

            case '${RequestType.CHANGE_CLASS}':
                hideById("enrollForm");
                showById("changeClassForm");
                break;

            default:
                hideById("enrollForm");
                hideById("changeClassForm");
                break;
        }
    }

    function loadCourseDetail() {
        let courseId = Number($("#selCourse").val());

        /* value = '0' khi là option mặc định '- Chọn ... -'. Tức là chưa có chọn */
        if (courseId !== 0) {
            $.ajax({
                type: "GET",
                url: "/api/course-detail?courseId=" + courseId,
                success: function (response) {
                    let courseDTO = response['course'];

                    $("#txtCoursePrice").empty()
                        .append("Giá tiền: <br/> " + (courseDTO['currentPrice'])['price'] + " ₫");

                    let isPromotion = (courseDTO['currentPrice'])['isPromotion'];
                    if (isPromotion) {
                        let txtCourseDiscount = $("#txtCourseDiscount");
                        
                        txtCourseDiscount.removeClass("visually-hidden").empty()
                            .append("Giảm giá: <br/> " + (courseDTO['currentPrice'])['promotionAmount']);
                        
                        let promotionType = (courseDTO['currentPrice'])['promotionType'];
                        switch (promotionType) {
                            case "PERCENT":
                                txtCourseDiscount.append(" %");
                                break;
                            case "AMOUNT":
                                txtCourseDiscount.append(" ₫");
                                break;
                        }
                        
                        

                        $("#txtCourseFinalPrice").removeClass("visually-hidden").empty()
                            .append("Giá cuối: <br/> " + (courseDTO['currentPrice'])['finalPrice'] + " ₫");
                    } else {
                        $("#txtCourseDiscount").addClass("visually-hidden").empty();
                        $("#txtCourseFinalPrice").addClass("visually-hidden").empty();
                    }
                }
            })
        }
    }

    function loadSemesterDetail() {
        let semesterId = Number($("#selSemester").val());

        /* value = '0' khi là option mặc định '- Chọn ... -'. Tức là chưa có chọn */
        if (semesterId !== 0) {
            $.ajax({
                type: "GET",
                url: "/api/semester-detail?semesterId=" + semesterId,
                success: function (response) {
                    let semesterDTO = response['semester'];

                    $("#txtSemesterStart").empty()
                        .append("Bắt đầu: <br/> " + semesterDTO['startDate']);

                    $("#txtSemesterEnd").empty()
                        .append("Kết thúc: <br/> " + semesterDTO['endDate']);

                }
            })
        }
    }

    function loadCenterDetail() {
        let centerId = Number($("#selCenter").val());

        /* value = '0' khi là option mặc định '- Chọn ... -'. Tức là chưa có chọn */
        if (centerId !== 0) {
            $.ajax({
                type: "GET",
                url: "/api/center-detail?centerId=" + centerId,
                success: function (response) {
                    let centerDTO = response['center'];

                    $("#txtCenterAddr").empty()
                        .append("Địa chỉ: <br/> " + (centerDTO['address'])['addressString']);
                }
            })
        }
    }

    function loadClassList() {
        let courseId = Number($("#selCourse").val());
        let semesterId = Number($("#selSemester").val());
        let centerId = Number($("#selCenter").val());

        /* value = '0' khi là option mặc định '- Chọn ... -'. Tức là chưa có chọn */
        if (courseId !== 0 && semesterId !== 0 && centerId !== 0) {
            $.ajax({
                type: "GET",
                url: "/api/clazz?courseId=" + courseId + "&semesterId=" + semesterId + "&centerId=" + centerId,
                success: function (response) {
                    let clazzList = response['clazzList'];
                    
                    let selClazz = $("#selClazz");
                    selClazz.empty(); /* xóa dữ liệu cũ */

                    if (clazzList === null) {
                        $("#classDetail").removeClass("visually-hidden");

                        $("#txtClazzSchedule").addClass("visually-hidden").empty();
                        $("#txtClazzSlot").addClass("visually-hidden").empty();
                        $("#txtClazzFrom").addClass("visually-hidden").empty();
                        $("#txtClazzTo").addClass("visually-hidden").empty();
                        $("#txtClazzRoom").addClass("visually-hidden").empty();
                        $("#txtClazzTeacher").addClass("visually-hidden").empty();
                        $("#txtClazzMember").addClass("visually-hidden").empty();
                        selClazz.addClass("visually-hidden");
                        
                        $("#selClazz404").removeClass("visually-hidden");
                    } else {
                        $("#classDetail").removeClass("visually-hidden");

                        selClazz.removeClass("visually-hidden");
                        $("#selClazz404").addClass("visually-hidden");

                        selClazz.append(
                            '<option value="0" selected disabled hidden>-- Xin chọn lớp học --</option>');
                        for (const clazzDTO of clazzList) {
                            selClazz.append(
                                '<option value="' + clazzDTO['id'] + '">' + clazzDTO['clazzName'] + '</option>');
                        }
                    }
                }
            })
        }
    }

    function loadClassDetail() {
        let clazzId = Number($("#selClazz").val());

        /* value = '-1' khi là option mặc định '- Chọn ... -'. Tức là chưa có chọn */
        if (clazzId !== 0) {
            $.ajax({
                type: "GET",
                url: "/api/clazz-detail?clazzId=" + clazzId,
                success: function (response) {
                    let clazzDTO = response['clazz'];

                    let scheduleType = '';
                    switch ((clazzDTO['clazzSchedule'])['scheduleType']) {
                        case 'MON_WED_FRI':
                            scheduleType = "T2, T4, T6";
                            break;
                        case 'TUE_THU_SAT':
                            scheduleType = "T3, T5, T7";
                            break;
                        case 'SAT_SUN':
                            scheduleType = "T7, CN";
                            break;
                        case 'CUSTOM':
                            scheduleType = "Khác";
                            break;
                    }

                    $("#txtClazzSchedule").removeClass("visually-hidden").empty()
                        .append("Lịch học: <br/> " + (((clazzDTO['clazzSchedule'])['scheduleType'])['scheduleCategory'])['scheduleName']);

                    $("#txtClazzSlot").removeClass("visually-hidden").empty()
                        .append("Tiết học: <br/> Tiết " + (clazzDTO['clazzSchedule'])['slot']);
                    $("#txtClazzFrom").removeClass("visually-hidden").empty()
                        .append("Bắt đầu: <br/> " + (clazzDTO['clazzSchedule'])['sessionStart']);
                    $("#txtClazzTo").removeClass("visually-hidden").empty()
                        .append("Kết thúc: <br/> " + (clazzDTO['clazzSchedule'])['sessionEnd']);
                    $("#txtClazzRoom").removeClass("visually-hidden").empty()
                        .append("Phòng học: <br/> " + (clazzDTO['clazzSchedule'])['roomName']);
                    $("#txtClazzTeacher").removeClass("visually-hidden").empty()
                        .append("Giáo viên: <br/> " + ((clazzDTO['staff'])['user'])['fullName']);
                    let memberList = clazzDTO['memberList'];
                    let memberCount = memberList == null ? 0 : memberList.length;
                    $("#txtClazzMember").removeClass("visually-hidden").empty()
                        .append("Thành viên: <br/> " + memberCount + " / " + clazzDTO['clazzSize']);

                    if (memberCount < clazzDTO['clazzSize']) {
                        $("#submitSection").empty()
                            .append('<button type="submit" class="btn btn-primary w-50">Gửi đơn</button>');
                    } else {
                        $("#submitSection").empty()
                            .append('<p class="ts-txt-orange ts-txt-italic">Lớp học này đã đầy, xin hãy thử lớp khác</p>');
                    }
                }
            })
        }
    }

    function enableParamBeforeFormSubmit(formId) {
        enableAllFormElementIn(formId);

        const form = document.getElementById(formId);
        form.requestSubmit();
    }
</script>

<script id="script1">
    <c:if test="${fromEnroll}">
        $("#selRequestType").val("${RequestType.ENROLL}").prop('disabled', true);
        toggleRequestForm();
    
        /* Course */
        $("#selCourse").val("${courseList.get(0).id}").prop('disabled', true);
        $("#txtCoursePrice").append("${courseList.get(0).currentPrice.price} ₫");
        let isPromotion = ${courseList.get(0).currentPrice.isPromotion};
        if (isPromotion) {
            $("#txtCourseDiscount").removeClass("visually-hidden")
                .append("${courseList.get(0).currentPrice.promotionAmount}");
            $("#txtCourseFinalPrice").removeClass("visually-hidden")
                .append("${courseList.get(0).currentPrice.finalPrice} ₫");
        }
    
        /* Semester */
        $("#selSemester").val("${semesterList.get(0).id}").prop('disabled', true);
    
        <fmt:parseDate value="${semesterList.get(0).startDate}" type="date"
                       pattern="yyyy-MM-dd" var="parsedStartDate" />
        <fmt:parseDate value="${semesterList.get(0).endDate}" type="date"
                       pattern="yyyy-MM-dd" var="parsedEndDate" />
        
        $("#txtSemesterStart").append('<fmt:formatDate value="${parsedStartDate}" type="date" pattern="dd/MM/yyyy"/>');
        $("#txtSemesterEnd").append('<fmt:formatDate value="${parsedEndDate}" type="date" pattern="dd/MM/yyyy"/>');
    
        /* Center */
        $("#selCenter").val("${centerList.get(0).id}").prop('disabled', true);
        $("#txtCenterAddr").append("${centerList.get(0).address.addressString}");
    
        /* Clazz */
        $("#classDetail").removeClass("visually-hidden");
        $("#selClazz").val("${clazzList.get(0).id}").prop('disabled', true);
        
        $("#txtClazzSchedule").removeClass("visually-hidden").append("${clazzList.get(0).clazzSchedule.scheduleCategory.scheduleName}");
        $("#txtClazzSlot").removeClass("visually-hidden").append("Tiết ${clazzList.get(0).clazzSchedule.slot}");
        $("#txtClazzFrom").removeClass("visually-hidden").append("${clazzList.get(0).clazzSchedule.sessionStart}");
        $("#txtClazzTo").removeClass("visually-hidden").append("${clazzList.get(0).clazzSchedule.sessionEnd}");
        $("#txtClazzRoom").removeClass("visually-hidden").append("${clazzList.get(0).clazzSchedule.roomName}");
        $("#txtClazzTeacher").removeClass("visually-hidden").append("${clazzList.get(0).staff.user.fullName}");
        let memberCount = ${empty clazzList.get(0).memberList ? 0 : clazzList.get(0).memberList.size()};
        $("#txtClazzMember").removeClass("visually-hidden").append(memberCount + " / ${clazzList.get(0).clazzSize}");
    
        $("#submitSection").empty()
            .append('<button type="submit" class="btn btn-primary w-50">Gửi đơn</button>');
        $("#changeClassForm").remove();
    </c:if>
    
    // $("#script1").remove(); /* Xóa thẻ <script> sau khi xong */
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>