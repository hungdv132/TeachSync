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

        <p>abc</p>


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
            <!-- Tab News -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue active"
                        data-bs-toggle="tab" role="tab" aria-selected="true"
                        id="news-tab" data-bs-target="#news-tab-pane" aria-controls="news-tab-pane">
                    News
                </button>
            </li>

            <!-- Tab Homework -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                        data-bs-toggle="tab" role="tab" aria-selected="false"
                        id="homework-tab" data-bs-target="#homework-tab-pane" aria-controls="homework-tab-pane">
                    Homework
                </button>
            </li>

            <!-- Tab Score -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                        data-bs-toggle="tab" role="tab" aria-selected="false"
                        id="score-tab" data-bs-target="#score-tab-pane" aria-controls="score-tab-pane">
                    Score
                </button>
            </li>

            <!-- Tab Absence -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                        data-bs-toggle="tab" role="tab" aria-selected="false"
                        id="absence-tab" data-bs-target="#absence-tab-pane" aria-controls="absence-tab-pane">
                    Absence
                </button>
            </li>

            <!-- Tab Material -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                        data-bs-toggle="tab" role="tab" aria-selected="false"
                        id="material-tab" data-bs-target="#material-tab-pane" aria-controls="material-tab-pane">
                    Material
                </button>
            </li>

            <!-- Tab test -->
            <li class="nav-item" role="presentation">
                <button type="button" class="nav-link ts-txt-grey ts-txt-hover-blue"
                        data-bs-toggle="tab" role="tab" aria-selected="false"
                        id="test-tab" data-bs-target="#test-tab-pane" aria-controls="test-tab-pane">
                    Bài kiểm tra
                </button>
            </li>

        </ul>

        <!-- Clazz dependency tab content -->
        <div class="tab-content border border-top-0 rounded-bottom-3 pt-3 px-3" id="semesterTabContent">
            <!-- Tab News TabPane -->
            <div class="tab-pane fade active show" role="tabpanel"
                 id="news-tab-pane" aria-labelledby="news-tab">
                <c:forEach items="${newsList}" var="news">
                    <p>${news.newsTitle} : ${news.newsLink} </p>
                    <p>${news.newsDesc} </p>
                </c:forEach>
            </div>

            <!-- Tab Homework TabPane -->
            <div class="tab-pane fade" role="tabpanel"
                 id="homework-tab-pane" aria-labelledby="homework-tab">
                <c:forEach items="${homeworkList}" var="homework">
                    <p>${homework.homeworkName} </p>
                    <p>Deadline : ${homework.deadline}</p>
                    <br>
                </c:forEach>
            </div>

            <!-- Tab Score TabPane -->
            <div class="tab-pane fade" role="tabpanel"
                 id="score-tab-pane" aria-labelledby="score-tab">
                <c:forEach items="${homeworkList}" var="homework">
                    <c:forEach items="${homework.memberHomeworkRecordList}" var="record">
                        <p>${record.name} </p>
                        <p>Điểm ${record.score}   </p>
                        <br>
                    </c:forEach>
                </c:forEach>
            </div>

            <!-- Tab Absence TabPane -->
            <div class="tab-pane fade" role="tabpanel"
                 id="absence-tab-pane" aria-labelledby="absence-tab">
                <p>đây là Absence</p>
            </div>

            <!-- Tab Material TabPane -->
            <div class="tab-pane fade" role="tabpanel"
                 id="material-tab-pane" aria-labelledby="material-tab">
                <c:forEach items="${material}" var="material">
                    <p>${material.materialName} : ${material.materialLink}  </p>
                    <br>
                </c:forEach>
            </div>

            <div class="tab-pane fade" role="tabpanel"
                 id="test-tab-pane" aria-labelledby="test-tab">

                <c:if test="${sessionScope.user.roleId eq 1}">
                    <c:forEach items="${clazz.testList}" var="tests">
                        <c:if test="${tests.test.testType == 'FIFTEEN_MINUTE'}">
                            <a href="/take-test?clazzId=${clazz.id}&clazzTestId=${tests.id}">Bài kiểm tra 15 phút</a>
                        </c:if>
                        <c:if test="${tests.test.testType == 'MIDTERM'}">
                            <a href="/take-test?clazzId=${clazz.id}&clazzTestId=${tests.id}">Bài kiểm tra giữa kỳ</a>
                        </c:if>
                        <c:if test="${tests.test.testType == 'FINAL'}">
                            <a href="/take-test?clazzId=${clazz.id}&clazzTestId=${tests.id}">Bài kiểm tra cuối kỳ</a>
                        </c:if>
                        <c:if test="${tests.test.questionType == 'ESSAY'}">
                            <p>Loại kiểm tra: Tự luận</p>
                        </c:if>
                        <c:if test="${tests.test.questionType == 'MULTIPLE'}">
                            <p>Loại kiểm tra: Trắc nghiệm</p>
                        </c:if>
                        <p>Trạng thái: ${tests.inTime}</p>
                        <br>
                    </c:forEach>
                </c:if>
                <c:if test="${sessionScope.user.roleId eq 3}">
                    <c:forEach items="${lstTestTeacher}" var="tests">
                        <c:if test="${tests.testType == 'FIFTEEN_MINUTE'}">
                            <a href="/test-score?idClazz=${clazz.id}&idTest=${tests.id}">Bài kiểm tra 15 phút</a>
                        </c:if>
                        <c:if test="${tests.testType == 'MIDTERM'}">
                            <a href="/test-score?idClazz=${clazz.id}&idTest=${tests.id}">Bài kiểm tra giữa kỳ</a>
                        </c:if>
                        <c:if test="${tests.testType == 'FINAL'}">
                            <a href="/test-score?idClazz=${clazz.id}&idTest=${tests.id}">Bài kiểm tra cuối kỳ</a>
                        </c:if>
                        <c:if test="${tests.questionType == 'ESSAY'}">
                            <p>Loại kiểm tra: Tự luận</p>
                        </c:if>
                        <c:if test="${tests.questionType == 'MULTIPLE'}">
                            <p>Loại kiểm tra: Trắc nghiệm</p>
                        </c:if>
                        <br>
                    </c:forEach>
                </c:if>


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