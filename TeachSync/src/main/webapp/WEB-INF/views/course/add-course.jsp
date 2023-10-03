<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Thêm khóa học</title>

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
        <li class="breadcrumb-item active" aria-current="page">
          Thêm khóa học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <%--@elvariable id="createDTO" type="com.teachsync.dtos.course.CourseCreateDTO"--%>
    <form:form id="form"
               modelAttribute="createDTO" action="/add-course" method="post" class="row"
               onkeydown="return event.key != 'Enter';">
    
        <h4>Thêm khóa học</h4>
        <br>
    
        <!-- Course Img -->
        <div class="col-sm-12 col-md-3 mb-3">
          <label for="fileImg" class="form-label">Ảnh khóa học:</label>
          <img id="imgCourseImg"
               src="../../../resources/img/no-img.jpg" alt="courseImg"
               class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
            
          <input id="fileImg" name="img"
                 type="file" accept="image/*"
                 class="form-control ts-border-grey"
                 onchange="updateImgFromInput('fileImg', 'imgCourseImg', 0.75)">
          <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
          <input id="hidCourseImg" name="courseImg"
                 type="hidden" value="../../../resources/img/no-img.jpg">
        </div>
    
        <!-- Course detail -->
        <div class="col-sm-12 col-md-9 mb-3">
          <div class="row mb-3">
            <!-- Course Alias -->
            <div class="col-sm-4 col-md-2 mb-3">
              <label for="txtAlias" class="form-label">Mã khóa học:</label>
              <input id="txtAlias" name="courseAlias"
                     type="text" minlength="1" maxlength="10"
                     class="form-control ts-border-grey"
                     required="required">
            </div>

            <!-- Course Name -->
            <div class="col-sm-8 col-md-10 mb-3">
              <label for="txtName" class="form-label">Tên khóa học:</label>
              <input id="txtName" name="courseName"
                     type="text" minlength="1" maxlength="45"
                     class="form-control ts-border-grey"
                     required="required">
            </div>
            
            <!-- Course Desc -->
            <div class="col-12 mb-3">
              <label for="txtADesc" class="form-label">Miêu tả về khóa học:</label>
              <textarea id="txtADesc" name="courseDesc"
                        minlength="0" maxlength="9999"
                        class="form-control ts-border-grey" rows="3" style="resize: none"
                        required="required"></textarea>
            </div>
          </div>
  
          <!-- Course numSession, minScore, minAttendant -->
          <div class="row">
            <!-- Course numSession -->
            <div class="col-sm-12 col-md-4 mb-3">
              <label for="numNumSession" class="form-label">Số tiết học:</label>
              <input id="numNumSession" name="numSession"
                     type="number" min="1" max="100" value="10"
                     class="form-control ts-border-grey"
                     required="required">
            </div>

            <!-- Course minScore -->
            <div class="col-sm-12 col-md-4 mb-3">
              <label for="numMinScore" class="form-label">Điểm tối thiểu:</label>
              <input id="numMinScore" name="minScore"
                     type="number" min="0" max="10" step=".01" value="5"
                     class="form-control ts-border-grey"
                     required="required">
            </div>

            <!-- Course minAttendant -->
            <div class="col-sm-12 col-md-4 mb-3">
              <label for="numMinAttendant" class="form-label">Điểm danh tối thiểu:</label>
              <div class="input-group">
                <input id="numMinAttendant" name="minAttendant"
                       type="number" min="0" max="100" step=".01" value="80"
                       class="form-control ts-border-grey"
                       required="required">
                <span class="input-group-text ts-border-grey">%</span>
              </div>
            </div>
          </div>
  
          <div class="row">
            <div class="col-sm-12 col-md-4 mb-3">
  
              <!-- Course price -->
              <label for="numPrice" class="form-label">Giá khóa học:</label>
              <div class="input-group">
                <input id="numPrice" name="price.price"
                       type="number" min="1000" max="99999000" step="100" value="100000"
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
  
            <!-- Course promotionAmount -->
            <div class="col-sm-12 col-md-4 mb-3 visually-hidden" id="divPromotionAmount">
              <label class="form-label" for="numPromotionAmount">Giảm:</label>
              <div class="input-group">
                <input id="numPromotionAmount" name="price.promotionAmount"
                       type="number" min="0.01" max="100" step=".01" value="0.01"
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
                       type="text"
                       class="form-control ts-border-grey"
                       disabled="disabled" readonly="readonly">
                <span class="input-group-text ts-border-grey">₫</span>
              </div>
            </div>
  
            <!-- Course promotionDesc -->
            <div class="col-12 visually-hidden" id="divPromotionDesc">
              <label for="txtAPromotionDesc" class="form-label">Chi tiết Khuyến mãi:</label>
              <textarea id="txtAPromotionDesc" name="price.promotionDesc"
                        minlength="0" maxlength="9999"
                        class="form-control ts-border-grey" rows="3" style="resize: none;"
                        disabled="disabled"></textarea>
            </div>
          </div>
        </div>
        
        <!-- Submit button -->
        <div class="col-12">
          <div class="row d-flex justify-content-center">
            <div class="col-sm-12 col-md-4">
              <button id="btnSubmit" type="submit" class="btn btn-primary w-100">Submit</button>
            </div>
          </div>
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

</script>

<script>
    /* Validate input */
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
    
    
    $("#btnSubmit").on("click", async function (event) {
        let isInvalid = 0;
        
        /* Validate input */
        if(!validateTextInput(
            txtAlias, txtAlias.minLength, txtAlias.maxLength,
            ["required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"])) {
            isInvalid++;
        }

        if(!validateTextInput(
            txtName, txtName.minLength, txtName.maxLength,
            ["required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"])) {
            isInvalid++;
        }

        if(!validateTextInput(
            txtADesc, 1, txtADesc.maxLength,
            ["nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar"])) {
            isInvalid++;
        }

        /* TODO: number validate */

        if (isInvalid > 0) {
            event.preventDefault();
        } else {
            let file = $('#fileImg').prop("files")[0];

            let imgURL = await uploadImageFileToFirebaseAndGetURL(file);

            $("#hidCourseImg").val(imgURL);
        }
    });
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>