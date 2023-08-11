<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Chỉnh lớp học</title>
  
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
  <form action="add-clazz" method="post">
    <input type="hidden" name="clazzId" id="txtId" value="${clazz.id}">
    <div class="form-group mb-3">
      <label>Tên lớp
      <input type="text" name="name" id="txtName" class="ms-3"
             value="${clazz.clazzName}" placeholder="Nhập tên lớp">
      </label>
    </div>
    
    <div class="row mb-3">
      <div class="col-12">
        <div class="d-flex align-items-center">
          
          <p class="mb-0">Khóa học</p>
          <div class="dropdown ms-3">
            <select name="courseId" id="courseIdSel" class="btn btn-secondary dropdown-toggle">
              <c:forEach items="${courseList}" var="course">
                <option value="${course.id}"> ${course.courseName}</option>
              </c:forEach>
            </select>
            
            <c:if test="${option == 'edit'}">
              <script id="script1">
                  $("#courseIdSel").val(${clazz.courseSemester.courseId});
                  $("#script1").remove(); /* Xóa thẻ script này sau khi xong */
              </script>
            </c:if>
          </div>
  
          <p class="ms-5 mb-0">Học kỳ</p>
          <div class="dropdown ms-3">
            <select name="semesterId" id="semesterIdSel" class="btn btn-secondary dropdown-toggle">
              <c:forEach items="${semesterList}" var="semester">
                <option value="${semester.id}"> ${semester.semesterAlias}</option>
              </c:forEach>
            </select>
  
            <c:if test="${option == 'edit'}">
              <script id="script2">
                  $("#semesterIdSel").val(${clazz.courseSemester.semesterId});
                  $("#script2").remove(); /* Xóa thẻ script này sau khi xong */
              </script>
            </c:if>
          </div>
  
          <p class="ms-5 mb-0">Cơ sở</p>
          <div class="dropdown ms-3">
            <select name="centerId" id="centerIdSel" class="btn btn-secondary dropdown-toggle"
                    onchange="refreshStaff()">
              <c:forEach items="${centerList}" var="center">
                <option value="${center.id}"> ${center.centerName}</option>
              </c:forEach>
            </select>
  
            <c:if test="${option == 'edit'}">
              <script id="script3">
                  $("#centerIdSel").val(${clazz.courseSemester.centerId});
                  $("#script3").remove(); /* Xóa thẻ script này sau khi xong */
              </script>
            </c:if>
          </div>
  
          <p class="ms-5 mb-0">Giáo viên</p>
          <div class="dropdown ms-3">
            <select name="staffId" id="staffIdSel" class="btn btn-secondary dropdown-toggle">
              <c:forEach items="${staffList}" var="staff">
                <option value="${staff.id}"> ${staff.user.fullName}</option>
              </c:forEach>
            </select>
  
            <script id="script4">
                $("#staffIdSel").val(${clazz.staffId});
                $("#script4").remove(); /* Xóa thẻ script này sau khi xong */
            </script>
          </div>

        </div>
      </div>
    </div>
  
    <div class="form-group mb-3">
      <label>Miêu tả
        <input type="text" name="desc" id="txtDesc" class="ms-3 w-50"
               value="${clazz.clazzDesc}" placeholder="Nhập miêu tả">
      </label>
    </div>
    
    <div class="form-group">
      <label>Dung lượng học sinh
        <input type="number" name="size" id="txtSize" class="ms-3 w-50"
               value="${clazz.clazzSize}" placeholder="Nhập dung lượng">
      </label>
    </div>
    
    <br>
  
    <c:if test="${option == 'add'}">
      <button type="button" class="btn btn-primary" onclick="addClazz()">Submit</button>
    </c:if>
    
    <c:if test="${option == 'edit'}">
      <button type="button" class="btn btn-primary" onclick="editClazz()">Submit</button>
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
    
    function refreshStaff() {
      let centerId = $("#centerIdSel").val();
      
      $.ajax({
          type: "GET",
          url: "/api/staff?centerId=" + centerId ,
          success: function(response) {
              let staffList = response['staffList'];

              let staffIdSel = $("#staffIdSel");
              staffIdSel.empty();
              
              for (const staff of staffList) {
                  staffIdSel.append('<option value="' + staff['id'] + '">' + (staff['user'])['fullName'] + '</option>')
              }
          }
      })
    }

    function addClazz() {
        let createDTO = {
            "staffId" : $("#staffIdSel").val(),
            "courseId" : $("#courseIdSel").val(),
            "semesterId" : $("#semesterIdSel").val(),
            "centerId" : $("#centerIdSel").val(),
            "clazzName" : $("#txtName").val(),
            "clazzDesc" : $("#txtDesc").val(),
            "clazzSize" : $("#txtSize").val(),
        }

        $.ajax({
            type: "POST",
            data: JSON.stringify(createDTO),
            url: "/add-clazz",
            contentType: "application/json",
            success: function(response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })
    }

    function editClazz() {
        let updateDTO = {
            "id": $("#txtId").val(),
            "staffId" : $("#staffIdSel").val(),
            "courseId" : $("#courseIdSel").val(),
            "semesterId" : $("#semesterIdSel").val(),
            "centerId" : $("#centerIdSel").val(),
            "clazzName" : $("#txtName").val(),
            "clazzDesc" : $("#txtDesc").val(),
            "clazzSize" : $("#txtSize").val(),
        }

        $.ajax({
            type: "PUT",
            data: JSON.stringify(updateDTO),
            url: "/edit-clazz",
            contentType: "application/json",
            success: function(response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })
    }
</script>
</html>