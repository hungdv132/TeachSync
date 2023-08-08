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
    <form action="/homework/add-homework" method="post">
        <input type="hidden" name="homeworkId" value="${homework.id}">
        <div class="form-group">
            <label>Tên Bài tập</label>
            <c:if test="${option == 'detail'}">
                <input type="text" name="name"
                       value="${homework.homeworkName}"
                       disabled
                       class="form-control" placeholder="Nhập tên bài tập">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="text" name="name"
                       value="${homework.homeworkName}" required
                       class="form-control" placeholder="Nhập tên bài tập">
            </c:if>

        </div>
        <div class="row">
            <div class="col-md-4 pt-4">
                <div class="d-flex align-items-center">
                    <p class="mr-2">Tên lớp học : ${homework.clazzName}</p>
                    <div class="dropdown ms-3">
                        <c:if test="${option == 'edit' || option == 'add'}">
                            <select name="clazzId" required
                                    class="btn btn-secondary dropdown-toggle">
                                <c:forEach items="${clazzList}" var="clazz">
                                    <option value="${clazz.id}"> ${clazz.clazzName}</option>
                                </c:forEach>
                            </select>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label>Miêu tả về bài tập</label>
            <c:if test="${option == 'detail'}">
                <input type="text" disabled value="${homework.homeworkDesc}" name="desc" class="form-control"
                       placeholder="Nhập miêu tả">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="text" value="${homework.homeworkDesc}" name="desc" class="form-control"
                       placeholder="Nhập miêu tả">
            </c:if>
        </div>

        <div class="form-group">
            <label>File đính kèm</label>
            <c:if test="${option == 'detail'}">
                <c:if test="${homework.homeworkContent != null || homework.homeworkContent == ''}">
                    <br/>
                    <a href="${homework.homeworkContent}" target="_blank">
                        <button type="button" class="btn btn-primary">Xem</button>
                    </a>
                </c:if>
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <c:if test="${homework.homeworkContent != null || homework.homeworkContent == ''}">
                    <br/>
                    <a href="${homework.homeworkContent}" target="_blank">
                        <button type="button" class="btn btn-primary">Xem</button>
                    </a>
                </c:if>
                <input type="file" value="${homework.homeworkContent}" name="homeworkContent" class="form-control"
                       placeholder="File đính kèm file">
            </c:if>
        </div>

        <div class="form-group">
            <label>Link đính kèm</label>
            <c:if test="${option == 'detail'}">
                <c:if test="${homework.homeworkDoc != null || homework.homeworkDoc == ''}">
                    <br/>
                    <a href="${homework.homeworkDoc}" target="_blank">
                        <button type="button" class="btn btn-primary">Xem</button>
                    </a>
                </c:if>
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <c:if test="${homework.homeworkDoc != null || homework.homeworkDoc == ''}">
                    <br/>
                    <a href="${homework.homeworkDoc}" target="_blank">
                        <button type="button" class="btn btn-primary">Xem</button>
                    </a>
                </c:if>
                <input type="text" value="${homework.homeworkDoc}" name="homeworkDoc" class="form-control"
                       placeholder="đính kèm link">
            </c:if>
        </div>

        <div class="form-group">
            <label>Ngày mở</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${homework.openAt}" name="openAt" class="form-control" disabled
                       placeholder="Ngày mở nộp bài tập">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="datetime-local" value="${homework.openAt}" name="openAt" class="form-control"
                       placeholder="Ngày mở nộp bài tập">
            </c:if>
        </div>

        <div class="form-group">
            <label>Hạn nộp</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${homework.deadline}" name="deadline" class="form-control" disabled
                       placeholder="Ngày hết hạn">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">

                <input type="datetime-local" value="${homework.deadline}" name="deadline" class="form-control" required
                       placeholder="Ngày hết hạn">
            </c:if>
        </div>
        <br>
        <c:if test="${option == 'edit' || option == 'add'}">
            <button type="submit" class="btn btn-primary">Submit</button>
        </c:if>
        <p>Danh sách bài tập đã nộp</p>
        <c:forEach items="${homework.memberHomeworkRecordList}" var="homeworkRecord">
            <a href="detail-record-homework?id=${homeworkRecord.id}&homeworkId=${homework.id}" >${homeworkRecord.name}</a>
            <a href="delete-record-homework?id=${homeworkRecord.id}&homeworkId=${homework.id}" ><button type="button" class="btn btn-danger">Xóa</button></a>
            <br/>
        </c:forEach>
        <br/>
        <c:if test="${option == 'detail' && sessionScope.user.roleId == 1}">
            <a href="record-homework?id=${homework.id}">
                <button type="button" class="btn btn-primary">Nộp bài tập về nhà</button>
            </a>
        </c:if>
        <br><br>
    </form>


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