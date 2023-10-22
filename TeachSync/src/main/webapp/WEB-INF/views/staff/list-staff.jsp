<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Danh sách nhân viên</title>

    <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

    <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
    <script src="../../../resources/js/common.js"></script>
</head>
<body class="min-vh-100 container-fluid d-flex flex-column ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <c:if test="${isAdmin}">
        <a>
            <button type="button" class="btn btn-primary">Thêm mới nhân viên</button>
        </a>
    </c:if>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Ảnh đại diện</th>
            <th scope="col">Tên nhân viên</th>
            <th scope="col">Tên tài khoản</th>
            <th scope="col">Chức vụ</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="staff" items="${staffList}">
            <tr>
                <th scope="row">${staff.id}</th>
                <td><a href="/staff-detail?id=${staff.id}"><img src="${staff.user.userAvatar}" alt="avatar"
                         class="rounded-1 border ts-border-yellow w-35 h-auto"></a></td>
                <td><a style="font-weight: bold;" href="/staff-detail?id=${staff.id}">${staff.user.fullName}</a></td>
                <td>${staff.user.username}</td>
                <td>${staff.staffType}</td>
                <c:if test="${isAdmin}">
                    <td>
                        <a href="/staff-detail?id=${staff.id}">
                            <button type="button" class="btn btn-success">Sửa</button>
                        </a>
                        <a href="/delete-staff?id=${staff.id}">
                            <button type="button" class="btn btn-danger">Xóa</button>
                        </a>
                    </td>
                </c:if>
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
    var mess = `${mess}`
    if (mess != '') {
        alert(mess);
    }
</script>
</html>