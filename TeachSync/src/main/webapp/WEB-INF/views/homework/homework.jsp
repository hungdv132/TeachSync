<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>About</title>

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
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <c:if test="${sessionScope.user.roleId eq  4 || sessionScope.user.roleId eq  3}">
        <a href="add-homework">
            <button type="button" class="btn btn-primary">Thêm mới bài tập về nhà</button>
        </a>
    </c:if>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Lớp</th>
            <th scope="col">Tên bài tập</th>
            <th scope="col">Miêu tả</th>
            <th scope="col">File đính kèm</th>
            <th scope="col">Link đính kèm</th>
            <th scope="col">Thời hạn</th>
            <th scope="col">Ngày mở</th>
            <th scope="col">Chức năng</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="homework" items="${homeworkList}">
            <tr>
                <th scope="row">${homework.id}</th>
                <td>
                    <a href="detail-homework?id=${homework.id}">
                            ${homework.clazzName}
                    </a>
                </td>
                <td>${homework.homeworkName}</td>
                <td>${homework.homeworkDesc}</td>
                <td>${homework.homeworkDoc}</td>
                <td>${homework.homeworkContent}</td>
                <td>${homework.deadline}</td>
                <td>${homework.openAt}</td>
                <td>
                    <c:if test="${sessionScope.user.roleId eq  4 || sessionScope.user.roleId eq  3}">
                        <a href="add-homework?id=${homework.id}">
                            <button type="button" class="btn btn-success">Sửa</button>
                        </a>
                        <a href="delete-homework?id=${homework.id}">
                            <button type="button" class="btn btn-danger">Xóa</button>
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }
</script>
</html>