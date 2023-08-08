<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Danh sách lớp học</title>

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
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Tên user</th>
            <th scope="col">Họ và tên</th>
            <th scope="col">Link đính kèm</th>
            <th scope="col">File upload</th>
            <th scope="col">Trạng thái đơn</th>
            <th scope="col">Ngày nộp đơn</th>
            <c:if test="${isAdmin}">
                <th scope="col">Chức năng</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="teacherQuest" items="${teacherQuestList}">
            <tr>
                <th scope="row">${teacherQuest.id}</th>
                <td><a href="detail-application?id=${teacherQuest.applicationDetail.id}" >${teacherQuest.applicant.username}</a></td>
                <td>${teacherQuest.applicant.fullName}</td>
                <td>${teacherQuest.applicationDetail.detailLink}</td>
                <td>${teacherQuest.applicationDetail.detailNote}</td>
                <td>${teacherQuest.result}</td>
                <td>${teacherQuest.appliedAtShow}</td>
                <c:if test="${isAdmin}">
                    <td>

                        <c:if test="${teacherQuest.result eq 'Đang chờ duyệt'}">
                            <a href="change-status?id=${teacherQuest.id}&operation=approve">
                                <button type="button" class="btn btn-success">Chấp nhận</button>
                            </a>
                            <a href="change-status?id=${teacherQuest.id}&operation=reject">
                                <button type="button" class="btn btn-danger">Từ chối</button>
                            </a>
                        </c:if>


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
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }
</script>
</html>