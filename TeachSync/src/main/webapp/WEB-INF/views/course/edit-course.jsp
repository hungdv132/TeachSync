<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
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
          <c:url var="/courseDetail" value="course-detail">
            <c:param name="id" value="${course.id}"/>
          </c:url>
          <a href="${courseDetail}">
            <c:out value="${course.courseName}"/>
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Sửa khóa học
        </li>
      </ol>
    </nav>
  </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white d-flex justify-content-center border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <form onsubmit="editCourse(event)" class="col-12 d-flex justify-content-center px-5 mb-3">
      
      
      <div class="row">
        
        <h4>Thêm khóa học</h4>
        <br>
        
        <!-- Course Img -->
        <div class="col-sm-12 col-md-3 mb-3">
          <label class="w-100">
            Ảnh khóa học: <br/>
            <img src="${course.courseImg}" alt="courseImg" id="imgCourseImg"
                 class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
            <br/>
            <input type="file" name="img" id="txtImg" class="w-100"
                   accept="image/*" onchange="updateImgFromInput('txtImg', 'imgCourseImg', 0.75)">
          </label>
        </div>
        
        <!-- Course detail -->
        <div class="col-sm-12 col-md-9 mb-3">
          <!-- Course name, Alias -->
          <div class="row mb-3">
            <label class="col-sm-4 col-md-2">
              Mã khóa học: <br/>
              <input type="text" name="alias" id="txtAlias" class="w-100"
                     required="required" value="${course.courseAlias}">
            </label>
            <label class="col-sm-8 col-md-10">
              Tên khóa học: <br/>
              <input type="text" name="name" id="txtName" class="w-100"
                     required="required" value="${course.courseName}">
            </label>
          </div>
          
          <!-- Course desc -->
          <label class="w-100 mb-3">
            Miêu tả về khóa học: <br/>
            <textarea name="desc" id="txtDesc" class="w-100" style="resize: none"
                      rows="3">${course.courseDesc}</textarea>
          </label>
          
          <!-- Course numSession, minScore, minAttendant -->
          <div class="row">
            <label class="col-sm-12 col-md-4 mb-3">
              Số tiết học: <br/>
              <input type="number" name="numSession" id="txtNumSession" class="w-100"
                     required="required" min="1" max="100" value="${course.numSession}">
            </label>
            
            <label class="col-sm-12 col-md-4 mb-3">
              Điểm tối thiểu: <br/>
              <input type="number" name="minScore" id="txtMinScore" class="w-100"
                     required="required" min="0" max="10" step=".01" value="${course.minScore}">
            </label>
            
            <label class="col-sm-12 col-md-4 mb-3">
              Điểm danh tối thiểu: <br/>
              <span class="input-percent right">
              <input type="number" name="minAttendant" id="txtMinAttendant" class="w-100 pe-4"
                     required="required" min="0" max="100" step=".01" value="${course.minAttendant}">
            </span>
            </label>
          </div>
          
          <!-- Course price, isPromotion, promotionAmount, promotionType -->
          <div class="row">
            <div class="col-sm-12 col-md-4 mb-3">
              <div class="row align-items-end">
                <label class="col-8">
                  Giá khóa học:
                  <br/>
                  <span class="input-vnd right">
                  <input type="number" name="price" id="txtPrice" class="w-100 pe-4"
                         required="required" min="1000" max="99999000" value="${course.currentPrice.price}" step="100"
                         oninput="calculateFinalPrice(); updateInputPromotionAmountMax()">
                </span>
                </label>
                
                <label class="col-4 px-0">
                  <input type="checkbox" name="isPromotion" id="chkIsPromotion"
                         onchange="togglePromotion()">
                  Giảm giá.
                </label>
                <script id="script1">
                    $("#chkIsPromotion").attr("checked", ${course.currentPrice.isPromotion});
                    $("#script1").remove();
                </script>
              </div>
            </div>
            
            <div class="col-sm-12 col-md-4 mb-3 visually-hidden" id="divPromotionAmount">
              <div class="row d-flex align-items-end">
                <label class="col-9 pe-0">
                  Giảm:
                  <br/>
                  <input type="number" name="promotionAmount" id="txtPromotionAmount" class="w-100"
                         min="0.01" max="100" step=".01"
                         value="${course.currentPrice.isPromotion ? course.currentPrice.promotionAmount : '0.01'}"
                         oninput="calculateFinalPrice()" onchange="calculateFinalPrice()">
                </label>
                
                <label class="col-3">
                  <select name="promotionType" id="selPromotionType" class="w-100"
                          onchange="changeInputPromotionAmountSpec()">
                    <option value="PERCENT">%</option>
                    <option value="AMOUNT">₫</option>
                  </select>
                </label>
                <script id="script2">
                    let isPromotion = ${course.currentPrice.isPromotion};
                    if (isPromotion) {
                        $("#selPromotionType").val("${course.currentPrice.promotionType.stringValue}").change();
                    }
                    $("#script2").remove();
                </script>
              </div>
            </div>
            
            <label class="col-sm-12 col-md-4 mb-3 visually-hidden" id="lblPromotionFinal">
              Giá sau giảm:
              <br/>
              <span class="input-vnd right">
                <input type="number" name="price" id="txtFinalPrice" class="w-100 pe-4"
                       disabled="disabled" value="${course.currentPrice.finalPrice}">
              </span>
            </label>
          </div>
          
          <!-- Course promotionDesc -->
          <label class="w-100 visually-hidden" id="lblPromotionDesc">
            Chi tiết Khuyến mãi: <br/>
            <textarea name="promotionDesc" id="txtPromotionDesc" class="w-100"  style="resize: none"
                      rows="3">${course.currentPrice.isPromotion ? course.currentPrice.promotionDesc : ''}</textarea>
          </label>
        
        </div>
        
        <!-- Submit button -->
        <div class="col-12">
          <div class="row d-flex justify-content-center">
            <div class="col-sm-12 col-md-4">
              <button type="submit" class="btn btn-primary w-100">Submit</button>
            </div>
          </div>
        </div>
      
      </div>
    </form>
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
    
    togglePromotion();

    function updateInputPromotionAmountMax() {
        $("#txtPromotionAmount").attr({
            'max': $("#txtPrice").val(),
            'placeholder' : '0 - ' + $("#txtPrice").val()});
    }

    function changeInputPromotionAmountSpec() {
        let txtPromotionAmount = $("#txtPromotionAmount");

        switch ($("#selPromotionType").val()) {
            case 'AMOUNT':
                let price = $("#txtPrice").val();
                txtPromotionAmount
                    .attr({
                        'min': "100",
                        'max': price,
                        'step': '100',
                        'placeholder' : '0 - ' + price})
                    .val('100');
                break;

            case 'PERCENT':
                txtPromotionAmount
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
        let price = $("#txtPrice").val();
        let promotionAmount = $("#txtPromotionAmount").val();
        let finalPrice = 0;

        switch ($("#selPromotionType").val()) {
            case 'AMOUNT':
                finalPrice = price - promotionAmount;
                break;

            case 'PERCENT':
                finalPrice = price - (price * (promotionAmount/100));
                break;
        }

        $("#txtFinalPrice").val(finalPrice);
    }

    function togglePromotion() {
        let promotionAmount = $("#txtPromotionAmount");

        if ($("#chkIsPromotion").is(":checked")) {
            showById("divPromotionAmount");
            showById("lblPromotionFinal");
            showById("lblPromotionDesc");

            promotionAmount.attr("required", true);

            calculateFinalPrice();
        } else {
            hideById("divPromotionAmount");
            hideById("lblPromotionFinal");
            hideById("lblPromotionDesc");

            promotionAmount.attr("required", false);
        }
    }

    async function editCourse(event) {
        event.preventDefault();

        let file = $('#fileImg').prop("files")[0];

        let imgURL = await uploadImageFileToFirebaseAndGetURL(file);
        
        let priceCreateDTO = {};

        if ($("#chkIsPromotion").is(":checked")) {
            priceCreateDTO = {
                "price": $("#txtPrice").val(),
                "isPromotion": true,
                "promotionType": $("#selPromotionType").val(),
                "promotionAmount": $("#txtPromotionAmount").val(),
                "promotionDesc": $("#txtPromotionDesc").val(),
            }
        } else {
            priceCreateDTO = {
                "price": $("#txtPrice").val(),
            }
        }

        let createDTO = {
            "id": ${course.id},
            "courseName": $("#txtName").val(),
            "courseAlias": $("#txtAlias").val(),
            "courseImg": imgURL,
            "courseDesc": $("#txtDesc").val(),
            "numSession": $("#txtNumSession").val(),
            "minScore": $("#txtMinScore").val(),
            "minAttendant": $("#txtMinAttendant").val(),

            "price": priceCreateDTO,
        }

        $.ajax({
            type: "PUT",
            data: JSON.stringify(createDTO),
            url: "/edit-course",
            contentType: "application/json",
            success: function(response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })

        return false;
    }
</script>
</html>