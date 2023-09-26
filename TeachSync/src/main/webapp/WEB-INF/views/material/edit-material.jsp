<%@ page import="com.teachsync.utils.enums.MaterialType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Thêm mới tài liệu</title>

    <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

    <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

    <!-- Import the SDKs you need -->
    <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-storage.js"></script>
    <script src="../../../resources/js/firebase.js"></script>

    <script src="../../resources/js/common.js"></script>
</head>
<body class="min-vh-100 container-fluid d-flex flex-column ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <form onsubmit="editMaterial(event)" class="col-12 d-flex justify-content-center px-5 mb-3">

                <p class="ms-5 mb-0">Tên khóa học</p>
                <div class="dropdown ms-3">
                    <select class="btn btn-secondary dropdown-toggle"
                            id="selCourseId" name="courseId">
                        <c:forEach items="${courseList}" var="course">
                            <option value="${course.id}"> ${course.courseName}</option>
                        </c:forEach>
                    </select>
                </div>


        <div class="form-group">
            <label>Tên tài liệu</label>
            <input type="text" name="materialName"
                   id="txtMaterialName"
                   required
                   class="form-control" placeholder="Nhập tên tài liệu">
        </div>

        <div class="form-group">
            <label>Link tài liệu </label>
            <input type="text" name="materialLink"
                   id="txtMaterialLink"
                   required
                   class="form-control" placeholder="Nhập link tài liệu">
        </div>

        <input type="hidden" id="txtMaterialContent" name="materialContent" value="${material.materialContent}">


        <div class="col-sm-12 col-md-3 mb-3">
            <label class="w-100">
                Ảnh tài liệu: <br/>
                <img src="${empty material.materialImg ? '../../../resources/img/no-img.jpg' : material.materialImg}" alt="materialImg" id="imgMaterialImg"
                     class="rounded-2 border ts-border-blue w-100 h-auto mb-3">
                <br/>
                <input type="file" name="img" id="txtMaterialImg" class="w-100"
                       accept="image/*" onchange="updateImgFromInput('txtImg', 'imgMaterialImg', 0.75)">
            </label>
            <p class="ts-txt-italic ts-txt-sm mb-0">*Tối đa 0.75 MB</p>
        </div>


        <div class="form-group mb-3">
            <label class="dropdown">Kiểu tài liệu: <br>
                <select class="btn btn-secondary dropdown-toggle"
                        id="selmaterialType" name="materialType">
                    <option value="${MaterialType.E_BOOK}">${MaterialType.E_BOOK.stringValueVie}</option>
                    <option value="${MaterialType.EXCEL}">${MaterialType.EXCEL.stringValueVie}</option>
                    <option value="${MaterialType.PDF}">${MaterialType.PDF.stringValueVie}</option>
                    <option value="${MaterialType.POWER_POINT}">${MaterialType.POWER_POINT.stringValueVie}</option>
                    <option value="${MaterialType.WORD}">${MaterialType.WORD.stringValueVie}</option>
                </select>
            </label>
        </div>

        <div class="form-group">
            <label>
                Tài liệu free
                <input type="checkbox" id="chkisFree" name="isFree" value="yes">
            </label>
        </div>


        <button type="submit" class="btn btn-primary">Submit</button>
    </form>


</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }

    function editMaterial(event) {
        event.preventDefault();

        let file = $('#fileImg').prop("files")[0];

        let imgURL = await uploadImageFileToFirebaseAndGetURL(file);


        let createDTO = {
            "id": ${material.id},
            "materialName": $("#txtMaterialName").val(),
            "materialLink": $("#txtMaterialLink").val(),
            "materialImg": imgURL,
            "materialContent": $("#txtMaterialContent").val(),
            "materialType": $("#selMaterialType").val(),
            "isFree": $("#chkisFree").val(),
        }

        $.ajax({
            type: "PUT",
            data: JSON.stringify(createDTO),
            url: "/edit-material",
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
</body>
</html>