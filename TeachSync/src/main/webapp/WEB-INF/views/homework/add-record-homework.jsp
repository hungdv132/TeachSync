<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Danh sách lớp học</title>

    <link rel="stylesheet" href="../../../resources/css/bootstrap-5.3.0/bootstrap.css">
    <link rel="stylesheet" href="../../../resources/css/teachsync_style.css">

    <script src="../../../resources/js/jquery/jquery-3.6.3.js"></script>
    <script src="../../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
    <script src="../../../resources/js/common.js"></script>

    <!-- Import the SDKs you need -->
    <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js"></script>
    <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-storage.js"></script>

    <script src="../../../resources/js/firebase.js"></script>

    <script src="../../../resources/js/common.js"></script>

    <script>
        $(document).ready(function () {
            $('#ckHoSo').click(function () {
                $('#divHoSo').toggle();
            });

            // Gán sự kiện 'change' cho input type="file"
            document.getElementById('submission').addEventListener('change', handleFileUpload);

            // Hàm xử lý khi người dùng chọn file
            async function handleFileUpload(event) {
                const file = event.target.files[0]; // Lấy file từ sự kiện

                if (!file) {
                    return;
                }

                try {
                    // Gọi function uploadFileToFirebaseAndGetURL để upload file và nhận URL
                    const url = await uploadFileToFirebaseAndGetURL(file);

                    // Gán URL vào input type="text"
                    document.getElementById('submissionFile').value = url;
                } catch (error) {
                    console.error('Error uploading file:', error);
                    // Xử lý lỗi nếu cần
                }
            }

        });
    </script>
    <script>
        var mess = '${mess}'
        if (mess != '') {
            alert(mess);
        }
    </script>
</head>
<body class="container-fluid ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
    <form action="/homework/record-homework" method="post">
        <input type="hidden" name="homeworkId" value="${homework.id}">
        <input type="hidden" name="clazzId" value="${homework.clazzId}">
        <div class="form-group">
            <label>Số điểm chấm : ${homeworkRecord.score}</label>
        </div>
        <div class="form-group">
            <label>Tên Bài tập</label>
            <input type="text" name="name"
                   value="${homework.homeworkName}"
                   disabled
                   class="form-control" placeholder="Nhập tên bài tập">
        </div>
        <div class="form-group">
            <label>Tên bài tập đã nộp</label>
            <input type="text" name="name"
                   value="${homeworkRecord.name}"
                   disabled
                   class="form-control" placeholder="Nhập tên bài tập">
        </div>
        <div class="form-group">
            <label>File đính kèm</label>
            <c:if test="${option eq 'detail' }">
                <br>
                <a href="${homeworkRecord.submission}" download>Xem file của bạn</a>
            </c:if>
            <c:if test="${option eq 'add' }">
                <input type="file" name="submission"
                       value=""
                       id="submission"
                       class="form-control" placeholder="File đính kèm">
                <input type="text" name="submissionFile" hidden id="submissionFile">

            </c:if>

        </div>

        <p>Bạn đã link của file bài tập <input type="checkbox" id="ckHoSo"></p>
        <div class="form-group" id="divHoSo" style="display: none;">
            <label>Link đính kèm</label>
            <c:if test="${option eq 'detail' }">
                <input type="text" name="submissionLink"
                       disabled
                       value="${homeworkRecord.submissionLink}"
                       class="form-control" placeholder="File đính kèm">
            </c:if>
            <c:if test="${option eq 'add' }">
                <input type="text" name="submissionLink"
                       value=""
                       class="form-control" placeholder="File đính kèm">
            </c:if>
        </div>
        <br>
        <c:if test="${option eq 'detail' }">
            <button hidden type="submit" class="btn btn-primary">Submit</button>
        </c:if>
        <c:if test="${option eq 'add' }">
            <button type="submit" class="btn btn-primary">Submit</button>
        </c:if>

    </form>

    <c:if test="${isTeacher}">
        <form action="/homework/update-score-record-homework" method="post">
            <input type="hidden" name="homeworkId" value="${homework.id}">
            <input type="hidden" name="recordHomeworkId" value="${homeworkRecord.id}">
            <div class="form-group">
                <label>Số điểm chấm</label>
                <input type="number" name="score"
                       value="${homeworkRecord.score}"
                       class="form-control" placeholder="Nhập điểm chấm">
            </div>
            <br>
            <button type="submit" class="btn btn-primary">Chấm</button>
        </form>
        <br>
    </c:if>
    <br/>
    <br/>


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