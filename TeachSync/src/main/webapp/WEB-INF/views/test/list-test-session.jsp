<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Course List</title>

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


<!-- ================================================== Main Body ================================================== -->

<div class="page-header">
    <div class="row">
        <div class="col">
            <h3 class="page-title">Danh sách test</h3>
        </div>
        <div class="top-nav-search">
            <form id="login-form" name="myform" action="search-test-session" method="get"
                  onsubmit="return validateform()">

                <input type="text" class="form-control" placeholder="Search here" name="searchText">
                <input type="radio" name="searchType" value="class" checked>Lớp<br>
                <input type="radio" name="searchType" value="subject">Môn học<br>
                <input type="radio" name="searchType" value="user">Username<br>
                <button class="btn" type="submit"><i class="fas fa-search"></i></button>
            </form>
        </div>
    </div>
</div>
<table>
    <tbody>

    <div class="row">
        <div class="col-sm-12">
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="datatable table table-stripped">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Người làm bài</th>
                                <th>Môn học</th>
                                <th>Lớp học</th>
                                <th>Trạn thái</th>
                                <th>Thời gian bắt đầu</th>
                                <th>Thời gian kết thúc</th>
                                <th>Thời gian cập nhật</th>
                                <th>Người cập nhật</th>
                                <th>Chuyển trạng thái</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${tests.content}" var="tests">
                                <tr>
                                    <td>
                                            ${tests.id}
                                    </td>
                                    <td>
                                            ${tests.username}
                                    </td>
                                    <td>
                                            ${tests.subject}
                                    </td>
                                    <td>
                                            ${tests.clazz}
                                    </td>
                                    <td>
                                        <c:if test="${tests.status == 0}">
                                            <span
                                                    class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md "
                                            ><a> Cho phép làm lại</a></span>
                                        </c:if>
                                        <c:if test="${tests.status == 1}">
                                            <span
                                                    class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md "
                                            ><a> Đang làm</a></span>
                                        </c:if>
                                        <c:if test="${tests.status == 2}">
                                            <span
                                                    class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md "
                                            ><a> Đã hoàn thành</a></span>
                                        </c:if>
                                        <c:if test="${tests.status == 3}">
                                            <span
                                                    class="inline-flex px-5 py-2 font-semibold leading-5 text-green-800 bg-green-100 rounded-lg text-md "
                                            ><a> Tạm thời đình chỉ</a></span>
                                        </c:if>
                                    </td>
                                    <td>
                                            ${tests.startDate}
                                    </td>
                                    <td>
                                            ${tests.submitDate}
                                    </td>
                                    <td>
                                            ${tests.updateDate}
                                    </td>
                                    <td>
                                            ${tests.userUpdate}
                                    </td>
                                    <td>
                                        <a href="update-test-sessison?idSession=${tests.id}&newStatus=0" class="btn btn-outline-primary mr-2"><i class="fas fa-plus"></i>Làm lại</a>
                                        <a href="update-test-sessison?idSession=${tests.id}&newStatus=3" class="btn btn-outline-primary mr-2"><i class="fas fa-plus"></i>Đình chỉ</a>
                                    </td>
                                </tr>
                            </c:forEach>

                            </tbody>
                        </table>
                    </div>

                </div>

            </div>
        </div>
    </div>


    </tbody>
</table>

<div class="d-flex align-items-center mb-3">
    <a href="/test-sessison?page=0" class="btn btn-secondary"><i class="bi-chevron-bar-left"></i></a>
    <a href="/test-sessison?page=${pageNo-1}" class="btn btn-secondary mx-2"><i class="bi-chevron-left"></i></a>
    Page: <c:out value="${pageNo + 1}"/> &sol; <c:out value="${pageTotal}"/>
    <a href="/test-sessison?page=${pageNo + 1}" class="btn btn-secondary mx-2"><i class="bi-chevron-right"></i></a>
    <a href="/test-sessison?page=${pageTotal-1}" class="btn btn-secondary"><i class="bi-chevron-bar-right"></i></a>
</div>
</body>


<!-- Course List paging -->

<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>