<%@ page import="com.teachsync.utils.enums.QuestionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Làm bài kiểm tra</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- External CSS -->
    <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

    <!-- External JS -->
    <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
    <script src="../../resources/js/common.js"></script>
</head>
<body onload="startTimer(${test.timeLimit})">

<%@ include file="/WEB-INF/fragments/header.jspf" %>

<div class="container mt-4">
    <h2>Bài làm</h2>
    <form method="post" action="mark">
        <input type="hidden" name="memberTestRecordId" value="${memberTestRecordId}" >
        <div class="row">
            <c:forEach var="marktest" items="${markTestDTOS}">
                <div class="col-md-12 mb-4 border rounded p-3">
                    <h4>Câu hỏi:</h4>
                    <p>${marktest.questionContent}</p>

                    <h4>Đáp án:</h4>
                    <textarea class="form-control" readonly>${marktest.answerContent}</textarea>

                    <h4>Điểm:</h4>
                    <input type="text" class="form-control" name="scores[${marktest.testRecordId}]">
                </div>
            </c:forEach>
        </div>
        <button type="submit" class="btn btn-primary">Lưu</button>
    </form>
</div>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>

</body>
</html>
