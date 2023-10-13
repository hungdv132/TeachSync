<%@ page import="com.teachsync.utils.enums.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Sửa khóa học</title>
  
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
          Sửa khóa học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <%--@elvariable id="updateDTO" type="com.teachsync.dtos.course.CourseUpdateDTO"--%>
    <form:form id="form"
               modelAttribute="updateDTO" action="/edit-course" method="POST"
               cssClass="row">
      <input type="hidden" name="id" value="${course.id}">
      
      <h4>Sửa khóa học</h4>
      <br>
      
      <!-- Course Img -->
      <div id="divCourseImg"
          class="col-sm-12 col-md-3 mb-3">
        <label for="fileImg" class="form-label">Ảnh khóa học:</label>
        <img id="imgCourseImg"
             src="${empty course.courseImg ? '../../../resources/img/no-img.jpg' : course.courseImg}" alt="courseImg"
             class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
    
        <input id="fileImg" name="img"
               type="file" accept="image/*"
               class="form-control ts-border-grey"
               onchange="updateImgFromInput('fileImg', 'imgCourseImg', 0.75)">
        <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
        <input id="hidCourseImg" name="courseImg"
               type="hidden" value="${empty course.courseImg ? '../../../resources/img/no-img.jpg' : course.courseImg}">
      </div>

      
      <!-- Course detail -->
      <div class="col-sm-12 col-md-9">
        <!-- Course status -->
        <div class="row">
          <div class="col-12">
            <label for="selStatus" class="form-label">Trạng thái:</label>
            <select id="selStatus" name="status"
                    class="form-select ts-border-grey mb-3"
                    onchange="restrictByStatus()">
              <option value="${Status.DESIGNING}">${Status.DESIGNING.stringValueVie}</option>
              <option value="${Status.AWAIT_REVIEW}">${Status.AWAIT_REVIEW.stringValueVie}</option>
      
              <option value="${Status.OPENED}">${Status.OPENED.stringValueVie}</option>
              <option value="${Status.CLOSED}">${Status.CLOSED.stringValueVie}</option>
            </select>
            
            <p id="pStatusDesc" class="text-danger ts-txt-bold visually-hidden"></p>
          </div>
        </div>
        
        <!-- Course Alias, Name, Desc -->
        <div id="divCourseTextDetail"
             class="row">
          <!-- Course Alias -->
          <div class="col-4 mb-3">
            <label for="txtAlias" class="form-label">Mã khóa học:</label>
            <input id="txtAlias" name="courseAlias"
                   type="text" minlength="1" maxlength="10" value="${course.courseAlias}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
    
          <!-- Course Name -->
          <div class="col-8 mb-3">
            <label for="txtName" class="form-label">Tên khóa học:</label>
            <input id="txtName" name="courseName"
                   type="text" minlength="1" maxlength="45" value="${course.courseName}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
    
          <!-- Course Desc -->
          <div class="col-12 mb-3">
            <label for="txtADesc" class="form-label">Miêu tả về khóa học:</label>
            <textarea id="txtADesc" name="courseDesc"
                      minlength="0" maxlength="9999"
                      class="form-control ts-border-grey" rows="3" style="resize: none"><c:out value="${course.courseDesc}"/></textarea>
          </div>
        </div>
        
        <!-- Course numSession, minScore, minAttendant -->
        <div id="divCourseNumberDetail"
             class="row">
          <!-- Course numSession -->
          <div class="col-sm-12 col-md-4 mb-3">
            <label for="numNumSession" class="form-label">Số tiết học:</label>
            <input id="numNumSession" name="numSession"
                   type="number" min="1" max="100" step="1" value="${course.numSession}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
    
          <!-- Course minScore -->
          <div class="col-sm-12 col-md-4 mb-3">
            <label for="numMinScore" class="form-label">Điểm tối thiểu:</label>
            <input id="numMinScore" name="minScore"
                   type="number" min="0" max="10" step=".01" value="${course.minScore}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
    
          <!-- Course minAttendant -->
          <div class="col-sm-12 col-md-4 mb-3">
            <label for="numMinAttendant" class="form-label">Điểm danh tối thiểu:</label>
            <div class="input-group">
              <input id="numMinAttendant" name="minAttendant"
                     type="number" min="0" max="100" step=".01" value="${course.minAttendant}"
                     class="form-control ts-border-grey"
                     required="required">
              <span class="input-group-text ts-border-grey">%</span>
            </div>
          </div>
        </div>
        
        <!-- Course price, isPromotion, (promotionAmount, finalPrice, promotionDesc) -->
        <div id="divCoursePrice"
             class="row">
          <div class="col-sm-12 col-md-4 mb-3">
            <!-- Course price -->
            <label for="numPrice" class="form-label">Giá khóa học:</label>
            <div class="input-group">
              <input id="numPrice" name="price.price"
                     type="number" min="1000" max="99999000" step="100" value="${course.currentPrice.price}"
                     class="form-control ts-border-grey"
                     required="required"
                     oninput="calculateFinalPrice(); updateInputPromotionAmountMax()">
              <span class="input-group-text ts-border-grey">₫</span>
      
              <!-- Course isPromotion -->
              <div class="input-group-text ts-border-grey">
                <input id="chkIsPromotion" name="price.isPromotion"
                       type="checkbox" value="true"
                       class="form-check-input mt-0"
                       onchange="togglePromotion()">
                <label for="chkIsPromotion" class="form-check-label">&nbsp;Giảm giá</label>
              </div>
            </div>
          </div>
          
          <div class="col-sm-12 col-md-4 mb-3 visually-hidden" id="divPromotionAmount">
            <!-- Course promotionAmount -->
            <label class="form-label" for="numPromotionAmount">Giảm:</label>
            <div class="input-group">
              <input id="numPromotionAmount" name="price.promotionAmount"
                     type="number" min="0.01" max="100" step=".01"
                     class="form-control ts-border-grey" style="width: 70%;"
                     disabled="disabled" required="required"
                     oninput="calculateFinalPrice()" onchange="calculateFinalPrice()">
      
              <!-- Course promotionType -->
              <select id="selPromotionType" name="price.promotionType"
                      class="form-select ts-border-grey px-2" style="width: 30%;"
                      disabled="disabled"
                      onchange="changeInputPromotionAmountSpec()">
                <option value="PERCENT">%</option>
                <option value="AMOUNT">₫</option>
              </select>
            </div>
          </div>
  
          <!-- Course finalPrice -->
          <div class="col-sm-12 col-md-4 mb-3 visually-hidden" id="divPromotionFinal">
            <label class="form-label" for="txtFinalPrice">Giá sau giảm:</label>
            <div class="input-group">
              <input id="txtFinalPrice"
                     type="text" value="${course.currentPrice.finalPrice}"
                     class="form-control ts-border-grey"
                     disabled="disabled" readonly="readonly">
              <span class="input-group-text ts-border-grey">₫</span>
            </div>
          </div>

          <!-- Course promotionDesc -->
          <div class="col-12 mb-3 visually-hidden" id="divPromotionDesc">
            <label for="txtAPromotionDesc" class="form-label">Chi tiết Khuyến mãi:</label>
            <textarea id="txtAPromotionDesc" name="price.promotionDesc"
                      minlength="0" maxlength="9999"
                      class="form-control ts-border-grey" rows="3" style="resize: none;"
                      disabled="disabled"></textarea>
          </div>
          
        </div>
      </div>

      <!-- Submit button -->
      <div class="col-12 text-center">
        <button id="btnSubmit"
                type="submit"
                class="btn btn-primary w-50 mb-3">
          Gửi
        </button>
      </div>
    
    </form:form>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
  <%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->

<!-- ================================================== Script ===================================================== -->
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }
</script>

<script>
    function updateInputPromotionAmountMax() {
        let numPromotionAmount = $("#numPromotionAmount");

        if ($("#selPromotionType").val() === 'AMOUNT') {
            let price = $("#numPrice").val();
            numPromotionAmount.attr({
                'max': price,
                'placeholder': '0 - ' + price
            });
        }
    }

    function changeInputPromotionAmountSpec() {
        let numPromotionAmount = $("#numPromotionAmount");

        switch ($("#selPromotionType").val()) {
            case 'AMOUNT':
                let price = $("#numPrice").val();
                numPromotionAmount
                    .attr({
                        'min': "100",
                        'max': price,
                        'step': '100',
                        'placeholder' : '0 - ' + price})
                    .val('100');
                break;

            case 'PERCENT':
                numPromotionAmount
                    .attr({
                        'min': '0.01',
                        'max': '100',
                        'step': '.01',
                        'placeholder' : '0% - 100%'})
                    .val('0.01');
                break;
        }

        calculateFinalPrice();
    }

    function calculateFinalPrice() {
        let price = $("#numPrice").val();
        let promotionAmount = $("#numPromotionAmount").val();
        let finalPrice = 0;

        switch ($("#selPromotionType").val()) {
            case 'AMOUNT':
                finalPrice = price - promotionAmount;
                break;

            case 'PERCENT':
                finalPrice = price - (price * (promotionAmount / 100));
                let roundedFinalPrice = Math.round(finalPrice);
                finalPrice = Math.ceil(roundedFinalPrice / 100) * 100;
                break;
        }

        $("#txtFinalPrice").val(finalPrice);
    }

    function togglePromotion() {
        if ($("#chkIsPromotion").is(":checked")) {
            showById("divPromotionAmount");
            enableAllFormElementIn("divPromotionAmount");
            
            showById("divPromotionDesc");
            enableAllFormElementIn("divPromotionDesc");

            showById("divPromotionFinal");

            calculateFinalPrice();
        } else {
            hideById("divPromotionAmount");
            disableAllFormElementIn("divPromotionAmount");

            hideById("divPromotionDesc");
            disableAllFormElementIn("divPromotionDesc");

            hideById("divPromotionFinal");
        }
    }
    
    const initialStatus = "${course.status}";
    
    function restrictByStatus() {
        let pStatusDesc = $("#pStatusDesc");
        let status = $("#selStatus").val();
        
        /* Clear content */
        pStatusDesc.text("").addClass("visually-hidden");;

        console.log("init: " +initialStatus);
        console.log("new: " +status);
        
        if (status !== initialStatus) {
            /* No edit when changing status */
            disableAllFormElementIn("divCourseImg");
            disableAllFormElementIn("divCourseTextDetail");
            disableAllFormElementIn("divCourseNumberDetail");
            disableAllFormElementIn("divCoursePrice");

            pStatusDesc.text("Khi chuyển đổi trạng thái không được phép sửa đổi các thông tin khác.")
                .removeClass("visually-hidden");
            
            if (initialStatus === '${Status.AWAIT_REVIEW}') {
                if (status === '${Status.DESIGNING}') {
                    /* Denied */
                    pStatusDesc.append("<br>")
                        .append("Xác nhận CHƯA hoàn thành thiết kế?")
                        .removeClass("visually-hidden");
                }
                
                if (status === '${Status.OPENED}') {
                    /* Accept */
                    pStatusDesc.append("<br>")
                        .append("Xác nhận ĐÃ hoàn thành thiết kế? " +
                            "Một khi đã hoàn thành, hệ thống sẽ khóa khả năng sửa đổi các thông tin của khóa học.")
                        .removeClass("visually-hidden");
                }
            }
        } else {
            switch (status) {
                case "${Status.DESIGNING}":
                    /* All editable */
                    enableAllFormElementIn("divCourseImg");
                    enableAllFormElementIn("divCourseTextDetail");
                    enableAllFormElementIn("divCourseNumberDetail");
                    enableAllFormElementIn("divCoursePrice");
                    break;
                case "${Status.AWAIT_REVIEW}":
                    /* No edit, period */
                    pStatusDesc.text("Khi đang chờ xét duyệt không được phép sửa đổi các thông tin khác.")
                        .removeClass("visually-hidden");

                    disableAllFormElementIn("divCourseImg");
                    disableAllFormElementIn("divCourseTextDetail");
                    disableAllFormElementIn("divCourseNumberDetail");
                    disableAllFormElementIn("divCoursePrice");
                    break;
                case "${Status.OPENED}":
                    /* Edit price and img */
                    pStatusDesc.text("Chỉ được phép sửa đổi ảnh và giá của khóa học.")
                        .removeClass("visually-hidden");
                    
                    enableAllFormElementIn("divCourseImg");
                    disableAllFormElementIn("divCourseTextDetail");
                    disableAllFormElementIn("divCourseNumberDetail");
                    enableAllFormElementIn("divCoursePrice");
                    break;
                case "${Status.CLOSED}":
                    /* Edit price and img */
                    pStatusDesc.text("Chỉ được phép sửa đổi ảnh và giá của khóa học.")
                        .removeClass("visually-hidden");
                    
                    enableAllFormElementIn("divCourseImg");
                    disableAllFormElementIn("divCourseTextDetail");
                    disableAllFormElementIn("divCourseNumberDetail");
                    enableAllFormElementIn("divCoursePrice");
                    break;
                    
                default:
                    /* Where did you get this status from? */
                    disableAllFormElementIn("divCourseImg");
                    disableAllFormElementIn("divCourseTextDetail");
                    disableAllFormElementIn("divCourseNumberDetail");
                    disableAllFormElementIn("divCoursePrice");
                    break;
            }
        }
    }
</script>

<script>
    /* Validate input */
    // Select all form elements with the 'required' attribute
    const requiredElements = $('#form :input[required]');
    // Iterate through each required element
    requiredElements.each(function () {
        const element = $(this);
        if (element.value === '') {
            // Set an initial custom validity message for required input in VN
            element[0].setCustomValidity(requiredErrorMsg);
        }
    });

    /* courseAlias */
    let txtAlias = document.getElementById("txtAlias");
    txtAlias.addEventListener("input", function () {
        validateTextInput(
            txtAlias, txtAlias.minLength, txtAlias.maxLength,
            ["required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"]);

    });

    /* courseName */
    let txtName = document.getElementById("txtName");
    txtName.addEventListener("input", function () {
        validateTextInput(
            txtName, txtName.minLength, txtName.maxLength,
            ["required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"]);

    });

    /* courseDesc */
    let txtADesc = document.getElementById("txtADesc");
    txtADesc.addEventListener("input", function () {
        validateTextInput(
            txtADesc, 1, txtADesc.maxLength,
            ["nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"]);
    });

    /* numSession */
    let numNumSession = document.getElementById("numNumSession");
    numNumSession.addEventListener("input", function () {
        validateNumberInput(
            numNumSession, Number(numNumSession.min), Number(numNumSession.max), Number(numNumSession.step),
            ["required", "min", "max", "step"]);
    });

    /* numMinScore */
    let numMinScore = document.getElementById("numMinScore");
    numMinScore.addEventListener("input", function () {
        validateNumberInput(
            numMinScore, Number(numMinScore.min), Number(numMinScore.max), Number(numMinScore.step),
            ["required", "min", "max", "step"]);
    });

    /* numMinScore */
    let numMinAttendant = document.getElementById("numMinAttendant");
    numMinAttendant.addEventListener("input", function () {
        validateNumberInput(
            numMinAttendant, Number(numMinAttendant.min), Number(numMinAttendant.max), Number(numMinAttendant.step),
            ["required", "min", "max", "step"]);
    });

    /* numPrice */
    let numPrice = document.getElementById("numPrice");
    numPrice.addEventListener("input", function () {
        validateNumberInput(
            numPrice, Number(numPrice.min), Number(numPrice.max), Number(numPrice.step),
            ["required", "min", "max", "step"]);
    });

    /* numPromotionAmount */
    let numPromotionAmount = document.getElementById("numPromotionAmount");
    numPromotionAmount.addEventListener("input", function () {
        validateNumberInput(
            numPromotionAmount, Number(numPromotionAmount.min), Number(numPromotionAmount.max), Number(numPromotionAmount.step),
            ["required", "min", "max", "step"]);
    });

    /* course promotionDesc */
    let txtAPromotionDesc = document.getElementById("txtAPromotionDesc");
    txtADesc.addEventListener("input", function () {
        validateTextInput(
            txtAPromotionDesc, 1, txtAPromotionDesc.maxLength,
            ["nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"]);
    });

    $("#form").on("submit", async function (event) {
        let file = $('#fileImg').prop("files")[0];

        let imgURL = await uploadImageFileToFirebaseAndGetURL(file);

        $("#hidCourseImg").val(imgURL);
    });
</script>

<script id="script1">
    /* promotion */
    const isPromotion = ${course.currentPrice.isPromotion};

    if (isPromotion) {
        let promoType = "${course.currentPrice.promotionType.stringValue}";
        $("#selPromotionType").val(promoType).trigger("change");

        let promoValue = "${course.currentPrice.promotionAmount}";
        $("#numPromotionAmount").val(promoValue).trigger("change");

        let promoDesc = "${course.currentPrice.promotionDesc}";
        $("#txtAPromotionDesc").val(promoDesc);
    }

    $("#chkIsPromotion").attr("checked", isPromotion).trigger("change");

    /* status */
    $("#selStatus").val(initialStatus).trigger("change");

    switch (initialStatus) {
        case "${Status.DESIGNING}":
            $(`#selStatus option[value="${Status.OPENED}"]`).remove();
            $(`#selStatus option[value="${Status.CLOSED}"]`).remove();
            break;

        case "${Status.AWAIT_REVIEW}":
            $(`#selStatus option[value="${Status.CLOSED}"]`).remove();
            break;

        case "${Status.OPENED}":
        case "${Status.CLOSED}":
            $(`#selStatus option[value="${Status.DESIGNING}"]`).remove();
            $(`#selStatus option[value="${Status.AWAIT_REVIEW}"]`).remove();
            break;
    }

    $("#script1").remove();
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>