<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bài kiểm tra trắc nghiệm</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">

    <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

    <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

    <script src="../../resources/js/common.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            padding: 20px;
        }
        h1 {
            text-align: center;
            margin-bottom: 30px;
        }
        .question {
            margin-bottom: 30px;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .question h3 {
            font-weight: bold;
            margin-bottom: 10px;
        }
        .question label {
            display: block;
            margin-bottom: 10px;
        }
        .question label input[type="radio"] {
            margin-right: 10px;
        }
        .submit-button {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body onload="startTimer(${test.timeLimit})">
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<h1>Bài kiểm tra</h1>

<p>Thời gian còn lại: <span id="timer"></span></p>
<c:choose>
    <c:when test="${test.testDesc eq 'multipleChoice'}">
        <form action="submitTest" method="post">
            <input type="hidden" name="idTest" value="${idTest}" >
            <input type="hidden" name="typeTest" value="${test.testDesc}" >
            <input type="hidden" name="classTest" value="${classTest}" >

            <c:forEach var="entry" items="${hmQA}">
                <div class="question">
                    <h3>${entry.key.questionDesc}</h3>
                    <c:forEach var="answer" items="${entry.value}">
                        <label>
                            <input type="radio" name="question${entry.key.id}" value="${answer.id}">
                                ${answer.answerDesc}
                        </label>
                    </c:forEach>
                </div>
            </c:forEach>

            <input type="submit" value="Nộp bài">
        </form>
    </c:when>
    <c:otherwise>
        <form id="myForm" action="submitTest" method="post">
            <input type="hidden" name="idTest" value="${idTest}" >
            <input type="hidden" name="typeTest" value="${test.testDesc}" >
            <input type="hidden" name="classTest" value="${classTest}" >

            <c:forEach var="entry" items="${hmQA}">
                <div class="question">
                    <h4>${entry.key.questionDesc}</h4>
                    <textarea id="question${entry.key.id}" name="question${entry.key.id}" rows="10" cols="80"required></textarea>
                </div>
            </c:forEach>

            <input type="submit" value="Nộp bài">
        </form>
    </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/fragments/footer.jspf" %>

<%-- Bạn cần thêm các thư viện JSTL và phần logic xử lý bài kiểm tra vào đây --%>

</body>
<script>
    function startTimer(minutes) {
        var countDownDate = new Date().getTime() + (minutes * 60000);

        var x = setInterval(function() {
            var now = new Date().getTime();
            var distance = countDownDate - now;

            var minutesLeft = Math.floor(distance / (1000 * 60));
            var secondsLeft = Math.floor((distance % (1000 * 60)) / 1000);

            document.getElementById("timer").innerHTML = minutesLeft + " phút " + secondsLeft + " giây";

            if (distance < 0) {
                clearInterval(x);
                document.getElementById("timer").innerHTML = "Hết giờ!";
                document.getElementById("myForm").submit(); // Submit form khi hết giờ
            }
        }, 1000);
    }
</script>
</html>