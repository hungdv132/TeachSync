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
        /* Add your custom CSS styles here */

        /* Apply a background color to the body */
        body {
            background-color: #f0f0f0;
        }

        /* Style the header */
        header {
            background-color: #333;
            color: #fff;
            padding: 10px;
            text-align: center;
        }

        /* Style the page title */
        h1 {
            font-size: 24px;
            margin-top: 20px;
            color: #333;
        }

        /* Style the form */
        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
        }

        /* Style the container */
        .container {
            display: flex;
            width: 100%;
            margin-top: 20px;
        }

        /* Style the left and right columns */
        .left, .right {
            width: 50%;
            box-sizing: border-box;
            padding: 10px;
        }

        /* Style the table */
        table {
            border-collapse: collapse;
            width: 100%;
        }

        table, th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        /* Style the labels and form elements */
        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
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

        /* Style the buttons */
        input[type="submit"], button {
            background-color: #333;
            color: #fff;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 4px;
        }

        input[type="submit"]:hover, button:hover {
            background-color: #555;
        }

        /* Style the links */
        a {
            text-decoration: none;
            color: #007bff;
        }

        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<%@ include file="/WEB-INF/fragments/header.jspf" %>

<h1>Sửa bài test</h1>
<form action="/update-answer" method="post">
<%--    <label for="question-type">Môn học:</label>--%>
    <input type="hidden" name="idTest" id="idTest" value="${test.id}">

<%--    <select name="courseId" id="selCourseId">--%>
<%--        <c:forEach var="courseDTO" items="${courseList}">--%>
<%--            <option value="${courseDTO.id}">--%>
<%--                <c:out value="${courseDTO.courseName}"/>--%>
<%--            </option>--%>
<%--        </c:forEach>--%>
<%--    </select>--%>

    <label for="question-type">Loại kiểm tra:</label>
    <select name="testType" id="selTestType">
        <option value="FIFTEEN_MINUTE">15 phút</option>
        <option value="MIDTERM">Giữa kỳ</option>
        <option value="FINAL">Cuối kỳ</option>
    </select>

    <label>Thời gian:</label>
    <input type="number" id="timeLimit" name="timeLimit" min="1" value="${test.timeLimit}" required>

<%--    <label for="question-type">Loại câu hỏi:</label>--%>
<%--    <select id="question-type" name="questionType" disabled>--%>
<%--        <option value="ESSAY">Tự luận</option>--%>
<%--        <option value="MULTIPLE">Trắc nghiệm</option>--%>
<%--    </select>--%>

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
                            <a href="#"
                                <%--                               onclick="displayQuestion('${qs.key.questionDesc}', '${qs.key.id}',null , '${qs.key.questionType}', ${qs.value})">--%>
                               onclick="displayQuestion(${qs.key.id}, '${qs.key.questionDesc}', '${qs.key.questionType}')">
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
            <div id="questions-container"></div>
            <div id="checkEssay"></div>
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

    // function displayQuestion(questionDesc, idQuestionInput, lstAnswer, type, value) {
    //
    //     questionsArea.innerHTML = questionDesc;
    //     idQuestion.value = idQuestionInput;
    //     var element = document.getElementById("checkEssay");
    // element.innerHTML = ""; // Xóa toàn bộ nội dung trong element trước khi thêm phần tử mới.
    //     var z = 1;
    //     for (let x of value) {
    //         var questionLabel = document.createElement("label");
    //         questionLabel.textContent = "Câu hỏi " + (z) + ":";
    //         z = z + 1;
    //         console.log("đây là " + i);
    //         element.appendChild(questionLabel);
    //     }
    // }

    function displayQuestion(qsId, questionDesc, questionType) {
        idQuestion.value = qsId;
        if (questionType == "ESSAY") {
            questionsArea.value = questionDesc;

        } else {
            $.ajax({
                type: "GET",
                url: "/api/getLstAnswer?questionId=" + qsId,
                success: function (response) {
                    questionsArea.value = questionDesc;
                    var element = document.getElementById("checkEssay");
                    element.innerHTML = ""; // Xóa toàn bộ nội dung trong element trước khi thêm phần tử mới.

                    var i = 1;
                    for (let x of response) {
                        var tempDiv = document.createElement("div")
                        tempDiv.id = "idDivAnswer" + x.id;
                        var answerInput = document.createElement("input");
                        answerInput.type = "text";
                        answerInput.name = "newAnswer" + x.id;
                        answerInput.id = "newAnswer" + x.id;
                        answerInput.value = x.answerDesc;
                        answerInput.disabled = true;
                        var isCorrectCheckbox = document.createElement("input");
                        isCorrectCheckbox.type = "checkbox";
                        isCorrectCheckbox.id = "isCorrect" + x.id;
                        isCorrectCheckbox.name = "isCorrect" + x.id;
                        isCorrectCheckbox.checked = x.isCorrect;
                        isCorrectCheckbox.disabled = true;

                        var deleteButton = document.createElement("button");
                        deleteButton.textContent = "Xóa"; // Đặt văn bản của nút là "Xóa"
                        deleteButton.addEventListener("click", function (event) {
                            event.preventDefault();
                            deleteAnswer(x.id, qsId, questionDesc)
                        });

                        var editButton = document.createElement("button");
                        editButton.textContent = "Sửa";
                        editButton.addEventListener("click", function (event) {
                            event.preventDefault(); // Ngăn chặn hành vi mặc định của nút "Sửa"
                            editAnswer(x.id, "newAnswer" + x.id, "isCorrect" + x.id, qsId, questionDesc)
                        });


                        var lineBreak = document.createElement('br');
                        answerInput.required = true;
                        console.log(x.answerDesc);
                        tempDiv.appendChild(answerInput);
                        tempDiv.appendChild(isCorrectCheckbox);
                        tempDiv.appendChild(deleteButton);
                        tempDiv.appendChild(editButton)
                        tempDiv.appendChild(lineBreak);

                        element.appendChild(tempDiv);


                    }
                    var tempDiv2 = document.createElement("div")
                    var addNewAnswer = document.createElement("button");
                    addNewAnswer.textContent = "Thêm mới";
                    addNewAnswer.id = "tempBt";
                    addNewAnswer.addEventListener("click", function (event) {
                        event.preventDefault();
                        var newInput = document.createElement("input");
                        newInput.type = "text";
                        newInput.id = "newInput";

                        var isCorrectCheckbox = document.createElement("input");
                        isCorrectCheckbox.type = "checkbox";
                        isCorrectCheckbox.name = "isCorrectNew";

                        var saveNewAnswer = document.createElement("button");
                        saveNewAnswer.textContent = "Thêm";
                        saveNewAnswer.addEventListener("click", function (event) {
                            event.preventDefault();
                            addAnswer(qsId, newInput.value, isCorrectCheckbox.checked, questionDesc);
                        });

                        tempDiv2.appendChild(newInput);
                        tempDiv2.appendChild(isCorrectCheckbox);
                        tempDiv2.appendChild(saveNewAnswer);
                        var rm = document.getElementById("tempBt");
                        rm.remove();


                    });
                    tempDiv2.appendChild(addNewAnswer);
                    element.appendChild(tempDiv2);
                }


            })
        }

    }

    function editAnswer(answerId, textName, buttonName, qsId, questionDesc) {
        var txt = document.getElementById(textName);
        var bt = document.getElementById(buttonName);

        var element = document.getElementById("idDivAnswer" + answerId);
        txt.disabled = false;
        bt.disabled = false;
        var editButton = document.createElement("button");
        editButton.textContent = "Lưu";
        editButton.addEventListener("click", function (event) {
            event.preventDefault();
            saveAnswer(answerId, txt.value, bt.checked, qsId, questionDesc)
        });

        element.appendChild(editButton);

    }


    function saveAnswer(id, content, tr, qsId, questionDesc) {
        var dataToSend = {
            id: id,
            content: content,
            isTrue: tr
        };
        $.ajax({
            type: "POST",
            url: "/api/editAnswer",
            data: dataToSend, // Truyền dữ liệu vào request
            success: function (response) {
                displayQuestion(qsId, questionDesc);
            },

        })
    }

    function deleteAnswer(id, qsId, questionDesc) {
        var dataToSend = {
            id: id
        };
        $.ajax({
            type: "POST",
            url: "/api/deleteAnswer",
            data: dataToSend, // Truyền dữ liệu vào request
            success: function (response) {
                displayQuestion(qsId, questionDesc);
            },

        })
    }

    function addAnswer(qsId, answerDesc, isCorrect, questionDesc) {
        var dataToSend = {
            id: qsId,
            answerDesc: answerDesc,
            isCorrect: isCorrect
        };
        $.ajax({
            type: "POST",
            url: "/api/addNewAnswer",
            data: dataToSend, // Truyền dữ liệu vào request
            success: function (response) {
                displayQuestion(qsId, questionDesc);
            },

        })
    }
</script>
</body>
</html>
