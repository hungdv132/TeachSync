<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Sửa lớp học</title>
  
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
<div class="row">
  <!-- Breadcrumb -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal px-5 mb-3">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
        <li class="breadcrumb-item">
          <a href="/index">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
        <li class="breadcrumb-item" aria-current="page">
          <a href="/clazz">
            Lớp học
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Sửa lớp học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  
  
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <%--@elvariable id="createDTO" type="com.teachsync.dtos.clazz.ClazzCreateDTO"--%>
    <form:form id="form"
               modelAttribute="createDTO" action="/edit-clazz" method="POST"
               cssClass="row">
      <input type="hidden" name="id" value="${clazz.id}">
  
      <h4>Sửa lớp học</h4>
      <br>
  
      <div class="col-12">
        <div class="row">
          <!-- Clazz Alias -->
          <div class="col-4 mb-3">
            <label for="txtAlias" class="form-label">Mã lớp:</label>
            <input id="txtAlias" name="clazzAlias"
                   type="text" minlength="1" maxlength="10" value="${clazz.clazzAlias}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
    
          <!-- Clazz Name -->
          <div class="col-8 mb-3">
            <label for="txtName" class="form-label">Tên lớp:</label>
            <input id="txtName" name="clazzName"
                   type="text" minlength="1" maxlength="45" value="${clazz.clazzName}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>

          <!-- Clazz Desc -->
          <div class="col-12 mb-3">
            <label for="txtADesc" class="form-label">Miêu tả về lớp học:</label>
            <textarea id="txtADesc" name="clazzDesc"
                      minlength="0" maxlength="9999"
                      class="form-control ts-border-grey" rows="3" style="resize: none"><c:out value="${clazz.clazzDesc}"/></textarea>
          </div>
          
        </div>
      </div>
      
      <div class="col-12">
        <div class="row">
          <!-- Clazz courseId -->
          <div class="col-4 mb-3">
            <label for="selCourseId" class="form-label">Khóa học:</label>
            <select id="selCourseId" name="courseId"
                    class="form-select ts-border-grey">
              <c:forEach items="${courseList}" var="course">
                <option value="${course.id}"><c:out value="${course.courseName}"/></option>
              </c:forEach>
            </select>
          </div>

          <!-- Clazz centerId -->
          <div class="col-4 mb-3">
            <label for="selCenterId" class="form-label">Cơ sở:</label>
            <select id="selCenterId" name="centerId"
                    class="form-select ts-border-grey"
                    onchange="refreshStaff()">
              <c:forEach items="${centerList}" var="center">
                <option value="${center.id}"><c:out value="${center.centerName}"/></option>
              </c:forEach>
            </select>
          </div>
          
          <!-- Clazz staffId (bound to Center) -->
          <div class="col-4 mb-3">
            <label for="selStaffId" class="form-label">Giáo viên:</label>
            <select id="selStaffId" name="staffId"
                    class="form-select ts-border-grey">
              <c:forEach items="${staffList}" var="staff">
                <option value="${staff.id}"><c:out value="${staff.user.fullName}"/></option>
              </c:forEach>
            </select>
          </div>
          
        </div>
      </div>
      
      <div class="col-12">
        <div class="row">
          <!-- Clazz maxCapacity -->
          <div class="col-sm-12 col-md-6 mb-3">
            <label for="numMinCapacity" class="form-label">Số học sinh tối thiểu: (Để mở lớp)</label>
            <input id="numMinCapacity" name="minCapacity"
                   type="number" min="1" max="${clazz.minCapacity}" step="1" value="${clazz.minCapacity}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>
  
          <!-- Clazz maxCapacity -->
          <div class="col-sm-12 col-md-6 mb-3">
            <label for="numMaxCapacity" class="form-label">Số học sinh tối đa:</label>
            <input id="numMaxCapacity" name="maxCapacity"
                   type="number" min="1" step="1" value="${clazz.maxCapacity}"
                   class="form-control ts-border-grey"
                   required="required">
          </div>

        </div>
      </div>
      
      <button type="submit" class="btn btn-primary">Gửi</button>
      
    </form:form>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }
</script>

<script>
    function refreshStaff() {
        let centerId = $("#selCenterId").val();

        $.ajax({
            type: "GET",
            url: "/api/staff?centerId=" + centerId,
            success: function (response) {
                let staffList = response['staffList'];

                let selStaffId = $("#selStaffId");
                selStaffId.empty();

                for (const staff of staffList) {
                    selStaffId.append('<option value="' + staff['id'] + '">' + (staff['user'])['fullName'] + '</option>')
                }
            }
        })
    }
</script>

<script id="script1">
    $("#selCourseId").val("${clazz.courseId}");

    $("#selCenterId").val("${clazz.centerId}");

    $("#selStaffId").val("${clazz.staffId}");

    $("#script1").remove(); /* Xóa thẻ script này sau khi xong */
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>