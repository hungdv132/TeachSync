<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Certificate</title>

    <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../../resources/css/certificate_style.css">
    <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
    <script src="../../../resources/js/common.js"></script>
    <style>
        .center-list {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .center-item {
            width: 500px;
            height: 550px;
            padding: 20px;
            margin: 20px;
            background-color: #e8e8e8;
            border-radius: 10px;
            text-align: center;
            font-size: 24px;
        }


        h2 {
            font-size: 20px;
        }

        p {
            font-size: 16px;
            margin-top: 10px;
        }

    </style>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<script>

</script>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 mx-2 mb-3">
    <div class="col">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
                <li class="breadcrumb-item">
                    <a href="/index">
                        <i class="bi-house-door"></i>&nbsp;Trang chủ
                    </a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    Trung tâm
                </li>
            </ol>
        </nav>
    </div>
</div>
<div class="center-list">
<c:forEach var="item" items="${centerList}">

    <a href="/center-detail?id=${item.id}">
        <div class="center-item">
            <h2 value="${item.id}">${item.centerName}</h2>
            <p>Địa chỉ: ${item.address.addressString}</p>
            <img src="https://amore-architecture.vn/wp-content/uploads/2021/12/TTTA-GCE-tp-HCM-1.jpg" width="300px"  height="400px" >
        </div>
    </a>
</c:forEach>


</div>

<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>
