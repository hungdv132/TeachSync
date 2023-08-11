<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>News List</title>

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
        <li class="breadcrumb-item active" aria-current="page">
          Tin Tức
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
    <h5 class="d-flex justify-content-between align-items-center mb-3">
      <span>Danh sách</span>
      <c:if test="${isAdmin}">
        <a href="create-news" class="btn btn-primary">
          Thêm mới
        </a>
      </c:if>
    </h5>

    <div class="row gy-3 mb-3">
      <c:forEach var="news" items="${newsList}">
        <div class="col-12">
          <div class="row px-3">
              <%--      <div class="col-2 rounded-start-2 border ts-border-orange overflow-hidden px-0">--%>
              <%--&lt;%&ndash;        <img src="../../resources/img/engbook.jpg" class="w-100 h-auto">&ndash;%&gt;--%>
              <%--      </div>--%>

            <div class="col-10 px-0">
              <div class="card rounded-start-0 border-start-0 ts-border-orange h-100">
                <div class="card-header">
                  <h5 class="card-title mb-0">
                    <c:url var="newsLink" value="news-detail">
                      <c:param name="id" value="${news.id}"/>
                    </c:url>
                    <a href="${newsLink}">
                      <c:out value="${news.newsTitle}"/>
                    </a>
                  </h5>
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
      </c:forEach>
    </div>
  </div>
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>