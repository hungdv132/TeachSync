<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Thêm chiến dịch tuyển dụng</title>

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
    <p style="color:red">${mess}</p>
    <form action="add" method="post">
        <input type="hidden" name="id" id="txtId" value="${campaign.id}">
        <div class="form-group mb-3">
            <label>Tên chiến dịch
                <c:if test="${option == 'detail'}">
                    <input type="text" name="name" class="ms-3" disabled
                           value="${campaign.campaignName}" placeholder="Nhập tên chiến dịch">
                </c:if>
                <c:if test="${option == 'edit' || option == 'add'}">
                    <input type="text" name="name" class="ms-3" required
                           value="${campaign.campaignName}" placeholder="Nhập tên chiến dịch">
                </c:if>

            </label>
        </div>
        <div class="form-group">
            <label>Trung Tâm : ${campaign.center.centerName}</label>
            <div class="dropdown ms-3">
                <c:if test="${option == 'edit' || option == 'add'}">
                    <select name="centerId" class="btn btn-secondary dropdown-toggle" required>
                        <c:forEach items="${centerList}" var="center">
                            <option value="${center.id}"> ${center.centerName}</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
        </div>
        <div class="form-group">
            <label>Ảnh</label>
            <c:if test="${campaign.campaignImg != null && campaign.campaignImg != ''}">
                <img src="${campaign.campaignImg}" width="30%" height="30%" alt="">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="file" name="campaignImg" class="form-control" value="${campaign.campaignImg}">
            </c:if>
        </div>
        <div class="form-group">
            <label>Miêu tả</label>
            <c:if test="${option == 'detail'}">
                <input type="text" name="campaignDesc" disabled class="form-control" value="${campaign.campaignDesc}">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="text" name="campaignDesc" required class="form-control" value="${campaign.campaignDesc}">
            </c:if>

        </div>
        <div class="form-group">
            <label>Vị trí</label>
            <c:if test="${option == 'detail'}">
                <input type="text" name="position" disabled class="form-control" value="${campaign.position}">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="text" name="position" required class="form-control" value="${campaign.position}">
            </c:if>
        </div>
        <div class="form-group">
            <label>Số lượng</label>
            <c:if test="${option == 'detail'}">
                <input type="number" name="openingSlot" disabled class="form-control" value="${campaign.openingSlot}">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="number" name="openingSlot" required class="form-control" value="${campaign.openingSlot}">
            </c:if>
        </div>
        <div class="form-group">
            <label>Từ ngày</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${campaign.recruitFrom}" name="recruitFrom" class="form-control"
                       disabled
                       placeholder="Từ ngày mở chiến dịch">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="datetime-local" value="${campaign.recruitFrom}" name="recruitFrom" class="form-control"
                       required
                       placeholder="Từ ngày mở chiến dịch">
            </c:if>
        </div>
        <div class="form-group">
            <label>Đến ngày</label>
            <c:if test="${option == 'detail'}">
                <input type="datetime-local" value="${campaign.recruitTo}" name="recruitTo" class="form-control"
                       disabled
                       placeholder="Từ ngày mở chiến dịch">
            </c:if>
            <c:if test="${option == 'edit' || option == 'add'}">
                <input type="datetime-local" value="${campaign.recruitTo}" name="recruitTo" class="form-control"
                       required
                       placeholder="Từ ngày mở chiến dịch">
            </c:if>
        </div>
        <br>
        <c:if test="${option == 'edit' || option == 'add'}">
            <button type="submit" class="btn btn-primary">Submit</button>
        </c:if>
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