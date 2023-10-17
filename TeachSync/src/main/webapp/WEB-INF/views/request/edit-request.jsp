<%@ page import="com.teachsync.utils.enums.RequestType" %>
<%@ page import="com.teachsync.utils.enums.PaymentType" %>
<%@ page import="com.teachsync.utils.enums.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <fmt:setLocale value="vi_VN" scope="session"/>
  
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Chi tiết đơn</title>
  
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
          <a href="/request">
            Đơn xin
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Chi tiết đơn
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  

<!-- ================================================== Main Body ================================================== -->
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
  
    <!-- Request detail -->
    <div class="border-bottom ts-border-blue mb-3">
      <h4 class="mb-3">Mã đơn: <c:out value="${request.id}"/></h4>
      
      <h5 class="mb-3">Loại đơn: <c:out value="${request.requestType.stringValueVie}"/></h5>
  
      <h5 class="mb-3">Trạng thái: <c:out value="${request.status.stringValueVie}"/></h5>
  
      <h5 class="mb-1">Chi tiết: </h5>
      <p class="mb-3"><c:out value="${request.requestDesc}" escapeXml="false"/></p>
    </div>

    <!-- Request form -->
    <div class="row">
      <c:set var="status" value="${request.status}"/>
      
      <c:if test="${request.requestType.equals(RequestType.ENROLL)}">
        
        <c:set var="course" value="${request.clazz.course}"/>
        <c:set var="currentPrice" value="${course.currentPrice}"/>
        <c:set var="center" value="${request.clazz.center}"/>
        <c:set var="clazzSchedule" value="${request.clazz.clazzSchedule}"/>
        
        <!-- Request detail -->
        <div class="col-sm-12 col-md-6 mb-3">
          <div class="card ts-border-teal">
            <!-- Center & Room -->
            <div class="card-header ts-border-teal">
              <h6 class="card-title mb-0">
                Cơ sở: <c:out value="${center.centerName}"/>
              </h6>
            </div>
            <div class="card-body">
              <p class="card-text">
                Địa chỉ: <c:out value="${center.address.addressString}"/>
              </p>
              <p class="card-text">
                Phòng: <c:out value="${clazzSchedule.roomName}"/>
              </p>
            </div>
          
            <!-- Course & Clazz -->
            <div class="card-header border-top ts-border-teal">
              <h6 class="card-title mb-0">
                Khóa học: <c:out value="${course.courseAlias.concat(' - ').concat(course.courseName)}"/>
              </h6>
            </div>
            <div class="card-body row">
              <c:if test="${currentPrice.isPromotion}">
                <p class="card-text col-6">
                  Giá gốc: <fmt:formatNumber value="${currentPrice.price}" type="currency"/>
                </p>
                <p class="card-text col-6">
                  Giảm giá: <fmt:formatNumber value="${currentPrice.promotionAmount}" type="${currentPrice.promotionType.fmtType}"/>
                </p>
              </c:if>
              <p class="card-text col-12">
                Cần đóng: <fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/>
              </p>
            </div>
          
            <!-- Clazz -->
            <div class="card-header border-top ts-border-teal">
              <h6 class="card-title mb-0">
                Lớp: <c:out value="${request.clazz.clazzAlias.concat(' - ').concat(request.clazz.clazzName)}"/>
              </h6>
            </div>
            <div class="card-body row">
              <fmt:parseDate value="${clazzSchedule.startDate}" type="date" pattern="yyyy-MM-dd" var="parsedStartDate"/>
              <p class="card-text col-6">
                Bắt đầu: <fmt:formatDate value="${parsedStartDate}" pattern="dd/MM/yyyy"/>
              </p>
              <fmt:parseDate value="${clazzSchedule.endDate}" type="date" pattern="yyyy-MM-dd" var="parsedEndDate"/>
              <p class="card-text col-6">
                Kết thúc: <fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy"/>
              </p>
              <p class="card-text col-6">
                Lịch học: <c:out value="${clazzSchedule.scheduleCategory.scheduleName}"/>
              </p>
              <p class="card-text col-6">
                Tiết: <c:out value="${clazzSchedule.slot}"/>
              </p>
              <p class="card-text col-6 mb-0">
                Từ: <c:out value="${clazzSchedule.sessionStart}"/>
              </p>
              <p class="card-text col-6 mb-0">
                Đến: <c:out value="${clazzSchedule.sessionEnd}"/>
              </p>
            </div>
          </div>
        </div>
        
        <!-- Edit enroll request -->
        <div class="col-sm-12 col-md-6 mb-3">
          <c:if test="${isStudent}">
            <c:choose>
              <c:when test="${status eq Status.PENDING_PAYMENT}">
                <!-- Nếu Request chưa được duyệt, thì cho edit -->
                <%--@elvariable id="updateDTO" type="com.teachsync.dtos.request.RequestUpdateDTO"--%>
                <form:form id="formUpdateEnrollStudent"
                           modelAttribute="updateDTO" action="/edit-request/enroll" method="POST"
                           cssClass="card ts-border-yellow">
                
                  <div class="card-header ts-border-yellow">
                    <h6 class="card-title mb-0">Chuyển khoản</h6>
                  </div>
                  
                  <div class="card-body">
                    <input type="hidden" name="id" value="${request.id}">
                    <input type="hidden" name="requesterId" value="${request.requesterId}">
                    <input type="hidden" name="requestName" value="${request.requestName}">
                    <input type="hidden" name="requestDesc" value="${request.requestDesc}">
                    <input type="hidden" name="requestType" value="${RequestType.ENROLL}">
                    <input type="hidden" name="clazzId" value="${request.clazzId}">
                    <input type="hidden" name="status" value="${Status.AWAIT_CONFIRM}">
  
                    <!-- Transfer info -->
                    <div class="row">
                      <!-- Transfer QR code -->
                      <div class="col-5">
                        <img src="../../../resources/img/QRCode.jpg" alt="QR code"
                             class="rounded-4 border ts-border-teal w-100 h-auto">
                      </div>
    
                      <!-- Transfer info -->
                      <div class="col-7">
                        <h6 class="mb-1">Thông tin chuyển khoản: </h6>
                        <p class="mb-3">
                          VIETCOMBANK <br>
                          123 456 789 123 456 789 <br>
                          Teach Sync Edu Inc
                        </p>
      
                        <h6 class="mb-1">Nội dung chuyển khoản: </h6>
                        <p class="mb-3"><c:out value="${request.requestName}"/></p>
      
                        <h6 class="mb-1">Khoản cần chuyển: </h6>
                        <p class="mb-0"><fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/></p>
                      </div>
                    </div>

                    <!-- Transfer file input -->
                    <label for="filePaymentInfoStudent" class="form-label">
                      <h6 class="mb-1">Chứng từ chuyển khoản: </h6>
                    </label>
                    <input id="filePaymentInfoStudent" name="file"
                           type="file"
                           class="form-control ts-border-grey mb-3"
                           required="required">
  
                    <input id="hidPaymentInfoStudent" name="contentLink"
                           type="hidden" value="">
  
                    <div class="w-100 d-flex justify-content-center">
                      <button type="submit"
                              class="btn btn-primary w-45">
                        Lưu
                      </button>
                    </div>
                  </div>
                </form:form>
              
                <script>
                    $("#formUpdateEnrollStudent").on("submit", function (e) {
                        e.preventDefault();
    
                        let file = $("#filePaymentInfoStudent").prop("files")[0];
    
                        uploadFileToFirebaseAndGetURL(file).then( function (fileURL) {
                            $("#hidPaymentInfoStudent").val(fileURL);
    
                            $("#formUpdateEnrollStudent")[0].submit();
                        })
                    });
                </script>
              </c:when>
            
              <c:when test="${status eq Status.AWAIT_CONFIRM}">
                <%--@elvariable id="updateDTO" type="com.teachsync.dtos.request.RequestUpdateDTO"--%>
                <form:form id="formUpdateEnrollStudent"
                           modelAttribute="updateDTO" action="/edit-request/enroll" method="POST"
                           cssClass="card ts-border-yellow">
  
                  <div class="card-header ts-border-yellow">
                    <h6 class="card-title mb-0">Chuyển khoản</h6>
                  </div>
        
                  <div class="card-body overflow-hidden">
                    <input type="hidden" name="id" value="${request.id}">
                    <input type="hidden" name="requesterId" value="${request.requesterId}">
                    <input type="hidden" name="requestName" value="${request.requestName}">
                    <input type="hidden" name="requestDesc" value="${request.requestDesc}">
                    <input type="hidden" name="requestType" value="${RequestType.ENROLL}">
                    <input type="hidden" name="clazzId" value="${request.clazzId}">
                    <input type="hidden" name="status" value="${Status.AWAIT_CONFIRM}">
                  
                    <!-- Transfer info -->
                    <div class="row mb-3">
                      <!-- Transfer QR code -->
                      <div class="col-5">
                        <img src="../../../resources/img/QRCode.jpg" alt="QR code"
                             class="rounded-4 border ts-border-teal w-100 h-auto">
                      </div>
                    
                      <!-- Transfer info -->
                      <div class="col-7">
                        <h6 class="mb-1">Thông tin chuyển khoản: </h6>
                        <p class="mb-3">
                          VIETCOMBANK <br>
                          123 456 789 123 456 789 <br>
                          Teach Sync Edu Inc
                        </p>
                      
                        <h6 class="mb-1">Nội dung chuyển khoản: </h6>
                        <p class="mb-3"><c:out value="${request.requestName}"/></p>
                      
                        <h6 class="mb-1">Khoản cần chuyển: </h6>
                        <p class="mb-3"><fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/></p>
                      </div>
                    </div>
  
                    <!-- Transfer file input -->
                    <label for="filePaymentInfoStudent" class="form-label">
                      <h6 class="mb-1">Chứng từ chuyển khoản: </h6>
                    </label>
                    <a id="aPaymentInfo"
                       href="${request.contentLink}"
                       class="text-nowrap"
                       onclick="window.open(this.href, '_blank'); return false;">
                      <c:out value="${request.contentLink}"/>
                    </a>
                    <input id="filePaymentInfoStudent" name="file"
                           type="file"
                           class="form-control ts-border-grey mb-3 visually-hidden"
                           required="required" disabled="disabled">
                  
                    <input id="hidPaymentInfoStudent" name="contentLink"
                           type="hidden" value="${request.contentLink}">
                  
                    <div class="w-100 d-flex justify-content-center">
                      <button id="btnEditPaymentInfoStudent"
                              type="button"
                              class="btn btn-warning w-45"
                              onclick="editPaymentInfoStudent()">
                        Đổi chứng từ
                      </button>
                    
                      <button id="btnUpdateRequestEnrollStudent"
                              type="submit"
                              class="btn btn-danger w-45 visually-hidden">
                        Cập nhập
                      </button>
                    
                      <button id="btnCancelEditPaymentInfoStudent"
                              type="button"
                              class="btn btn-danger w-45 ms-2 visually-hidden"
                              onclick="cancelEditPaymentInfoStudent()">
                        Hủy
                      </button>
                    </div>
                  </div>
                </form:form>
              
                <script>
                    $("#formUpdateEnrollStudent").on("submit", function (e) {
                        e.preventDefault();
    
                        let file = $("#filePaymentInfoStudent").prop("files")[0];
    
                        uploadFileToFirebaseAndGetURL(file).then( function (fileURL) {
                            $("#hidPaymentInfoStudent").val(fileURL);
    
                            $("#formUpdateEnrollStudent")[0].submit();
                        })
                    });
    
                    function editPaymentInfoStudent() {
                        hideById("aPaymentInfo");
                        showById("filePaymentInfoStudent");
                        enableById("filePaymentInfoStudent");
    
                        hideById("btnEditPaymentInfoStudent");
                        showById("btnUpdateRequestEnrollStudent");
                        showById("btnCancelEditPaymentInfoStudent");
                    }
    
                    function cancelEditPaymentInfoStudent() {
                        showById("aPaymentInfo");
                        hideById("filePaymentInfoStudent");
                        disableById("filePaymentInfoStudent");
    
                        showById("btnEditPaymentInfoStudent");
                        hideById("btnUpdateRequestEnrollStudent");
                        hideById("btnCancelEditPaymentInfoStudent");
                    }
                </script>
              </c:when>
            
              <c:when test="${status eq Status.APPROVED}">
                <div>
                
                </div>
              </c:when>
            
              <c:when test="${status eq Status.DENIED}">
            
              </c:when>
            </c:choose>
          </c:if>
        
          <c:if test="${isAdmin}">
            <c:choose>
              <c:when test="${status eq Status.PENDING_PAYMENT}">
                <form:form id="formUpdateEnrollAdmin"
                           action="/edit-request/enroll" method="POST"
                           cssClass="card ts-border-yellow">
                  <div class="card-header ts-border-yellow">
                    <h6 class="card-title mb-0">Duyệt đơn</h6>
                  </div>
                  
                  <div class="card-body">
                    <!-- RequestUpdateDTO -->
                    <input type="hidden" name="id" value="${request.id}">
                    <input type="hidden" name="requesterId" value="${request.requesterId}">
                    <input type="hidden" name="requestName" value="${request.requestName}">
                    <input type="hidden" name="requestDesc" value="${request.requestDesc}">
                    <input type="hidden" name="requestType" value="${RequestType.ENROLL}">
                    <input type="hidden" name="clazzId" value="${request.clazzId}">
                    <input type="hidden" name="resolverId" value="${user.id}">
  
                    <label for="selPaymentStatus" class="form-label">Kết quả:</label>
                    <select id="selPaymentStatus" name="status"
                            class="form-select ts-border-grey mb-3"
                            onchange="togglePaymentForm()">
                      <option value="${Status.APPROVED}"><c:out value="${Status.APPROVED.stringValueVie}"/></option>
                      <option value="${Status.DENIED}"><c:out value="${Status.DENIED.stringValueVie}"/></option>
                    </select>
  
                    <!-- PaymentCreateDTO -->
                    <input type="hidden" name="payerId" value="${request.requesterId}">
                    <input type="hidden" name="requestId" value="${request.id}">
                    <input type="hidden" name="verifierId" value="${user.id}">
  
                    <label for="txtAPaymentDesc" class="form-label">Ghi chú:</label>
                    <textarea id="txtAPaymentDesc" name="paymentDesc"
                              minlength="0" maxlength="9999"
                              class="form-control ts-border-grey mb-3" rows="3" style="resize: none;"
                              required="required"></textarea>
  
                    <!-- Approve payment detail -->
                    <div id="divApproved">
                      <label for="selPaymentType" class="form-label">Phương thức thanh toán:</label>
                      <select id="selPaymentType" name="paymentType"
                              class="form-select ts-border-grey mb-3">
                        <option value="${PaymentType.CASH}"><c:out value="${PaymentType.CASH.stringValueVie}"/></option>
                        <option value="${PaymentType.TRANSFER}"><c:out value="${PaymentType.TRANSFER.stringValueVie}"/></option>
                      </select>
    
                      <div class="row">
                        <div class="col-6">
                          <fmt:formatNumber value="${currentPrice.finalPrice}" type="number" var="parsedFinalPrice"/>
                          <label for="txtPaymentAmount" class="form-label">Số tiền đóng:</label>
                          <div class="input-group mb-3">
                            <input id="txtPaymentAmount" name="paymentAmount"
                                   type="text" value="${parsedFinalPrice}"
                                   class="form-control ts-border-grey"
                                   readonly="readonly">
                            <span class="input-group-text ts-border-grey">₫</span>
                          </div>
                            <%-- TODO: tiền thối? --%>
                        </div>
  
                        <div class="col-6">
                          <label for="datetimePaymentAt" class="form-label">Đóng vào lúc:</label>
                          <input id="datetimePaymentAt" name="paymentAt"
                                 type="datetime-local" min="${request.createdAt}" step="1"
                            <%-- max="${semester.startDate}T00:00"--%>
                                 class="form-control ts-border-grey mb-3"
                                 required="required">
                        </div>
                      </div>
                      
                      <label for="filePaymentInfoAdmin" class="form-label">Biên lai chứng từ:</label>
                      <input id="filePaymentInfoAdmin" name="file"
                             type="file" min="${currentPrice.finalPrice}" max="${currentPrice.finalPrice}"
                             class="form-control ts-border-grey mb-3"
                             required="required">
                      <input id="hidPaymentInfoAdmin" name="paymentDocLink"
                             type="hidden">
                    </div>
  
                    <div class="w-100 d-flex justify-content-center">
                      <button type="button" class="btn btn-primary w-45"
                              onclick="updateRequestEnroll('admin')">
                        Lưu
                      </button>
                    </div>
                  </div>
                  
    
<%--                    <!-- Nếu Request chưa được duyệt, thì cho edit -->--%>
<%--                    <script>--%>
<%--                        let hasTransfer = ${not empty request.contentLink};--%>
<%--                        let transferAt = "${not empty request.updatedAt ? request.updatedAt : ''}";--%>

<%--                        let datetimePaymentAt = $("#datetimePaymentAt");--%>

<%--                        if (hasTransfer) {--%>
<%--                            datetimePaymentAt.val(transferAt);--%>
<%--                            $("#selPaymentType").val("${PaymentType.TRANSFER}");--%>
<%--                            $("#selPaymentType option[value='${PaymentType.CASH}']")--%>
<%--                                .prop("disabled", true).prop("hidden", true);--%>
<%--                        } else {--%>
<%--                            let now = new Date();--%>
<%--                            let month = (now.getMonth() + 1).toString().padStart(2, '0');--%>
<%--                            let day = now.getDate().toString().padStart(2, '0');--%>
<%--                            let hour = now.getHours().toString().padStart(2, '0');--%>
<%--                            let minute = now.getMinutes().toString().padStart(2, '0');--%>
<%--                            let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;--%>
<%--                            datetimePaymentAt.val(datetime);--%>
<%--                        }--%>

<%--                        function togglePaymentForm() {--%>
<%--                            let paymentStatus = $("#selPaymentStatus").val();--%>
<%--                            switch (paymentStatus) {--%>
<%--                                case "APPROVED":--%>
<%--                                    showById("divApproved");--%>
<%--                                    enableAllFormElementIn("divApproved");--%>
<%--                                    break;--%>
<%--                                case "DENIED":--%>
<%--                                    hideById("divApproved");--%>
<%--                                    disableAllFormElementIn("divApproved");--%>
<%--                                    break--%>
<%--                            }--%>
<%--                        }--%>
<%--                    </script>--%>
<%--    --%>
<%--                    <!-- Nếu Request đã được duyệt, thì ko cho edit -->--%>
<%--                    <script>--%>
<%--                        let hasTransfer = ${not empty request.contentLink};--%>
<%--                        let transferAt = "${not empty request.updatedAt ? request.updatedAt : ''}";--%>

<%--                        let datetimePaymentAt = $("#datetimePaymentAt");--%>

<%--                        if (hasTransfer) {--%>
<%--                            datetimePaymentAt.val(transferAt);--%>
<%--                            $("#selPaymentType").val("${PaymentType.TRANSFER}");--%>
<%--                            $("#selPaymentType option[value='${PaymentType.CASH}']")--%>
<%--                                .prop("disabled", true).prop("hidden", true);--%>
<%--                        } else {--%>
<%--                            let now = new Date();--%>
<%--                            let month = (now.getMonth() + 1).toString().padStart(2, '0');--%>
<%--                            let day = now.getDate().toString().padStart(2, '0');--%>
<%--                            let hour = now.getHours().toString().padStart(2, '0');--%>
<%--                            let minute = now.getMinutes().toString().padStart(2, '0');--%>
<%--                            let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;--%>
<%--                            datetimePaymentAt.val(datetime);--%>
<%--                        }--%>

<%--                        function togglePaymentForm() {--%>
<%--                            let paymentStatus = $("#selPaymentStatus").val();--%>
<%--                            switch (paymentStatus) {--%>
<%--                                case "APPROVED":--%>
<%--                                    showById("divApproved");--%>
<%--                                    enableAllFormElementIn("divApproved");--%>
<%--                                    break;--%>
<%--                                case "DENIED":--%>
<%--                                    hideById("divApproved");--%>
<%--                                    disableAllFormElementIn("divApproved");--%>
<%--                                    break--%>
<%--                            }--%>
<%--                        }--%>
<%--                    </script>--%>
                </form:form>
              </c:when>
              
              <c:when test="${status eq Status.AWAIT_CONFIRM}">
              </c:when>
              
              <c:when test="${status eq Status.APPROVED}">
              </c:when>
    
              <c:when test="${status eq Status.DENIED}">
    
              </c:when>
            </c:choose>
          </c:if>
        </div>
      </c:if>
    
      <c:if test="${request.requestType.equals(RequestType.CHANGE_CLASS)}">
        <div class="col-sm-12 col-md-6 mb-3">
      
        </div>
      </c:if>
    </div>
    
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    async function updateRequestEnroll(formType) {
        let file;
        let fileURL;
        let form;
        switch (formType) {
            case "student":
                file = $('#filePaymentInfoStudent').prop("files")[0];

                fileURL = await uploadFileToFirebaseAndGetURL(file);
                $("#hidPaymentInfoStudent").val(fileURL);

                form = document.getElementById('formUpdateEnrollStudent');
                form.requestSubmit();
                break;
            case "admin":
                file = $('#filePaymentInfoAdmin').prop("files")[0];

                fileURL = await uploadFileToFirebaseAndGetURL(file);
                $("#hidPaymentInfoAdmin").val(fileURL);

                form = document.getElementById('formUpdateEnrollAdmin');
                form.requestSubmit();
                break;
        }
    }
    
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>