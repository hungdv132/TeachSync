<%@ page import="com.teachsync.utils.enums.RequestType" %>
<%@ page import="com.teachsync.utils.enums.PaymentType" %>
<%@ page import="com.teachsync.utils.enums.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
          Chi tiết đơn
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 px-5 pt-3 mx-2 mb-3">
  <div class="col-12 border-bottom ts-border-blue mb-3">
    <h4 class="mb-1">Mã đơn: <c:out value="${request.id}"/></h4>
    <h5 class="mb-3">Loại đơn: <c:out value="${request.requestType.stringValueVie}"/></h5>
    
    <h5 class="mb-3">Trạng thái: <c:out value="${request.status.stringValueVie}"/></h5>
    
    <h5 class="mb-1">Chi tiết: </h5>
    <p class="mb-3"><c:out value="${request.requestDesc}" escapeXml="false"/></p>
  </div>
  
  <c:set var="status" value="${request.status}"/>
  
  <c:if test="${request.requestType.equals(RequestType.ENROLL)}">
    <!-- Request detail -->
    <div class="col-sm-12 col-md-6 mb-3">
      <div class="row">
        <c:set var="course" value="${request.clazz.course}"/>
        <c:set var="currentPrice" value="${course.currentPrice}"/>
        <c:set var="center" value="${request.clazz.center}"/>
        <c:set var="clazzSchedule" value="${request.clazz.clazzSchedule}"/>
        
        <!-- Center & Room -->
        <h6 class="mb-1">Cơ sở: <c:out value="${center.centerName}"/></h6>
        <p class="mb-1">Địa chỉ: <c:out value="${center.address.addressString}"/></p>
        <p class="mb-3">Phòng: <c:out value="${clazzSchedule.roomName}"/></p>
        
        
        <!-- Course & Clazz -->
        <h6 class="mb-1">Khóa học: <c:out value="${course.courseName}"/></h6>
        <c:if test="${currentPrice.isPromotion}">
          <p class="col-6 mb-sm-1 mb-md-3">
            Giá gốc: <fmt:formatNumber value="${currentPrice.price}" type="currency"/>
          </p>
          <p class="col-6 mb-sm-1 mb-md-3">
            Giảm giá: <fmt:formatNumber value="${currentPrice.promotionAmount}" type="${currentPrice.promotionType.fmtType}"/>
          </p>
        </c:if>
        <p class="col-12 mb-3">
          Cần đóng: <fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/>
        </p>

        <!-- Clazz -->
        <h6 class="mb-1">Lớp: <c:out value="${request.clazz.clazzName}"/></h6>
        <fmt:parseDate value="${clazzSchedule.startDate}" type="date" pattern="yyyy-MM-dd" var="parsedStartDate" />
        <fmt:parseDate value="${clazzSchedule.endDate}" type="date" pattern="yyyy-MM-dd" var="parsedEndDate" />
        <p class="col-6">Bắt đầu: <fmt:formatDate value="${parsedStartDate}" pattern="dd/MM/yyyy"/></p>
        <p class="col-6">Kết thúc: <fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy"/></p>
        <p class="col-6 mb-1">
          Lịch học: <c:out value="${clazzSchedule.scheduleCategory.scheduleName}"/>
        </p>
        <p class="col-6 mb-1">
          Tiết: <c:out value="${clazzSchedule.slot}"/>
        </p>
        <p class="col-6 mb-0">
          Từ: <c:out value="${clazzSchedule.sessionStart}"/>
        </p>
        <p class="col-6 mb-0">
          Đến: <c:out value="${clazzSchedule.sessionEnd}"/>
        </p>
      </div>
    </div>
    
    <c:set var="statusResovled" value="${status.equals(Status.APPROVED) or status.equals(Status.DENIED)}"/>
    <c:set var="statusOngoing" value="${status.equals(Status.PENDING_PAYMENT) or status.equals(Status.AWAIT_CONFIRM)}"/>
    
    <c:if test="${isStudent}">
      <c:if test="${statusOngoing}">
        <!-- Nếu Request chưa được duyệt, thì cho edit -->
        <form action="/edit-request/enroll" method="POST"
              id="formUpdateEnrollStudent" class="col-sm-12 col-md-6 mb-3">
          <input type="hidden" name="id" value="${request.id}">
          
          <div class="row mb-3">
            <div class="col-6">
              <img src="../../../resources/img/QRCode.jpg" alt="QR code"
                   class="rounded-4 border ts-border-teal w-100 h-auto">
            </div>
            <div class="col-6">
              <h6 class="mb-1">Thông tin chuyển khoản: </h6>
              <p class="mb-3">VIETCOMBANK <br>
                              123 456 789 123 456 789 <br>
                              Teach Sync Edu Inc
              </p>
              
              <h6 class="mb-1">Nội dung chuyển khoản: </h6>
              <p class="mb-3"><c:out value="${request.requestName}"/></p>
              
              <h6 class="mb-1">Khoản cần chuyển: </h6>
              <p class="mb-3"><fmt:formatNumber value="${currentPrice.finalPrice}" type="currency"/></p>
            </div>
          </div>
          
          <label class="w-100 overflow-hidden mb-3"><h6 class="mb-1">Chứng từ chuyển khoản: </h6>
            <c:if test="${not empty request.contentLink}">
              <a href="${request.contentLink}" onclick="window.open(this.href, '_blank'); return false;"
                 id="hrefPaymentInfo" class="text-nowrap">
                <c:out value="${request.contentLink}"/>
              </a>
              <button type="button" class="btn btn-warning mt-3"
                      id="btnEditPaymentInfoStudent" onclick="editPaymentInfoStudent()">Chỉnh
              </button>
            </c:if>
            
            <input type="file" class="form-control mt-3" required="required"
                   id="filePaymentInfoStudent" name="file">
          </label>
          
          <input type="hidden" value="${not empty request.contentLink ? request.contentLink : null}"
                 id="hidPaymentInfoStudent" name="paymentInfo">
          
          <div class="w-100 d-flex justify-content-center">
            <button type="button" class="btn btn-primary w-45"
                    id="btnUpdateRequestEnrollStudent"
                    onclick="updateRequestEnroll('student')">Lưu
            </button>
            
            <c:if test="${not empty request.contentLink}">
              <button id="btnCancelEditPaymentInfoStudent" class="btn btn-danger w-45 ms-2 visually-hidden"
                      onclick="cancelEditPaymentInfoStudent()">Hủy
              </button>
            </c:if>
          </div>
          
          <script>
              let hasTransfer = ${not empty request.contentLink};
              if (hasTransfer) {
                  hideById("filePaymentInfoStudent");
                  disableById("filePaymentInfoStudent");

                  hideById("btnUpdateRequestEnrollStudent");
              }

              function editPaymentInfoStudent() {
                  hideById("hrefPaymentInfo");
                  showById("filePaymentInfoStudent");
                  enableById("filePaymentInfoStudent");

                  hideById("btnEditPaymentInfoStudent");
                  showById("btnUpdateRequestEnrollStudent");
                  showById("btnCancelEditPaymentInfoStudent");
              }

              function cancelEditPaymentInfoStudent() {
                  showById("hrefPaymentInfo");
                  hideById("filePaymentInfoStudent");
                  disableById("filePaymentInfoStudent");

                  showById("btnEditPaymentInfoStudent");
                  hideById("btnUpdateRequestEnrollStudent");
                  hideById("btnCancelEditPaymentInfoStudent");
              }
          </script>
        </form>
      </c:if>
      
      <c:if test="${statusResovled}">
        <!-- Nếu Request đã được duyệt, thì ko cho edit -->
      </c:if>
    </c:if>
    
    <c:if test="${isAdmin}">
      <form action="/edit-request/enroll" method="POST"
            id="formUpdateEnrollAdmin" class="col-sm-12 col-md-6 mb-3">
        <input type="hidden" name="id" value="${request.id}">
        
        <c:if test="${not empty request.contentLink}">
          <p class="overflow-hidden">Chứng từ chuyển khoản: <br>
            <a href="${request.contentLink}" onclick="window.open(this.href, '_blank'); return false;"
               class="text-nowrap">
              <c:out value="${request.contentLink}"/>
            </a>
          </p>
        </c:if>
        
        <label class="w-100 mb-3">Kết quả: <br>
          <select id="selPaymentStatus" name="paymentStatus" onchange="togglePaymentForm()">
            <option value="${Status.APPROVED}"><c:out value="${Status.APPROVED.stringValueVie}"/></option>
            <option value="${Status.DENIED}"><c:out value="${Status.DENIED.stringValueVie}"/></option>
          </select>
        </label>
        
        <label class="w-100 mb-3">Ghi chú: <br>
          <textarea id="txtAPaymentDesc" name="paymentDesc" class="w-100" rows="3" style="resize: none"
                    required></textarea>
        </label>
        
        <!-- Approve payment detail -->
        <div class="row" id="divApproved">
          <label class="col-12 mb-3">Phương thức thanh toán: <br>
            <select id="selPaymentType" name="paymentType" class="w-100">
              <option value="${PaymentType.CASH}"><c:out value="${PaymentType.CASH.stringValueVie}"/></option>
              <option value="${PaymentType.TRANSFER}"><c:out value="${PaymentType.TRANSFER.stringValueVie}"/></option>
            </select>
          </label>
          
          <label class="col-6 mb-3">Số tiền đóng: <br>
            <span class="input-vnd right">
          <input type="number" min="${currentPrice.finalPrice}" max="${currentPrice.finalPrice}" required
                 id="numPaymentAmount" name="paymentAmount" class="w-100" value="${currentPrice.finalPrice}">
        </span>
          </label>
          
          <label class="col-6 mb-3">Đóng vào lúc: <br>
            <input type="datetime-local" min="${request.createdAt}" max="${semester.startDate}T00:00:00"
                   id="datetimePaymentAt" name="paymentAt" class="w-100" step="1" required>
          </label>
          
          <label class="col-12 mb-3">Biên lai chứng từ: <br>
            <input type="file" min="${currentPrice.finalPrice}" max="${currentPrice.finalPrice}"
                   id="filePaymentInfoAdmin" name="file" class="w-100" required>
            <input type="hidden" name="paymentInfo" id="hidPaymentInfoAdmin">
          </label>
        </div>
        
        <div class="w-100 d-flex justify-content-center">
          <button type="button" class="btn btn-primary w-50" onclick="updateRequestEnroll('admin')">Lưu</button>
        </div>
        
        <c:if test="${statusOngoing}">
          <!-- Nếu Request chưa được duyệt, thì cho edit -->
          <script>
              let hasTransfer = ${not empty request.contentLink};
              let transferAt = "${not empty request.updatedAt ? request.updatedAt : ''}";

              let datetimePaymentAt = $("#datetimePaymentAt");

              if (hasTransfer) {
                  datetimePaymentAt.val(transferAt);
                  $("#selPaymentType").val("${PaymentType.TRANSFER}");
                  $("#selPaymentType option[value='${PaymentType.CASH}']")
                      .prop("disabled", true).prop("hidden", true);
              } else {
                  let now = new Date();
                  let month = (now.getMonth() + 1).toString().padStart(2, '0');
                  let day = now.getDate().toString().padStart(2, '0');
                  let hour = now.getHours().toString().padStart(2, '0');
                  let minute = now.getMinutes().toString().padStart(2, '0');
                  let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;
                  datetimePaymentAt.val(datetime);
              }

              function togglePaymentForm() {
                  let paymentStatus = $("#selPaymentStatus").val();
                  switch (paymentStatus) {
                      case "APPROVED":
                          showById("divApproved");
                          enableAllFormElementIn("divApproved");
                          break;
                      case "DENIED":
                          hideById("divApproved");
                          disableAllFormElementIn("divApproved");
                          break
                  }
              }
          </script>
        </c:if>
        
        <c:if test="${statusResovled}">
          <!-- Nếu Request đã được duyệt, thì ko cho edit -->
          <script>
              let hasTransfer = ${not empty request.contentLink};
              let transferAt = "${not empty request.updatedAt ? request.updatedAt : ''}";

              let datetimePaymentAt = $("#datetimePaymentAt");

              if (hasTransfer) {
                  datetimePaymentAt.val(transferAt);
                  $("#selPaymentType").val("${PaymentType.TRANSFER}");
                  $("#selPaymentType option[value='${PaymentType.CASH}']")
                      .prop("disabled", true).prop("hidden", true);
              } else {
                  let now = new Date();
                  let month = (now.getMonth() + 1).toString().padStart(2, '0');
                  let day = now.getDate().toString().padStart(2, '0');
                  let hour = now.getHours().toString().padStart(2, '0');
                  let minute = now.getMinutes().toString().padStart(2, '0');
                  let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;
                  datetimePaymentAt.val(datetime);
              }

              function togglePaymentForm() {
                  let paymentStatus = $("#selPaymentStatus").val();
                  switch (paymentStatus) {
                      case "APPROVED":
                          showById("divApproved");
                          enableAllFormElementIn("divApproved");
                          break;
                      case "DENIED":
                          hideById("divApproved");
                          disableAllFormElementIn("divApproved");
                          break
                  }
              }
          </script>
        </c:if>
      </form>
    </c:if>
  </c:if>
  
  <c:if test="${request.requestType.equals(RequestType.CHANGE_CLASS)}">
    <div class="col-sm-12 col-md-6 mb-3">
    
    </div>
  </c:if>
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