<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Bài kiểm tra</title>

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
                                <th>Người làm bài</th>
                                <th>Bài thi</th>
                                <th>Điểm số</th>
                                <th>Chỉnh sửa</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${testScore}" var="tests">
                                <tr>
                                    <td>
                                            ${tests.userTest}
                                    </td>
                                    <td>
                                            ${tests.type}
                                    </td>
                                    <td>
                                            ${tests.score}
                                    </td>
                                    <td>
                                        <c:if test="${testType eq 'essay'}">
                                            <a href="examine" class="btn btn-outline-primary mr-2"><i class="fas fa-plus"></i>Chấm bài</a>
                                        </c:if>
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
</body>


<!-- Course List paging -->

<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
</html>