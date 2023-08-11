<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Trang tạo bài test</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">

  <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

  <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>

  <script src="../../../resources/js/common.js"></script>
  <style>
    /* CSS cho định dạng trang */
    body {
      font-family: Arial, sans-serif;
      margin: 20px;
      background-color: #f1f1f1;
    }

    h1 {
      text-align: center;
      color: #333;
    }

    label {
      display: block;
      font-weight: bold;
      margin-top: 10px;
      font-size: 16px;
    }

    select,
    input[type="number"] {
      margin-top: 5px;
      font-size: 14px;
      padding: 5px;
    }

    button {
      margin-top: 10px;
      font-size: 14px;
      padding: 5px 10px;
      background-color: #4CAF50;
      color: white;
      border: none;
      cursor: pointer;
    }

    button:hover {
      background-color: #45a049;
    }

    .question {
      margin-top: 20px;
      background-color: white;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 5px;
    }

    .answer {
      margin-left: 20px;
      margin-top: 5px;
    }

    .answer label {
      display: inline-block;
      margin-right: 10px;
      font-size: 14px;
    }

    .answer input[type="text"],
    .answer input[type="checkbox"],
    textarea {
      margin-top: 5px;
      font-size: 14px;
      padding: 5px;
      width: 650px;
    }

    #questions-container {
      border-top: 1px solid #ccc;
      margin-top: 20px;
      padding-top: 20px;
    }
  </style>
</head>
<body>
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<h1>Tạo bài test</h1>

<form action="process-question" method="post">
  <label for="question-type">Môn học:</label>
  <select name="courseName">
    <c:forEach var="item" items="${lstCourse}">
      <option value="${item.id}">${item.courseName}</option>
    </c:forEach>
  </select>
  <label for="question-type">Loại kiểm tra:</label>
  <select name="testType">
    <option value="FIFTEEN_MINUTE">15 phút</option>
    <option value="MIDTERM">Giữa kỳ</option>
    <option value="FINAL">Cuối kỳ</option>
  </select>
  <label for="num-questions">Thời gian:</label>
  <input type="number" id="timeLimit" name="timeLimit" min="1" required>
  <label for="question-type">Loại câu hỏi:</label>
  <select id="question-type" name="questionType">
    <option value="essay">Tự luận</option>
    <option value="multipleChoice">Trắc nghiệm</option>
  </select>

  <label for="num-questions">Số lượng câu hỏi:</label>
  <input type="number" id="num-questions" name="numQuestions" min="1" required>

  <button id="gen-question">Tạo câu hỏi</button>

  <div id="questions-container"></div>

  <button type="submit">Tạo bài test</button>
</form>

<%@ include file="/WEB-INF/fragments/footer.jspf" %>

<script>
  // JavaScript để xử lý sự kiện khi thay đổi loại câu hỏi
  var questionTypeSelect = document.getElementById("question-type");
  var questionsContainer = document.getElementById("questions-container");

  var generateQuestionButton = document.getElementById("gen-question");

  generateQuestionButton.addEventListener("click", function () {
    var questionType = document.getElementById("question-type").value;
    var numQuestions = document.getElementById("num-questions").value;

    if (questionType === "essay") {
      generateEssayQuestions(numQuestions);
    } else if (questionType === "multipleChoice") {
      generateMultipleChoiceQuestions(numQuestions);
    }
  });

  function generateEssayQuestions(numQuestions) {
    questionsContainer.innerHTML = "";

    for (var i = 0; i < numQuestions; i++) {
      var questionDiv = document.createElement("div");
      questionDiv.className = "question";

      var questionLabel = document.createElement("label");
      questionLabel.textContent = "Câu hỏi " + (i + 1) + ":";

      var questionInput = document.createElement("textarea");
      questionInput.name = "essayQuestion" + i;
      questionInput.required = true;

      questionDiv.appendChild(questionLabel);
      questionDiv.appendChild(questionInput);

      questionsContainer.appendChild(questionDiv);
    }
  }

  function generateMultipleChoiceQuestions(numQuestions) {
    questionsContainer.innerHTML = "";

    for (var i = 0; i < numQuestions; i++) {
      var questionDiv = document.createElement("div");
      questionDiv.className = "question";

      var questionLabel = document.createElement("label");
      questionLabel.textContent = "Câu hỏi " + (i + 1) + ":";

      var questionInput = document.createElement("textarea");
      questionInput.name = "multipleChoiceQuestion" + i;
      questionInput.required = true;

      var numOptionsLabel = document.createElement("label");
      numOptionsLabel.textContent = "Số lượng đáp án:";

      var numOptionsInput = document.createElement("input");
      numOptionsInput.type = "number";
      numOptionsInput.name = "numOptions" + i;
      numOptionsInput.min = "2";
      numOptionsInput.required = true;

      var answerContainer = document.createElement("div");
      answerContainer.className = "answer-container";

      questionDiv.appendChild(questionLabel);
      questionDiv.appendChild(questionInput);
      questionDiv.appendChild(numOptionsLabel);
      questionDiv.appendChild(numOptionsInput);
      questionDiv.appendChild(answerContainer);

      numOptionsInput.addEventListener("change", function () {
        generateAnswerOptions(this);
      });

      questionsContainer.appendChild(questionDiv);
    }
  }

  function generateAnswerOptions(numOptionsInput) {
    var questionDiv = numOptionsInput.parentNode;
    var answerContainer = questionDiv.querySelector(".answer-container");

    // Xoá các đáp án cũ
    while (answerContainer.firstChild) {
      answerContainer.removeChild(answerContainer.firstChild);
    }

    var numOptions = numOptionsInput.value;

    for (var i = 0; i < numOptions; i++) {
      var answerLabel = document.createElement("label");
      answerLabel.textContent = "Đáp án " + (i + 1) + ":";

      var answerInput = document.createElement("input");
      answerInput.type = "text";
      answerInput.name = "answer" + numOptionsInput.name.slice(-1) + "-" + i;
      answerInput.required = true;
      var checkTrueLabel = document.createElement("label");
      checkTrueLabel.textContent = "Đáp án đúng:";
      var isCorrectCheckbox = document.createElement("input");
      isCorrectCheckbox.type = "checkbox";
      isCorrectCheckbox.name = "isCorrect" + numOptionsInput.name.slice(-1) + "-" + i;

      var answerWrapper = document.createElement("div");
      answerWrapper.className = "answer";
      answerWrapper.appendChild(answerLabel);
      answerWrapper.appendChild(answerInput);
      var checkTrueContainer = document.createElement("div");
      checkTrueContainer.appendChild(checkTrueLabel);
      checkTrueContainer.appendChild(isCorrectCheckbox);

      answerContainer.appendChild(answerWrapper);
      answerWrapper.appendChild(checkTrueContainer);
    }
  }
</script>
</body>
</html>
