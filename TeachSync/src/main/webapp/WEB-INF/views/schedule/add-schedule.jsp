<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Chỉnh lịch học</title>

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
    <form action="add-schedule" method="post">
        <input type="hidden" name="clazzScheduleId" id="txtId" value="${clazzSchedule.id}">

        <div class="row mb-3">
            <div class="col-12">
                <div class="d-flex align-items-center">

                    <p class="mb-0">Tên lớp</p>
                    <div class="dropdown ms-3">
                        <select name="clazzId" id="clazzIdSel" class="btn btn-secondary dropdown-toggle">
                            <c:forEach items="${clazzList}" var="clazz">
                                <option value="${clazz.id}"> ${clazz.clazzName}</option>
                            </c:forEach>
                        </select>

                        <c:if test="${option == 'edit'}">
                            <script id="script1">
                                $("#clazzIdSel").val(${clazz.clazzId});
                                $("#script1").remove(); /* Xóa thẻ script này sau khi xong */
                            </script>
                        </c:if>
                    </div>

                    <p class="ms-5 mb-0">Tên phòng</p>
                    <div class="dropdown ms-3">
                        <select name="roomId" id="roomIdSel" class="btn btn-secondary dropdown-toggle">
                            <c:forEach items="${roomList}" var="room">
                                <option value="${room.id}"> ${room.roomName}</option>
                            </c:forEach>
                        </select>

                        <c:if test="${option == 'edit'}">
                            <script id="script2">
                                $("#roomIdSel").val(${room.roomId});
                                $("#script2").remove(); /* Xóa thẻ script này sau khi xong */
                            </script>
                        </c:if>
                    </div>


                </div>
            </div>
        </div>


        <div class="form-group mb-3">
            <label>Kiểu lịch học
                <input type="text" name="type" id="txtType" class="ms-3"
                       value="${clazzSchedule.scheduleType}" placeholder="Nhập kiểu lịch học">
            </label>
        </div>

        <div class="form-group mb-3">
            <label>Slot
                <input type="text" name="slot" id="txtSlot" class="ms-3"
                       value="${clazzSchedule.slot}" placeholder="Nhập slot">
            </label>
        </div>

        <div class="form-group">
            <label>Thời gian bắt đầu</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${clazzSchedule.startDate}" name="startDate" id="dlStart" class="form-control"
                       disabled
                       placeholder="Thời gian bắt đầu">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="datetime-local" value="${clazzSchedule.startDate}" name="startDate" id="dlStart" class="form-control"
                       required
                       placeholder="Thời gian bắt đầu">
            </c:if>
        </div>

        <div class="form-group">
            <label>Thời gian kết thúc</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${clazzSchedule.endDate}" name="endDate" id="dlEnd" class="form-control"
                       disabled
                       placeholder="Thời gian kết thúc">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="datetime-local" value="${clazzSchedule.endDate}" name="endDate" id="dlEnd" class="form-control"
                       required
                       placeholder="Thời gian kết thúc">
            </c:if>
        </div>

        <br>

        <c:if test="${option == 'add'}">
            <button type="button" class="btn btn-primary" onclick="addClazzSchedule()">Submit</button>
        </c:if>

        <c:if test="${option == 'edit'}">
            <button type="button" class="btn btn-primary" onclick="editClazzSchedule()">Submit</button>
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


    function addClazzSchedule() {
        let createDTO = {
            "clazzId": $("#clazzIdSel").val(),
            "roomId": $("#roomIdSel").val(),
            "scheduleType": $("#txtType").val(),
            "slot": $("#txtSlot").val(),
            "startDate": $("#dlStart").val(),
            "endDate": $("#dlEnd").val(),
        }

        $.ajax({
            type: "POST",
            data: JSON.stringify(createDTO),
            url: "/add-schedule",
            contentType: "application/json",
            success: function (response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })
    }

    function editClazzSchedule() {
        let updateDTO = {
            "id": $("#txtId").val(),
            "clazzId": $("#clazzIdSel").val(),
            "roomId": $("#roomIdSel").val(),
            "scheduleType": $("#txtType").val(),
            "slot": $("#txtSlot").val(),
            "startDate": $("#dlStart").val(),
            "endDate": $("#dlEnd").val(),
        }

        $.ajax({
            type: "PUT",
            data: JSON.stringify(updateDTO),
            url: "/edit-clazzSchedule",
            contentType: "application/json",
            success: function (response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })
    }
</script>
</html>