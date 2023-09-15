<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Chi tiết lớp học</title>
  
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
  <!-- Detail -->
  <div class="col-12">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h5 class="mb-0">Tên lớp: ${clazz.clazzName}</h5>
      
      <c:if test="${isAdmin}">
        <c:url var="addClazz" value="/add-clazz">
          <c:param name="id" value="${clazz.id}"/>
          <c:param name="option" value="edit"/>
        </c:url>
        <a href="${addClazz}" class="btn btn-warning">Sửa</a>
      </c:if>
    </div>
    
    <p>Cơ sở: ${clazz.courseSemester.center.centerName}</p>
    
    <p>Giáo viên: ${clazz.staff.user.fullName}</p>
    
    <p>Khóa học: ${clazz.courseSemester.courseAlias} - ${clazz.courseSemester.courseName}</p>
    
    <p>Học kỳ: ${clazz.courseSemester.semester.semesterAlias} - ${clazz.courseSemester.semester.semesterName}</p>
    
    <p>Miêu tả: ${clazz.clazzDesc}</p>
    
    <c:if test="${isAdmin}">
      <p>Trạng thái: ${statusLabelMap[clazz.statusClazz]}</p>
    </c:if>
    
    <p>Dung lượng học sinh: ${clazz.clazzSize}</p>
  </div>
  
  <!-- Dependency -->
  <div class="col-12 mb-3">
    <!-- Clazz dependency tab -->
    <ul class="nav nav-tabs align-items-center" id="clazzDependencyTab" role="tablist">
      <!-- Tab Material -->
      <li class="nav-item" role="presentation">
        <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue active"
                data-bs-toggle="tab" role="tab" aria-selected="true"
                id="material-tab" data-bs-target="#material-tab-pane" aria-controls="material-tab-pane">
          Tài liệu
        </button>
      </li>
      
      <!-- Tab Test -->
      <li class="nav-item" role="presentation">
        <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                data-bs-toggle="tab" role="tab" aria-selected="false"
                id="test-tab" data-bs-target="#test-tab-pane" aria-controls="test-tab-pane">
          Bài kiểm tra
        </button>
      </li>
      
      <!-- Tab Homework -->
      <li class="nav-item" role="presentation">
        <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                data-bs-toggle="tab" role="tab" aria-selected="false"
                id="homework-tab" data-bs-target="#homework-tab-pane" aria-controls="homework-tab-pane">
          Bài tập
        </button>
      </li>
    </ul>
    
    <!-- Clazz dependency tab content -->
    <div class="tab-content border border-top-0 rounded-bottom-3 pt-3 px-3" id="semesterTabContent">
      <!-- Tab Material TabPane -->
      <div class="tab-pane fade active show" role="tabpanel"
           id="material-tab-pane" aria-labelledby="material-tab">
        <p>E-Book: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>NYT article: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
      </div>
      
      <!-- Tab Test TabPane -->
      <div class="tab-pane fade" role="tabpanel"
           id="test-tab-pane" aria-labelledby="test-tab">
        <p>Bài 1: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 2: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 3: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 4: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
      </div>
      
      <!-- Tab Homework TabPane -->
      <div class="tab-pane fade" role="tabpanel"
           id="homework-tab-pane" aria-labelledby="homework-tab">
        <p>Bài 1: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 2: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 3: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
        
        <p>Bài 4: <a href="https://firebase.xcvbkjsbvdlj">https://firebase.xcvbkjsbvdlj</a></p>
      </div>
    </div>
  </div>

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