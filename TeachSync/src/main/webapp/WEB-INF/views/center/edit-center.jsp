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


<div class="detail-container">
    <img src="https://amore-architecture.vn/wp-content/uploads/2021/12/TTTA-GCE-tp-HCM-1.jpg" id="centerImage">
    <div class="info-container">
        <label>Tên trung tâm:</label>
        <input type="text" id="centerName" value="${center.centerName}">
        <label>Chuyên môn:</label>
        <input type="text" id="functionInfo" value="${center.centerType}">
        <label>Địa chỉ:</label>
        <input type="text" id="centerAddress" value="${address.addressString}">
        <label>Số phòng:</label>
        <input type="text" id="centerSize" value="${center.centerSize}">

        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>

        <a>
            <button type="submit" id="submit">Chỉnh sửa</button>
        </a>
    </div>
</div>

<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>
