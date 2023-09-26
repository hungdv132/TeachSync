<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 9/15/2023
  Time: 11:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ page import="com.teachsync.utils.enums.Gender" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Tài khoản</title>

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
<div class="row px-5">
    <div class="col-sm-12 col-md-4">
        <form action="/user-detail/avatar" method="post"
              id="formAvatar" class="card ts-bg-white border ts-border-teal rounded-3 text-center mb-3">

            <!-- Avatar -->
            <h5 class="card-header ts-txt-blue ts-border-blue"><c:out value="Ảnh đại diện"/></h5>
            <div class="card-body">
                <img src="../../resources/img/unset_avatar.jpg" alt="avatar"
                     id="imgUserAvatar" class="rounded-circle border ts-border-teal mb-3" width="180" height="180">

                <div id="divAvatarInput" class=" mb-3">
                    <input type="file" accept="image/*" onchange="updateImgFromInput('fileImg', 'imgUserAvatar', 0.75)"
                           id="fileImg" name="img" class="visually-hidden">
                    <input type="button" value="Choose File" onclick="$('#fileImg').click()">
                    <input type="hidden" value="../../../resources/img/no-img.jpg"
                           id="txtImg" name="userAvatar">
                    <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
                </div>

                <!-- About -->
                <label class="w-100"><h6>About</h6>
                    <textarea class="w-75 p-1 mb-3" style="resize: none;" rows="3"
                              minlength="0" maxlength="255"
                              id="txtAbout" name="about"><c:out value="${user.about}"/></textarea>
                </label>

            </div>
        </form>
    </div>

    <div class="col-sm-12 col-md-8">
        <div class="card ts-bg-white border ts-border-teal rounded-3 mb-3">
            <!-- User Account -->
            <h5 class="card-header ts-txt-blue ts-border-blue">Tài khoản</h5>
            <div class="card-body row">

                <!-- Username -->
                <label id="lblTxtUsername" class="col-4 mb-3 ">Tên Tài Khoản: <br>
                    <input type="text" class="w-100" minlength="5" maxlength="45" required="required"
                           id="txtUsername" value="">
                </label>

                <!-- New Password -->
                <label id="lblTxtPassword" class="col-4 mb-3 ">Mật Khẩu: <br>
                    <input type="password" class="w-100" minlength="5" maxlength="45"
                           id="txtPassword" required="required" >
                </label>

                <!-- Old Password -->
                <label id="lblTxtOldPassword" class="col-4 mb-3">Mật Khẩu cũ: <br>
                    <input type="password" class="w-100" minlength="5" maxlength="45"
                           id="txtOldPassword" required="required" >
                </label>

                <p id="txtAccountMsg" class="visually-hidden"></p>
            </div>

            <!-- User Detail -->
            <h5 class="card-header ts-txt-blue border-top ts-border-blue">Thông tin cá nhân</h5>
            <form
                  id="formDetail" class="card-body row" >
                <!-- FullName -->
                <label class="col-sm-6 col-md-4 mb-3">Họ và tên: <br>
                    <input type="text" class="w-100" minlength="1" maxlength="255" required="required"
                           id="txtFullName" name="fullName" value="${user.fullName}">
                </label>

                <!-- Email -->
                <label class="col-sm-6 col-md-4 mb-3">Email: <br>
                    <input type="email" class="w-100"
                           id="txtEmail" name="email" value="${user.email}">
                </label>

                <!-- Phone -->
                <label class="col-sm-6 col-md-4 mb-3">Số điện thoại: <br>
                    <input type="tel" class="w-100" minlength="10" maxlength="10" pattern="^\d{10}$"
                           placeholder="0XXXXXXXXX"
                           id="txtPhone" name="phone" value="${user.phone}">
                </label>

                <!-- Gender -->
                <label class="col-sm-6 col-md-4 mb-3">Giới tính: <br>
                    <select class="w-100 py-1"
                            id="selGender" name="gender">
                        <option value="${Gender.OTHER}">${Gender.OTHER.stringValueVie}</option>
                        <option value="${Gender.MALE}">${Gender.MALE.stringValueVie}</option>
                        <option value="${Gender.FEMALE}">${Gender.FEMALE.stringValueVie}</option>
                    </select>
                </label>

                <!-- Role -->
                <label class="col-sm-6 col-md-4 mb-3">Giới tính: <br>
                    <select class="w-100 py-1"
                            id="selRole" name="roleId">
                        <option value="1">Học sinh</option>
                        <option value="3">Nhân viên</option>
                    </select>
                </label>
            </form>

            <!-- User Address -->
            <h5 class="card-header ts-txt-blue border-top ts-border-blue">Địa chỉ</h5>
            <form action="/edit-profile/address" method="post"
                  id="formAddress" class="card-body row">

                <!-- AddressNo, Street -->
                <label class="col-4 mb-3">Số: <br/>
                    <input class="w-100" minlength="1" maxlength="45" required="required"
                           id="txtAddrNo" name="addressNo" value="${address.addressNo}">
                </label>
                <label class="col-4 mb-3">Đường: <br/>
                    <input class="w-100" minlength="1" maxlength="255" required="required"
                           id="txtStreet" name="street" value="${address.street}">
                </label>

                <!-- Ward, District, -->
                <label class="col-4 mb-3">Xã/Phường: <br/>
                    <select class="w-100 py-1"
                            id="selWard" name="unitId">
                        <option value="0">- Xin Chọn -</option>
                        <c:forEach items="${wardList}" var="ward">
                            <option value="${ward.id}">${ward.unitName}</option>
                        </c:forEach>
                    </select>
                </label>
                <label class="col-4 mb-3">Quận/Huyện: <br/>
                    <select class="w-100 py-1"
                            id="selDistrict" onchange="updateSelLocationUnit(3)">
                        <option value="0">- Xin Chọn -</option>
                        <c:forEach items="${districtList}" var="district">
                            <option value="${district.id}">${district.unitName}</option>
                        </c:forEach>
                    </select>
                </label>

                <!-- Province, Country -->
                <label class="col-4 mb-3">Tỉnh/TP trung ương: <br/>
                    <select class="w-100 py-1"
                            id="selProvince" onchange="updateSelLocationUnit(2)">
                        <option value="0">- Xin Chọn -</option>
                        <c:forEach items="${provinceList}" var="province">
                            <option value="${province.id}">${province.unitName}</option>
                        </c:forEach>
                    </select>
                </label>
                <label class="col-4 mb-3">Quốc gia: <br/>
                    <select class="w-100 py-1"
                            id="selCountry" onchange="updateSelLocationUnit(1)">
                        <option value="0">- Xin Chọn -</option>
                        <c:forEach items="${countryList}" var="country">
                            <option value="${country.id}">${country.unitName}</option>
                        </c:forEach>
                    </select>
                </label>

            </form>

            <div class="col-12 d-flex justify-content-center">
                <button type="button" id="submit" class="btn btn-primary w-50" onclick="createUser()">Tạo mới</button>
            </div>
        </div>
    </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script id="script1">
    $("#selGender").val('${user.gender}');
    $("#selRole").val('${user.roleId}')

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

    async function createUser() {
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

</body>
</html>
