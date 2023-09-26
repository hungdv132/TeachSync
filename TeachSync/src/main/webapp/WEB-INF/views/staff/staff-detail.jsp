<%@ page import="com.teachsync.utils.enums.Gender" %>
<%@ page import="com.teachsync.utils.enums.StaffType" %>
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
        <form action="/staff-detail/avatar" method="post"
              id="formAvatar" class="card ts-bg-white border ts-border-teal rounded-3 text-center mb-3">

            <!-- Avatar -->
            <h5 class="card-header ts-txt-blue ts-border-blue"><c:out value="${staff.user.username}"/></h5>
            <div class="card-body">
                <img src="${empty staff.user.userAvatar ? '../../resources/img/unset_avatar.jpg' : staff.user.userAvatar}" alt="avatar"
                     id="imgUserAvatar" class="rounded-circle border ts-border-teal mb-3" width="180" height="180">

                <div id="divAvatarInput" class="visually-hidden mb-3">
                    <input type="file" accept="image/*" onchange="updateImgFromInput('fileImg', 'imgUserAvatar', 0.75)"
                           id="fileImg" name="img" class="visually-hidden" disabled="disabled">
                    <input type="button" value="Choose File" onclick="$('#fileImg').click()">
                    <input type="hidden" value="../../../resources/img/no-img.jpg"
                           id="txtImg" name="userAvatar">
                    <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
                </div>

                <!-- About -->
                <label class="w-100"><h6>About</h6>
                    <textarea class="w-75 p-1 mb-3" style="resize: none;" rows="3" disabled="disabled"
                              minlength="0" maxlength="255"
                              id="txtAbout" name="about"><c:out value="${staff.user.about}"/></textarea>
                </label>

                <!-- Button -->
                <div class="d-flex justify-content-center w-100">
                    <button type="button" class="btn btn-warning"
                            id="btnEditAvatar" onclick="editUserAvatar()">Sửa</button>
                    <button type="button" class="btn btn-primary visually-hidden me-1"
                            id="btnUpdateAvatar" onclick="updateUserAvatar()">Lưu</button>
                    <button type="button" class="btn btn-danger visually-hidden ms-1"
                            id="btnCancelEditAvatar" onclick="cancelEditUserAvatar()">Hủy</button>
                </div>
            </div>
        </form>
    </div>

    <div class="col-sm-12 col-md-8">
        <div class="card ts-bg-white border ts-border-teal rounded-3 mb-3">
            <!-- User Account -->
            <h5 class="card-header ts-txt-blue ts-border-blue">Tài khoản</h5>
            <div class="card-body row">
                <!-- Button -->
                <div id="divBtnAccount" class="col-12">
                    <button type="button" class="btn btn-primary w-100 mb-3"
                            id="btnEditUsername" onclick="editAccount('username')">Đổi Tên Tài Khoản</button>
                    <button type="button" class="btn btn-primary w-100"
                            id="btnEditPassword" onclick="editAccount('password')">Đổi Mật Khẩu</button>
                </div>

                <!-- Username -->
                <label id="lblTxtUsername" class="col-4 mb-3 visually-hidden">Tên Tài Khoản: <br>
                    <input type="text" class="w-100" minlength="5" maxlength="45" required="required"
                           id="txtUsername" value="${staff.user.username}" disabled="disabled">
                </label>

                <!-- New Password -->
                <label id="lblTxtPassword" class="col-4 mb-3 visually-hidden">Mật Khẩu: <br>
                    <input type="password" class="w-100" minlength="5" maxlength="45"
                           id="txtPassword" required="required" disabled="disabled">
                </label>

                <p id="txtAccountMsg" class="visually-hidden"></p>

                <div class="col-12">
                    <button type="button" class="btn btn-primary w-15 visually-hidden me-1"
                            id="btnUpdateUsername" onclick="updateAccount('username')">Lưu</button>
                    <button type="button" class="btn btn-primary w-15 visually-hidden me-1"
                            id="btnUpdatePassword" onclick="updateAccount('password')">Lưu</button>
                    <button type="button" class="btn btn-danger w-15 visually-hidden ms-1"
                            id="btnCancelEditAccount" onclick="cancelEditAccount()">Hủy</button>
                </div>
            </div>

            <!-- User Detail -->
            <h5 class="card-header ts-txt-blue border-top ts-border-blue">Thông tin cá nhân</h5>
            <form action="/staff-detail/detail" method="POST"
                  id="formDetail" class="card-body row" >
                <input type="hidden" name="staffId" value="${staff.id}">
                <!-- FullName -->
                <label class="col-sm-6 col-md-4 mb-3">Họ và tên: <br>
                    <input type="text" class="w-100" minlength="1" maxlength="255" required="required" disabled="disabled"
                           id="txtFullName" name="fullName" value="${staff.user.fullName}">
                </label>

                <!-- Email -->
                <label class="col-sm-6 col-md-4 mb-3">Email: <br>
                    <input type="email" class="w-100" disabled="disabled"
                           id="txtEmail" name="email" value="${staff.user.email}">
                </label>

                <!-- Phone -->
                <label class="col-sm-6 col-md-4 mb-3">Số điện thoại: <br>
                    <input type="tel" class="w-100" minlength="10" maxlength="10" pattern="^\d{10}$"
                           placeholder="0XXXXXXXXX" disabled="disabled"
                           id="txtPhone" name="phone" value="${staff.user.phone}">
                </label>

                <!-- Gender -->
                <label class="col-sm-6 col-md-4 mb-3">Giới tính: <br>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selGender" name="gender">
                        <option value="${Gender.OTHER}">${Gender.OTHER.stringValueVie}</option>
                        <option value="${Gender.MALE}">${Gender.MALE.stringValueVie}</option>
                        <option value="${Gender.FEMALE}">${Gender.FEMALE.stringValueVie}</option>
                    </select>
                </label>


                <!-- Button -->
                <div class="col-12">
                    <button type="button" class="btn btn-warning w-15"
                            id="btnEditDetail" onclick="editUserDetail()">Sửa</button>
                    <button type="submit" class="btn btn-primary w-15 visually-hidden me-1"
                            id="btnUpdateDetail">Lưu</button>
                    <button type="button" class="btn btn-danger w-15 visually-hidden ms-1"
                            id="btnCancelEditDetail" onclick="cancelEditUserDetail()">Hủy</button>
                </div>
            </form>

            <!-- Staff type, center -->

            <h5 class="card-header ts-txt-blue border-top ts-border-blue">Cơ Sở Hiện Tại và Chức Vụ</h5>
            <form action="/staff-detail/staffCenter" method="POST"
                  id="formStaffCenter" class="card-body row" >
                <input type="hidden" name="staffId" value="${staff.id}">

                <label class="col-sm-6 col-md-4 mb-3">Trung tâm: <br>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selCenter" name="centerId">
                        <option value="">1</option>
                        <option value="">2</option>
                        <option value="">3</option>
                    </select>
                </label>
                <select name="centerId" id="centerIdSel" class="btn btn-secondary dropdown-toggle"
                        onchange="refreshStaff()">
                    <c:forEach items="${centerList}" var="center">
                        <option value="${center.id}"> ${center.centerName}</option>
                    </c:forEach>
                </select>
                <label class="col-sm-6 col-md-4 mb-3">Chức vụ: <br>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selStaffType" name="staffType">
                        <option value="${StaffType.MANAGER}">${StaffType.MANAGER.stringValueVie}</option>
                        <option value="${StaffType.TEACHER}">${StaffType.TEACHER.stringValueVie}</option>
                        <option value="${StaffType.ACCOUNTANT}">${StaffType.ACCOUNTANT.stringValueVie}</option>
                        <option value="${StaffType.EMPLOYEE}">${StaffType.EMPLOYEE.stringValueVie}</option>
                    </select>
                </label>

                <!-- Button -->
                <div class="col-12">
                    <button type="button" class="btn btn-warning w-15"
                            id="btnEditStaffCenter" onclick="editStaffCenter()">Sửa</button>
                    <button type="submit" class="btn btn-primary w-15 visually-hidden me-1"
                            id="btnUpdateStaffCenter">Lưu</button>
                    <button type="button" class="btn btn-danger w-15 visually-hidden ms-1"
                            id="btnCancelEditStaffCenter" onclick="cancelEditStaffCenter()">Hủy</button>
                </div>
            </form>
            <!-- User Address -->
            <h5 class="card-header ts-txt-blue border-top ts-border-blue">Địa chỉ</h5>
            <form action="/staff-detail/address" method="post"
                  id="formAddress" class="card-body row">

                <!-- AddressNo, Street -->
                <label class="col-4 mb-3">Số: <br/>
                    <input class="w-100" minlength="1" maxlength="45" required="required" disabled="disabled"
                           id="txtAddrNo" name="addressNo" value="${address.addressNo}">
                </label>
                <label class="col-4 mb-3">Đường: <br/>
                    <input class="w-100" minlength="1" maxlength="255" required="required" disabled="disabled"
                           id="txtStreet" name="street" value="${address.street}">
                </label>

                <!-- Ward, District, -->
                <label class="col-4 mb-3">Xã/Phường: <br/>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selWard" name="unitId">
                        <option value="0" >- Xin Chọn -</option>
                        <c:forEach items="${wardList}" var="ward">
                            <option value="${ward.id}">${ward.unitName}</option>
                        </c:forEach>
                    </select>
                </label>
                <label class="col-4 mb-3">Quận/Huyện: <br/>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selDistrict" onchange="updateSelLocationUnit(3)">
                        <option value="0" >- Xin Chọn -</option>
                        <c:forEach items="${districtList}" var="district">
                            <option value="${district.id}">${district.unitName}</option>
                        </c:forEach>
                    </select>
                </label>

                <!-- Province, Country -->
                <label class="col-4 mb-3">Tỉnh/TP trung ương: <br/>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selProvince" onchange="updateSelLocationUnit(2)">
                        <option value="0" >- Xin Chọn -</option>
                        <c:forEach items="${provinceList}" var="province">
                            <option value="${province.id}">${province.unitName}</option>
                        </c:forEach>
                    </select>
                </label>
                <label class="col-4 mb-3">Quốc gia: <br/>
                    <select class="w-100 py-1" disabled="disabled"
                            id="selCountry" onchange="updateSelLocationUnit(1)">
                        <option value="0" >- Xin Chọn -</option>
                        <c:forEach items="${countryList}" var="country">
                            <option value="${country.id}">${country.unitName}</option>
                        </c:forEach>
                    </select>
                </label>

                <!-- Button -->
                <div class="col-12">
                    <button type="button" class="btn btn-warning w-15"
                            id="btnEditAddress" onclick="editUserAddress()">Sửa</button>
                    <button type="submit" class="btn btn-primary w-15 visually-hidden me-1"
                            id="btnUpdateAddress">Lưu</button>
                    <button type="button" class="btn btn-danger w-15 visually-hidden ms-1"
                            id="btnCancelEditAddress" onclick="cancelEditUserAddress()">Hủy</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script id="script1">
    <c:if test="${not empty levelUnitIdMap}">
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
    </c:if>
    <c:if test="${empty levelUnitIdMap}">
    $("#selCountry option[value='0']").prop("selected", true);
    $("#selProvince option[value='0']").prop("selected", true);
    $("#selDistrict option[value='0']").prop("selected", true);
    $("#selWard option[value='0']").prop("selected", true);
    </c:if>

    $("#selGender").val('${staff.user.gender}');
    $("#selCenter").val('${staff.centerId}');
    $("#selStaffTyper").val('${staff.staffType}');
    $("#script1").remove();
</script>
<script>
    let avatar = '../../resources/img/unset_avatar.jpg';
    <c:if test="${not empty staff.user.userAvatar}">
    avatar = '${staff.user.userAvatar}';
    </c:if>

    const username = $("#txtUsername").val();

    const userDetail = {
        "fullName" : $("#txtFullName").val(),
        "email" : $("#txtEmail").val(),
        "phone" : $("#txtPhone").val(),
        "gender" : $("#selGender").val()
    };

    const staffCenter = {
        "center": $("#selCenter").val(),
        "staffType": $("#selStaffType").val()
    };

    const address = {
        "addressNo": "${address.addressNo}",
        "street": "${address.street}",
    };

    const levelUnitIdMap = {
        "0": $("#selCountry").val(),
        "1": $("#selProvince").val(),
        "2": $("#selDistrict").val(),
        "3": $("#selWard").val()
    };

    /* Avatar */
    function editUserAvatar() {
        enableAllInputIn("formAvatar");
        enableAllTextAreaIn("formAvatar");
        hideById("btnEditAvatar");
        showById("btnUpdateAvatar");
        showById("btnCancelEditAvatar");
        showById("divAvatarInput");

        cancelEditAccount();
        cancelEditUserDetail();
        cancelEditUserAddress();
        cancelEditStaffCenter();
    }
    async function updateUserAvatar() {
        let file = $('#fileImg').prop("files")[0];

        let imgURL = await uploadImageFileToFirebaseAndGetURL(file);
        $("#txtImg").val(imgURL);

        const form = document.getElementById('formAvatar');
        form.requestSubmit();
    }
    function cancelEditUserAvatar() {
        disableAllInputIn("formAvatar");
        disableAllTextAreaIn("formAvatar");
        showById("btnEditAvatar");
        hideById("btnUpdateAvatar");
        hideById("btnCancelEditAvatar");
        hideById("divAvatarInput");

        $("#imgUserAvatar").attr("src", avatar)
        $("#txtImg").val(avatar);
        $("#fileImg").val(null);
    }

    /* Account */
    function editAccount(type) {
        hideById("divBtnAccount");

        switch (type) {
            case "username":
                showById("lblTxtUsername");
                enableAllInputIn("lblTxtUsername");

                showById("lblTxtPassword");
                enableAllInputIn("lblTxtPassword");

                hideById("lblTxtOldPassword")
                disableAllInputIn("lblTxtOldPassword");

                showById("btnUpdateUsername");
                hideById("btnUpdatePassword");
                break;

            case "password":
                hideById("lblTxtUsername");
                disableAllInputIn("lblTxtUsername");

                showById("lblTxtPassword");
                enableAllInputIn("lblTxtPassword");

                showById("lblTxtOldPassword")
                enableAllInputIn("lblTxtOldPassword");

                hideById("btnUpdateUsername");
                showById("btnUpdatePassword");
                break;
        }
        showById("btnCancelEditAccount");

        cancelEditUserAvatar();
        cancelEditUserDetail();
        cancelEditUserAddress();
        cancelEditStaffCenter()
    }
    async function updateAccount(type) {
        let txtAccountMsg = $("#txtAccountMsg");

        let request = {
            "username" : $("#txtUsername").val(),
            "password" : $("#txtPassword").val(),
            "oldPassword" : $("#txtOldPassword").val()
        };

        let option;
        switch (type) {
            case "username":
                option = "/username";
                break;
            case "password":
                option = "/password";
                break;
        }

        await $.ajax({
            type: "PUT",
            url: "/api/staff-detail" + option,
            data: JSON.stringify(request),
            contentType: "application/json",
            success: function (response) {
                if (response["msg"] !== undefined) {
                    txtAccountMsg
                        .addClass("text-success")
                        .removeClass("text-danger visually-hidden")
                        .text(response["msg"]);

                    hideById("btnUpdateUsername");
                    hideById("btnUpdatePassword");
                    hideById("btnCancelEditAccount");

                    setTimeout(function() {location.href = response['view']; }, 4000);
                } else {
                    txtAccountMsg
                        .addClass("text-danger")
                        .removeClass("text-success visually-hidden")
                        .text(response["error"]);
                }
            }
        })
    }
    function cancelEditAccount() {
        showById("divBtnAccount");

        hideById("lblTxtUsername");
        disableAllInputIn("lblTxtUsername");
        $("#txtUsername").val(username);

        hideById("lblTxtPassword");
        disableAllInputIn("lblTxtPassword");
        $("#txtPassword").val(null);

        hideById("lblTxtOldPassword")
        disableAllInputIn("lblTxtOldPassword");
        $("#txtOldPassword").val(null);

        hideById("btnUpdateUsername");
        hideById("btnUpdatePassword");
        hideById("btnCancelEditAccount");
    }

    /* Detail */
    function editUserDetail() {
        enableAllInputIn("formDetail");
        enableAllSelectIn("formDetail");
        hideById("btnEditDetail");
        showById("btnUpdateDetail");
        showById("btnCancelEditDetail");

        cancelEditUserAvatar();
        cancelEditAccount();
        cancelEditUserAddress();
        cancelEditStaffCenter()
    }

    function cancelEditUserDetail() {
        disableAllInputIn("formDetail");
        disableAllSelectIn("formDetail");
        showById("btnEditDetail");
        hideById("btnUpdateDetail");
        hideById("btnCancelEditDetail");

        $("#txtFullName").val(userDetail['fullName']);
        $("#txtEmail").val(userDetail['email']);
        $("#txtPhone").val(userDetail['phone']);
        $("#selGender").val(userDetail['gender']);
    }

    /* Staff Center*/
    function editStaffCenter() {
        enableAllInputIn("formStaffCenter");
        enableAllSelectIn("formStaffCenter");
        hideById("btnEditStaffCenter");
        showById("btnUpdateStaffCenter");
        showById("btnCancelEditStaffCenter");

        cancelEditUserAvatar();
        cancelEditAccount();
        cancelEditUserDetail();
        cancelEditUserAddress();
    }

    function cancelEditStaffCenter() {
        disableAllInputIn("formStaffCenter");
        disableAllSelectIn("formStaffCenter");
        showById("btnEditStaffCenter");
        hideById("btnUpdateStaffCanter");
        hideById("btnCancelEditStaffCenter");

        $("#selCenter").val(staffCenter['staffCenter']);
        $("#selStaffType").val(staffCenter['staffType']);
    }
    /* Address */
    function editUserAddress() {
        enableAllInputIn("formAddress");
        enableById("selCountry");

        hideById("btnEditAddress");
        showById("btnUpdateAddress");
        showById("btnCancelEditAddress");

        cancelEditUserAvatar();
        cancelEditAccount();
        cancelEditUserDetail();
    }
    function cancelEditUserAddress() {
        disableAllInputIn("formAddress");
        disableAllSelectIn("formAddress");
        showById("btnEditAddress");
        hideById("btnUpdateAddress");
        hideById("btnCancelEditAddress");

        let txtAddrNo = $("#txtAddrNo");
        let street = $("#txtStreet");
        let selCountry = $("#selCountry");
        let selProvince = $("#selProvince");
        let selDistrict = $("#selDistrict");
        let selWard = $("#selWard");

        if (typeof levelUnitIdMap === "object" && typeof address === "object") {
            txtAddrNo.val(address['addressNo']);
            street.val(address['street']);

            selCountry.val(levelUnitIdMap['0']);
            selProvince.val(levelUnitIdMap['1']);

            selDistrict.val(levelUnitIdMap['2']);
            selWard.val(levelUnitIdMap['3']);
        } else {
            txtAddrNo.val('');
            street.val('');

            $("#selCountry option[value='0']").prop("selected", true);
            $("#selProvince option[value='0']").prop("selected", true);
            $("#selDistrict option[value='0']").prop("selected", true);
            $("#selWard option[value='0']").prop("selected", true);
        }
    }

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
                        enableAllSelectIn("formAddress");

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
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>
