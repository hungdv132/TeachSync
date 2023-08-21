<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Chỉnh sửa bài kiểm tra</title>

    <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">

    <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

    <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

    <script src="../../../resources/js/common.js"></script>
    <style>
        /* Áp dụng CSS để tùy chỉnh giao diện form */
        .container {
            display: flex;
            width: 100%;
        }

        .left {
            width: 50%;
            box-sizing: border-box;
            padding: 10px;
        }

        .right {
            width: 50%;
            box-sizing: border-box;
            padding: 10px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        table, th, td {
            border: 1px solid black;
            padding: 5px;
        }

        /* Additional styles for better appearance */
        h1 {
            margin-top: 20px;
        }

        label {
            display: block;
            margin-top: 10px;
        }

        select, input[type="number"], textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            margin-top: 6px;
            margin-bottom: 16px;
            resize: vertical;
        }

        #optionsContainer {
            margin-top: 10px;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/fragments/header.jspf" %>

<h1>Sửa bài test</h1>
<form action="/update-answer" method="get">
    <label for="question-type">Môn học:</label>
    <input type="hidden" name="idTest" id="idTest" value="${test.id}">

    <select name="courseId" id="selCourseId">
        <c:forEach var="courseDTO" items="${courseList}">
            <option value="${courseDTO.id}">
                <c:out value="${courseDTO.courseName}"/>
            </option>
        </c:forEach>
    </select>

    <label for="question-type">Loại kiểm tra:</label>
    <select name="testType" id="selTestType">
        <option value="FIFTEEN_MINUTE">15 phút</option>
        <option value="MIDTERM">Giữa kỳ</option>
        <option value="FINAL">Cuối kỳ</option>
    </select>

    <label>Thời gian:</label>
    <input type="number" id="timeLimit" name="timeLimit" min="1" value="${test.timeLimit}" required>

    <label for="question-type">Loại câu hỏi:</label>
    <select id="question-type" name="questionType" disabled>
        <option value="ESSAY">Tự luận</option>
        <option value="MULTIPLE">Trắc nghiệm</option>
    </select>

    <div class="container">
        <div class="left">
            <table>
                <thead>
                <tr>
                    <th>Câu hỏi</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="qs" items="${questionAnswer}">
                    <tr>
                        <td>
                            <a onclick="displayQuestion('${qs.key.questionDesc}', '${qs.key.id}',null , '${qs.key.questionType}')">
                                <c:out value="${qs.key.questionDesc}"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="right">

            <!-- Content for the right div -->
            <label>Câu hỏi:</label>
            <textarea id="multipleChoiceQuestion" name="questionAll"></textarea>
            <input type="hidden" name="idQuestion" id="idQuestion">
            <br>
            <div id="checkEssay" style="display: none"></div>
            <input type="submit" value="Submit">

        </div>

    </div>
</form>


<script id="script1">
    $("#selCourseId").val('${test.courseId}');
    $("#selTestType").val('${test.testType}');
    $("#question-type").val('${questionList.get(0).questionType}');

    $("#script1").remove();
</script>

<script>
    var questionsArea = document.getElementById("multipleChoiceQuestion");
    var idQuestion = document.getElementById("idQuestion");

    function displayQuestion(questionDesc, idQuestionInput, lstAnswer, type) {
        questionsArea.innerHTML = questionDesc;
        idQuestion.value = idQuestionInput;
        var element = document.getElementById("checkEssay");
        if (type == "multipleChoice") {
            element.style.display = "block";
        } else {
            element.style.display = "none";
        }
    }
</script>
</body>
</html>
