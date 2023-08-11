<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>News Detail</title>

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
          <a href="/index">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
        <li class="breadcrumb-item" aria-current="page">
          <a href="/news">
            Tin Tức
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          <c:out value="${news.newsTitle}"/>
        </li>
      </ol>
    </nav>
  </div>
</div>

<c:set var="currentUri" value="${requestScope['jakarta.servlet.forward.request_uri']}"/>
<c:set var="queryString" value="${requestScope['jakarta.servlet.forward.query_string']}"/>
<c:set var="targetUrl" scope="session" value="${currentUri}${not empty queryString ? '?'.concat(queryString) : ''}"/>
<!-- ================================================== Breadcrumb ================================================= -->


<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <!-- News List paging -->
    <div class="col-12 mb-3">
        <div class="row gy-3">

  <!-- News List paging -->
  <div class="col-12 mb-3">
    <div class="row gy-3">
<%--      <div class="col-sm-12 col-md-5 px-sm-3 pe-md-0">--%>
<%--        <img src="../../resources/img/engbook.jpg"--%>
<%--           class="rounded-2 border ts-border-blue w-100 h-auto">--%>
<%--      </div>--%>

            <div class="col-sm-12 col-md-7 px-3">
                <div class="card ts-border-yellow h-100">

                    <div class="card-header">
                        <h4 class="card-title">
                            <c:out value="${news.newsTitle}"/>
                        </h4>
                        <br/>
                        <h6>
<%--                            <c:out value="${news.author}"/>--%>
                            <%= (new java.util.Date()).toLocaleString()%>
                        </h6>

                    </div>

                    <div class="card-body">
                        <p class="card-text">
                            <img src="../../../resources/img/engbook.jpg"
                                 class="rounded-2 border ts-border-blue w-100 h-auto">
                        </p>
                    </div>
          <div class="card-body">
            <p class="card-text">
              <c:out value="${news.newsDesc}"/>
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