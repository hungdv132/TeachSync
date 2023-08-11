<%@ page import="com.teachsync.utils.enums.CenterType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Center Detail</title>

    <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../resources/css/certificate_style.css">
    <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
    <script src="../../resources/js/common.js"></script>
    <style>
        .detail-container {
            display: flex;
            align-items: flex-start;
            padding: 20px;
        }

        #centerImage {
            width: 550px;
            height: auto;
            object-fit: cover;
            border-radius: 10px;
            margin-right: 20px;
        }

        .info-container {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
            margin-left: 40px;
        }

        h2 {
            font-size: 24px;
            margin-top: 0;
        }

        p {
            font-size: 18px;
            margin-top: 10px;
            margin-bottom: 0;
        }

        #submit{
            font-weight: bold;
            padding: 10px;
            margin-left: 700px;
            border-radius: 5px;
            background-color: #fd7e14;
        }
    </style>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>

<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Breadcrumb ================================================= -->

<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
    <img src="https://amore-architecture.vn/wp-content/uploads/2021/12/TTTA-GCE-tp-HCM-1.jpg" id="centerImage">

    <form class="col-12" action="/edit-center" method="POST">
        <div class="row">
            <input type="hidden" name="centerId" value="${center.id}">

            <label class="col-4">Tên trung tâm: <br/>
                <input class="w-100" type="text" id="centerName" name="centerName" value="${center.centerName}">
            </label>

            <label class="col-4">Chuyên môn: <br/>
                <select class="w-100" name="centerType" id="selCenterType">
                    <option value="${CenterType.ENGLISH}">Tiếng Anh</option>
                </select>
            </label>

            <label class="col-4">Số phòng: <br/>
                <input class="w-100" type="number" min="1" max="999" id="centerSize" name="centerSize" value="${center.centerSize}">
            </label>

            <br/>

            <label class="col-2">Số nhà: <br/>
                <input class="w-100" id="txtAddrNo" name="addrNo" value="${center.address.addressNo}" oninput="updateAddress()">
            </label>


            <label class="col-2">Đường: <br/>
                <input class="w-100" id="txtStreet" name="street" value="${center.address.street}" oninput="updateAddress()">
            </label>

            <label class="col-2">Quốc gia: <br/>
                <select class="w-100" name="countryId" id="selCountry" onchange="updateSelLocationUnit(1)">
                    <c:forEach items="${countryList}" var="country">
                        <option value="${country.id}">${country.unitName}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="col-2">Tỉnh/TP trung ương: <br/>
                <select class="w-100" name="provinceId" id="selProvince" onchange="updateSelLocationUnit(2)">
                    <c:forEach items="${provinceList}" var="province">
                        <option value="${province.id}">${province.unitName}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="col-2">Quận/Huyện: <br/>
                <select class="w-100" name="districtId" id="selDistrict" onchange="updateSelLocationUnit(3)">
                    <c:forEach items="${districtList}" var="district">
                        <option value="${district.id}">${district.unitName}</option>
                    </c:forEach>
                </select>
            </label>

            <label class="col-2">Xã/Phường: <br/>
                <select class="w-100" name="wardId" id="selWard" onchange="updateAddress()">
                    <c:forEach items="${wardList}" var="ward">
                        <option value="${ward.id}">${ward.unitName}</option>
                    </c:forEach>
                </select>
            </label>

            <div class="col-12">Địa chỉ:
                <p class="w-100" id="centerAddress">${center.address.addressString}</p>
            </div>

            <div class="col-12 d-flex justify-content-center">
                <button type="submit" id="submit" class="btn btn-primary w-50">Chỉnh sửa</button>
            </div>
        </div>
    </form>

</div>

<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
<script id="script1">
    $("#selCenterType").val('${center.centerType}');


    $("#selCountry").val(${levelUnitIdMap.get(0)});
    $("#selProvince").val(${levelUnitIdMap.get(1)});
    $("#selDistrict").val(${levelUnitIdMap.get(2)});
    $("#selWard").val(${levelUnitIdMap.get(3)});

    $("#script1").remove();
</script>

<script>
    async function updateSelLocationUnit (level) {
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
            url: "/api/refresh-location-unit?unitId="+unitId+"&level="+level,
            contentType: "application/json",
            success: function(response) {
                let unitList;
                //Check null
                switch (level) {
                    case 1:
                        selProvince.empty();
                        unitList = response['1'];
                        for (const province of unitList) {
                            selProvince.append('<option value="' + province['id'] + '">' + province['unitName'] + '</option>');
                        }

                        selDistrict.empty();
                        unitList = response['2'];
                        for (const district of unitList) {
                            selDistrict.append('<option value="' + district['id'] + '">' + district['unitName'] + '</option>');
                        }

                        selWard.empty();
                        unitList = response['3'];
                        for (const ward of unitList) {
                            selWard.append('<option value="' + ward['id'] + '">' + ward['unitName'] + '</option>');
                        }
                        break;

                    case 2:
                        selDistrict.empty();
                        unitList = response['2'];
                        for (const district of unitList) {
                            selDistrict.append('<option value="' + district['id'] + '">' + district['unitName'] + '</option>');
                        }

                        selWard.empty();
                        unitList = response['3'];
                        for (const ward of unitList) {
                            selWard.append('<option value="' + ward['id'] + '">' + ward['unitName'] + '</option>');
                        }
                        break;

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

        updateAddress();
    }

    function updateAddress () {
        let addrNo = $("#txtAddrNo").val();
        let street = $("#txtStreet").val();
        let ward = $("#selWard").find(":selected").text();
        let district = $("#selDistrict").find(":selected").text();
        let province = $("#selProvince").find(":selected").text();
        let country = $("#selCountry").find(":selected").text();

        let addrString = addrNo + " " + street + ", " + ward + ", " + district + ", " + province + ", " + country;
        $("centerAddress").text(addrString);
    }
</script>
</body>
</html>
