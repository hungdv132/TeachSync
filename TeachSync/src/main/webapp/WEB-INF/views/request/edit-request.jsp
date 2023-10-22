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
      
      <!-- Form enroll -->
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
                Lịch học: <c:out value="${clazzSchedule.scheduleCategory.categoryName}"/>
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
          <!-- Student -->
          <c:choose>
            <%-- Hadn't online transfer --%>
            <c:when test="${status eq Status.PENDING_PAYMENT}">
              <!-- Student -->
              <c:if test="${isStudent}">
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
                      <h6>Chứng từ chuyển khoản: </h6>
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
              </c:if>

              <!-- Admin -->
              <c:if test="${isAdmin}">
                <!-- Nếu Request chưa được duyệt, thì cho edit -->
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
                              class="form-select ts-border-grey mb-3"
                              onchange="toggleTransferInput()">
                        <option value="${PaymentType.CASH}"><c:out value="${PaymentType.CASH.stringValueVie}"/></option>
                        <option value="${PaymentType.TRANSFER}"><c:out value="${PaymentType.TRANSFER.stringValueVie}"/></option>
                      </select>
      
                      <div class="row">
                        <div class="col-6">
                          <label for="txtPaymentAmount" class="form-label">Số tiền đóng:</label>
                          <div class="input-group mb-3">
                            <input id="txtPaymentAmount"
                                   type="text" value="<fmt:formatNumber value="${currentPrice.finalPrice}" type="number"/>"
                                   class="form-control ts-border-grey"
                                   readonly="readonly">
                            <span class="input-group-text ts-border-grey">₫</span>
                          </div>
                            <%-- TODO: tiền thối? --%>
                          <input type="hidden" name="paymentAmount" value="${currentPrice.finalPrice}">
                        </div>
        
                        <div class="col-6">
                          <label for="datetimePaymentAt" class="form-label">Đóng vào lúc:</label>
                          <input id="datetimePaymentAt" name="paymentAt"
                                 type="datetime-local" min="${request.createdAt}" step="1"
                            <%-- max="${semester.startDate}T00:00"--%>
                                 class="form-control ts-border-grey mb-3"
                                 readonly="readonly">
                        </div>
                      </div>
  
                      <div id="divTransferProof" class="visually-hidden">
                        <label for="filePaymentInfoStudent" class="form-label">Chứng từ chuyển khoản:</label>
                        <input id="filePaymentInfoStudent" name="file"
                               type="file"
                               class="form-control ts-border-grey mb-3"
                               required="required" disabled="disabled">
                        <input id="hidPaymentInfoStudent" name="contentLink"
                               type="hidden"
                               disabled="disabled">
                      </div>
                      
                      <label for="filePaymentInfoAdmin" class="form-label">Chứng từ biên lai:</label>
                      <input id="filePaymentInfoAdmin" name="file"
                             type="file"
                             class="form-control ts-border-grey mb-3"
                             required="required">
                      <input id="hidPaymentInfoAdmin" name="paymentDocLink"
                             type="hidden">
                    </div>
    
                    <div class="w-100 d-flex justify-content-center">
                      <button type="button" class="btn btn-primary w-45">
                        Lưu
                      </button>
                    </div>
                  </div>
                </form:form>

                <script id="script1">
                    let now = new Date();
                    let month = (now.getMonth() + 1).toString().padStart(2, '0');
                    let day = now.getDate().toString().padStart(2, '0');
                    let hour = now.getHours().toString().padStart(2, '0');
                    let minute = now.getMinutes().toString().padStart(2, '0');
                    let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;

                    $("#datetimePaymentAt").val(datetime);

                    $("#script1").remove();
                </script>

                <script>
                    $("#formUpdateEnrollAdmin").on("submit", function (e) {
                        e.preventDefault();

                        let file = $("#filePaymentInfoAdmin").prop("files")[0];

                        uploadFileToFirebaseAndGetURL(file).then( function (fileURL) {
                            $("#hidPaymentInfoAdmin").val(fileURL);

                            let transferType = $("#selPaymentType");
                            if (transferType === `${PaymentType.TRANSFER}`) {
                                let file = $("#filePaymentInfoStudent").prop("files")[0];
                                
                                uploadFileToFirebaseAndGetURL(file).then( function (fileURL) {
                                    $("#hidPaymentInfoStudent").val(fileURL);

                                    $("#formUpdateEnrollAdmin")[0].submit();
                                });
                            }
                            
                            $("#formUpdateEnrollAdmin")[0].submit();
                        })
                    });

                    function togglePaymentForm() {
                        let paymentStatus = $("#selPaymentStatus").val();

                        switch (paymentStatus) {
                            case "${Status.APPROVED}":
                                showById("divApproved");
                                enableAllFormElementIn("divApproved");
                                break;
                                
                            default:
                            case "${Status.DENIED}":
                                hideById("divApproved");
                                disableAllFormElementIn("divApproved");
                                break
                        }
                    }
                    
                    function toggleTransferInput() {
                        let transferType = $("#selPaymentType");
                        
                        if (transferType === `${PaymentType.TRANSFER}`) {
                            showById("divTransferProof");
                            enableAllFormElementIn("divTransferProof");
                        } else {
                            hideById("divTransferProof");
                            disableAllFormElementIn("divTransferProof");
                        }
                    }
                </script>
              </c:if>
            </c:when>
          
            <%-- Had online transfer --%>
            <c:when test="${status eq Status.AWAIT_CONFIRM}">
              <!-- Student -->
              <c:if test="${isStudent}">
                <!-- Nếu Request chưa được duyệt, thì cho edit -->
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
                      <h6>Chứng từ chuyển khoản: </h6>
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
              </c:if>

              <!-- Admin -->
              <c:if test="${isAdmin}">
                <!-- Nếu Request chưa được duyệt, thì cho edit -->
                <!-- Đã chuyển khoản, chờ xét duyệt -->
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
      
                    <h6 class="card-text">Chứng từ chuyển khoản: </h6>
                    <a href="${request.contentLink}"
                       class="card-text text-nowrap"
                       onclick="window.open(this.href, '_blank'); return false;">
                      <c:out value="${request.contentLink}"/>
                    </a>
      
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
                    <%--<option value="${PaymentType.CASH}"><c:out value="${PaymentType.CASH.stringValueVie}"/></option>--%>
                        <option value="${PaymentType.TRANSFER}"><c:out value="${PaymentType.TRANSFER.stringValueVie}"/></option>
                      </select>
        
                      <div class="row">
                        <div class="col-6">
                          <label for="txtPaymentAmount" class="form-label">Số tiền đóng:</label>
                          <div class="input-group mb-3">
                            <input id="txtPaymentAmount"
                                   type="text" value="<fmt:formatNumber value="${currentPrice.finalPrice}" type="number"/>"
                                   class="form-control ts-border-grey"
                                   readonly="readonly">
                            <span class="input-group-text ts-border-grey">₫</span>
                          </div>
                            <%-- TODO: tiền thối? --%>
                          <input name="paymentAmount"
                                 type="hidden" value="${currentPrice.finalPrice}">
                        </div>
          
                        <div class="col-6">
                          <fmt:parseDate value="${request.createdAt}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedPaymentAt"/>
                          <label for="datetimePaymentAt" class="form-label">Đóng vào lúc:</label>
                          <input id="datetimePaymentAt"
                                 type="text" value="<fmt:formatDate value="${parsedPaymentAt}" pattern="dd/MM/yyyy HH:mm"/>"
                                 class="form-control ts-border-grey mb-3"
                                 readonly="readonly">
                          <input name="paymentAt"
                                 type="hidden" value="${request.createdAt}">
                        </div>
                      </div>
        
                      <label for="filePaymentInfoAdmin" class="form-label">Chứng từ biên lai:</label>
                      <input id="filePaymentInfoAdmin" name="file"
                             type="file"
                             class="form-control ts-border-grey mb-3"
                             required="required">
                      <input id="hidPaymentInfoAdmin" name="paymentDocLink"
                             type="hidden">
                    </div>
      
                    <div class="w-100 d-flex justify-content-center">
                      <button type="button" class="btn btn-primary w-45">
                        Lưu
                      </button>
                    </div>
                  </div>
                </form:form>
  
                <script id="script1">
                    let requestAt = `<c:out value="${request.updatedAt}"/>`;
  
                    /*
                    let now = new Date();
                    let month = (now.getMonth() + 1).toString().padStart(2, '0');
                    let day = now.getDate().toString().padStart(2, '0');
                    let hour = now.getHours().toString().padStart(2, '0');
                    let minute = now.getMinutes().toString().padStart(2, '0');
                    let datetime = now.getFullYear() + '-' + month + '-' + day + 'T' + hour + ':' + minute;
                    */
  
                    $("#datetimePaymentAt").val(requestAt);
  
                    $("#script1").remove();
                </script>
  
                <script>
                  $("#formUpdateEnrollAdmin").on("submit", function (e) {
                      e.preventDefault();

                      let file = $("#filePaymentInfoAdmin").prop("files")[0];

                      uploadFileToFirebaseAndGetURL(file).then( function (fileURL) {
                          $("#hidPaymentInfoAdmin").val(fileURL);

                          $("#formUpdateEnrollAdmin")[0].submit();
                      })
                  });

                  function togglePaymentForm() {
                      let paymentStatus = $("#selPaymentStatus").val();

                      switch (paymentStatus) {
                          case "${Status.APPROVED}":
                              showById("divApproved");
                              enableAllFormElementIn("divApproved");
                              break;
                          case "${Status.DENIED}":
                              hideById("divApproved");
                              disableAllFormElementIn("divApproved");
                              break
                      }
                  }
              </script>
              </c:if>
            </c:when>

            <c:when test="${status eq Status.APPROVED}">
              <c:set value="${request.payment}" var="payment"/>
  
              <div class="card border-success">
                <div class="card-header border-success">
                  <h6 class="card-title mb-0">
                    Kết quả duyệt đơn
                  </h6>
                </div>
    
                <div class="card-body">
                  <h6 class="card-text">
                    Trạng thái: <span class="text-success"><c:out value="${request.status.stringValueVie}"/></span>
                  </h6>
      
                  <c:if test="${not empty request.contentLink}">
                    <p class="card-text">Chứng từ chuyển khoản: </p>
                    <a id="aPaymentInfo"
                       href="${request.contentLink}"
                       class="card-text text-nowrap mb-3"
                       onclick="window.open(this.href, '_blank'); return false;">
                      <c:out value="${request.contentLink}"/>
                    </a>
                  </c:if>
      
                  <div class="row mb-3">
                    <div class="col-6">
                      <label for="txtPaymentAmount" class="form-label">Số tiền đóng:</label>
                      <div class="input-group">
                        <input id="txtPaymentAmount"
                               type="text" value="<fmt:formatNumber value="${currentPrice.finalPrice}" type="number"/>"
                               class="form-control ts-border-grey"
                               disabled="disabled" readonly="readonly">
                        <span class="input-group-text ts-border-grey">₫</span>
                      </div>
                    </div>
        
                    <div class="col-6">
                      <fmt:parseDate value="${payment.paymentAt}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedPaymentAt"/>
                      <label for="txtPaymentAt" class="form-label">Đóng vào lúc:</label>
                      <input id="txtPaymentAt"
                             class="form-control ts-border-grey"
                             type="text" value="<fmt:formatDate value="${parsedPaymentAt}" pattern="dd/MM/yyyy HH:mm"/>"
                             disabled="disabled" readonly="readonly">
                    </div>
                  </div>
      
                  <label for="txtAPaymentDesc" class="form-label">Ghi chú:</label>
                  <textarea id="txtAPaymentDesc" name="paymentDesc"
                            minlength="0" maxlength="9999"
                            class="form-control ts-border-grey mb-3" rows="3" style="resize: none;"
                            disabled="disabled" readonly="readonly"></textarea>
      
                  <p class="card-text">Chứng từ biên lai: </p>
                  <a href="${request.contentLink}"
                     class="card-text text-nowrap mb-3"
                     onclick="window.open(this.href, '_blank'); return false;">
                    <c:out value="${payment.paymentDocLink}"/>
                  </a>
      
                  <div class="row">
                    <div class="col-6">
                      <label for="txtResolvedBy" class="form-label">Duyệt bởi:</label>
                      <input id="txtResolvedBy"
                             class="form-control ts-border-grey"
                             type="text" value="${request.resolverFullName}"
                             disabled="disabled" readonly="readonly">
                    </div>
        
                    <div class="col-6">
                      <fmt:parseDate value="${payment.createdAt}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedResolvedAt"/>
                      <label for="txtResolvedAt" class="form-label">Duyệt vào lúc:</label>
                      <input id="txtResolvedAt"
                             class="form-control ts-border-grey"
                             type="text" value="<fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy HH:mm"/>"
                             disabled="disabled" readonly="readonly">
                    </div>
                  </div>
                </div>
              </div>
            </c:when>

            <c:when test="${status eq Status.DENIED}">
              <c:set value="${request.payment}" var="payment"/>
  
              <div class="card border-danger">
                <div class="card-header border-danger">
                  <h6 class="card-title mb-0">
                    Kết quả duyệt đơn
                  </h6>
                </div>
    
                <div class="card-body">
                  <h6 class="card-text">
                    Trạng thái: <span class="text-success"><c:out value="${request.status.stringValueVie}"/></span>
                  </h6>
      
                  <c:if test="${not empty request.contentLink}">
                    <p class="card-text">Chứng từ chuyển khoản: </p>
                    <a id="aPaymentInfo"
                       href="${request.contentLink}"
                       class="card-text text-nowrap mb-3"
                       onclick="window.open(this.href, '_blank'); return false;">
                      <c:out value="${request.contentLink}"/>
                    </a>
                  </c:if>
      
                  <div class="row mb-3">
                    <div class="col-6">
                      <label for="txtPaymentAmount" class="form-label">Số tiền đóng:</label>
                      <div class="input-group">
                        <input id="txtPaymentAmount"
                               type="text" value="<fmt:formatNumber value="${currentPrice.finalPrice}" type="number"/>"
                               class="form-control ts-border-grey"
                               disabled="disabled" readonly="readonly">
                        <span class="input-group-text ts-border-grey">₫</span>
                      </div>
                    </div>
        
                    <div class="col-6">
                      <fmt:parseDate value="${payment.paymentAt}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedPaymentAt"/>
                      <label for="txtPaymentAt" class="form-label">Đóng vào lúc:</label>
                      <input id="txtPaymentAt"
                             class="form-control ts-border-grey"
                             type="text" value="<fmt:formatDate value="${parsedPaymentAt}" pattern="dd/MM/yyyy HH:mm"/>"
                             disabled="disabled" readonly="readonly">
                    </div>
                  </div>
      
                  <label for="txtAPaymentDesc" class="form-label">Ghi chú:</label>
                  <textarea id="txtAPaymentDesc" name="paymentDesc"
                            minlength="0" maxlength="9999"
                            class="form-control ts-border-grey mb-3" rows="3" style="resize: none;"
                            disabled="disabled" readonly="readonly"><c:out value="${payment.paymentDesc}"/></textarea>
      
                  <p class="card-text">Chứng từ biên lai: </p>
                  <a href="${request.contentLink}"
                     class="card-text text-nowrap mb-3"
                     onclick="window.open(this.href, '_blank'); return false;">
                    <c:out value="${payment.paymentDocLink}"/>
                  </a>
      
                  <div class="row">
                    <div class="col-6">
                      <label for="txtResolvedBy" class="form-label">Duyệt bởi:</label>
                      <input id="txtResolvedBy"
                             class="form-control ts-border-grey"
                             type="text" value="${request.resolverFullName}"
                             disabled="disabled" readonly="readonly">
                    </div>
        
                    <div class="col-6">
                      <fmt:parseDate value="${payment.createdAt}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedResolvedAt"/>
                      <label for="txtResolvedAt" class="form-label">Duyệt vào lúc:</label>
                      <input id="txtResolvedAt"
                             class="form-control ts-border-grey"
                             type="text" value="<fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy HH:mm"/>"
                             disabled="disabled" readonly="readonly">
                    </div>
                  </div>
                </div>
              </div>
            </c:when>
          </c:choose>
        </div>
      </c:if>
      
      
      <!-- Form change class -->
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
    var mess = `<c:out value="${mess}"/>`;
    if (mess != '') {
        alert(mess);
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>