<%@ page import="com.teachsync.utils.enums.CenterType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Tạo phòng học</title>

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
                    <a href="/list-center">Danh sách phòng</a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    Tạo Phòng Học
                </li>
            </ol>
        </nav>
    </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 px-5 mx-2 mb-3">
    <form class="col-12" id="form" action="/add-center" method="POST">
        <div class="row mb-3">
            <h4>Sửa Phòng Học</h4>
            <br>
            <input type="hidden" name="id" value="${room.id}">


            <!-- Room detail -->
            <div class="col-sm-12 col-md-12 mb-3">

                <!-- Room name, type, size -->
                <div class="row mb-3">
                    <label class="col-4">Tên phòng: <br/>
                        <input class="w-100" type="text"
                               id="roomName" name="roomName" value="">
                    </label>

                    <label class="col-4">Loại phòng <br/>
                        <select class="w-100 py-1"
                                id="roomType" name="roomType" >
                            <option value="${room.roomType}">CLASSROOM</option>
                        </select>
                    </label>

                    <label class="col-4">Sức chứa <br/>
                        <input class="w-100" type="number" min="1" max="999"
                               id="roomSize" name="roomSize" value="${room.roomSize}">
                    </label>
                </div>

                <!-- Room desc -->
                <label class="w-100 mb-3">
                    Miêu tả về phòng học: <br/>
                    <textarea class="w-100" style="resize: none" rows="3"
                              id="roomDesc" name="roomDesc" >${room.roomDesc}</textarea>
                </label>

            </div>

            <div class="col-12 d-flex justify-content-center">
                <button type="button" id="submit" class="btn btn-primary w-50">Thêm mới</button>
            </div>
        </div>
    </form>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


</body>
</html>
