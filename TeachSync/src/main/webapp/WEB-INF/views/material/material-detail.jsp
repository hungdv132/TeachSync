<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Material Detail</title>

    <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">

    <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

    <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

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
                    <a href="/">
                        <i class="bi-house-door"></i>&nbsp;Trang chủ
                    </a>
                </li>
                <li class="breadcrumb-item" aria-current="page">
                    <a href="/material">
                        Tài liệu
                    </a>
                </li>
                <li class="breadcrumb-item active" aria-current="page">
                    <c:out value="${material.materialName}"/>
                </li>
            </ol>
        </nav>
    </div>
</div>
<!-- ================================================== Breadcrumb ================================================= -->



<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <!-- Material List paging -->
    <div class="col-12 mb-3">
        <div class="row gy-3">
            <div class="col-sm-12 col-md-3 px-sm-3 pe-md-0">
                <img src="${material.materialImg}" class="rounded-2 border ts-border-blue w-100 h-auto">
            </div>

            <div class="col-sm-12 col-md-7 px-3">
                <div class="card ts-border-yellow h-100">

                    <div class="card-header">
                        <h4 class="card-title">
                            <c:out value="${material.materialName}"/>
                        </h4>
                        <c:if test="${isAdmin}">
                            <a href="edit-material?id=${material.id}" class="btn btn-warning">
                                Chỉnh sửa
                            </a>
                            <a href="delete-material?id=${material.id}" class="btn btn-danger">
                                Xóa
                            </a>
                        </c:if>
                        <br/>
                        <h6>

                        </h6>

                    </div>

                    <div class="card-body">
                        <p class="card-text">
                            Type:
                            <c:out value="${material.materialType}"/>
                            <br/>
                            Link:
                            <a href="${material.materialLink}">
                                <c:out value="${material.materialLink}"/>
                            </a>
                        </p>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<!-- ================================================== Main Body ================================================== -->



<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>