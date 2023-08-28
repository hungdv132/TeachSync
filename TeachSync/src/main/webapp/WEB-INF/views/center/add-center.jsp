<%@ page import="com.teachsync.utils.enums.CenterType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Sửa Cơ Sở</title>

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
                <li class="breadcrumb-item">
                    <a href="/center">Danh sách Cơ Sở</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    Tạo Cơ Sở
                </li>
            </ol>
        </nav>
    </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
    <form class="col-12" id="form" action="/create-center" method="POST">
        <div class="row mb-3">
            <h4>Tạo mới cơ sở</h4>
            <br>
            <input type="hidden" name="id" value="${center.id}">

            <!-- Center Img -->
            <div class="col-sm-12 col-md-4 mb-3">
                <label class="w-100">
                    Ảnh Cơ Sở: <br/>
                    <img src="${empty center.centerImg ? '../../../resources/img/no-img.jpg' : center.centerImg}" alt="courseImg"
                         id="imgCenterImg" class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
                    <br/>
                    <input type="file" name="img" id="fileImg" class="w-100"
                           accept="image/*" onchange="updateImgFromInput('fileImg', 'imgCenterImg', 0.75)">
                    <input type="hidden" name="centerImg" id="txtImg"
                           value="../../../resources/img/no-img.jpg">
                </label>
                <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
            </div>

            <!-- Center detail -->
            <div class="col-sm-12 col-md-8 mb-3">

                <!-- Center name, type, size -->
                <div class="row mb-3">
                    <label class="col-4">Tên trung tâm: <br/>
                        <input class="w-100" type="text"
                               id="centerName" name="centerName" value="">
                    </label>

                    <label class="col-4">Chuyên môn: <br/>
                        <select class="w-100 py-1"
                                id="selCenterType" name="centerType" >
                            <option value="${CenterType.ENGLISH}">Tiếng Anh</option>
                        </select>
                    </label>

                    <label class="col-4">Số phòng: <br/>
                        <input class="w-100" type="number" min="1" max="999"
                               id="centerSize" name="centerSize" value="0">
                    </label>
                </div>

                <!-- Center desc -->
                <label class="w-100 mb-3">
                    Miêu tả về Cơ Sở: <br/>
                    <textarea class="w-100" style="resize: none" rows="3"
                              id="txtDesc" name="centerDesc" >${center.centerDesc}</textarea>
                </label>

                <!-- Center address -->
                <div class="row">
                    <label class="col-6 mb-3">Số: <br/>
                        <input class="w-100"
                               id="txtAddrNo" name="addressNo" value="">
                    </label>
                    <label class="col-6 mb-3">Đường: <br/>
                        <input class="w-100"
                               id="txtStreet" name="street" value="">
                    </label>

                    <label class="col-6 mb-3">Quốc gia: <br/>
                        <select class="w-100 py-1"
                                id="selCountry" onchange="updateSelLocationUnit(1)">
                            <c:forEach items="${countryList}" var="country">
                                <option value="${country.id}">${country.unitName}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label class="col-6 mb-3">Tỉnh/TP trung ương: <br/>
                        <select class="w-100 py-1"
                                id="selProvince" onchange="updateSelLocationUnit(2)">
                            <c:forEach items="${provinceList}" var="province">
                                <option value="${province.id}">${province.unitName}</option>
                            </c:forEach>
                        </select>
                    </label>

                    <label class="col-6 mb-3">Quận/Huyện: <br/>
                        <select class="w-100 py-1"
                                id="selDistrict" onchange="updateSelLocationUnit(3)">
                            <c:forEach items="${districtList}" var="district">
                                <option value="${district.id}">${district.unitName}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label class="col-6 mb-3">Xã/Phường: <br/>
                        <select class="w-100 py-1"
                                id="selWard" name="unitId">
                            <c:forEach items="${wardList}" var="ward">
                                <option value="${ward.id}">${ward.unitName}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
            </div>

            <div class="col-12 d-flex justify-content-center">
                <button type="button" id="submit" class="btn btn-primary w-50" onclick="createCenter()">Tạo mới</button>
            </div>
        </div>
    </form>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script id="script1">
    $("#selCenterType").val('${center.centerType}');
    let level;
    <c:forEach items="${levelUnitIdMap}" var="levelUnitId">
    level = ${levelUnitId.key};
    switch (level) {
        case 0:
            $("#selCountry").val(${levelUnitId.value});
            break;
        case 1:
            $("#selProvince").val(${levelUnitId.value});
            break;
        case 2:
            $("#selDistrict").val(${levelUnitId.value});
            break;
        case 3:
            $("#selWard").val(${levelUnitId.value});
            break;
    }
    </c:forEach>

    $("#script1").remove();
</script>
<script>
    async function updateSelLocationUnit(level) {
        let unitId;
        let selCountry = $("#selCountry");
        let selProvince = $("#selProvince");
        let selDistrict = $("#selDistrict");
        let selWard = $("#selWard");

        switch (level) {
            case 1:
                unitId = selCountry.val();
                break;
            case 2:
                unitId = selProvince.val();
                break;
            case 3:
                unitId = selDistrict.val();
                break;
        }

        await $.ajax({
            type: "GET",
            url: "/api/refresh-location-unit?unitId=" + unitId + "&level=" + level,
            success: function (response) {
                let unitList;
                //Check null
                switch (level) {
                    case 1:
                        selProvince.empty();
                        unitList = response['1'];
                        for (const province of unitList) {
                            selProvince.append('<option value="' + province['id'] + '">' + province['unitName'] + '</option>');
                        } /* No Break, let overflow due to repeated code */
                    case 2:
                        selDistrict.empty();
                        unitList = response['2'];
                        for (const district of unitList) {
                            selDistrict.append('<option value="' + district['id'] + '">' + district['unitName'] + '</option>');
                        } /* No Break, let overflow due to repeated code */
                    case 3:
                        selWard.empty();
                        unitList = response['3'];
                        for (const ward of unitList) {
                            selWard.append('<option value="' + ward['id'] + '">' + ward['unitName'] + '</option>');
                        }
                        break;
                }
            }
        });
    }

    async function createCenter() {
        let file = $('#fileImg').prop("files")[0];

        let imgURL = await uploadImageFileToFirebaseAndGetURL(file);
        $("#txtImg").val(imgURL);

        const form = document.getElementById('form');
        form.requestSubmit();
    }
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>
