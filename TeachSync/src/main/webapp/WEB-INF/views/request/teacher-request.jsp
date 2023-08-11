<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Tuyển dụng</title>

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

            // Gán sự kiện 'change' cho input type="file"
            document.getElementById('detailNote').addEventListener('change', handleFileUpload);

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
                    document.getElementById('detailNoteFile').value = url;
                } catch (error) {
                    console.error('Error uploading file:', error);
                    // Xử lý lỗi nếu cần
                }
            }

        });
    </script>
    <script>
        $(document).ready(function () {
            $('#ckHoSo').click(function () {
                $('#divHoSo').toggle();
            });
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
    <div class="container">
        <h1 style="text-align:center">Ứng tuyển ${campaign.center.centerName} - ${campaign.center.centerName}</h1>
        <div class="col">
            <!-- Content here -->
            <div class="container">
                <div class="row">
                    <div class="col mb-3">
                        <form action="application-request" method="post">
                            <input type="text" name="campaignId" value="${campaign.id}" hidden>
                            <p>Hãy gửi cho chúng tôi hồ sơ của bạn</p>
                            <div class="form-group">
                                <p>Loại Hồ Sơ</p>
                                <select name="detailType" id="courseIdSel" class="btn btn-secondary dropdown-toggle">
                                    <option value="CV"> CV</option>
                                    <option value="CITIZEN_ID"> Căn cước công dân</option>
                                    <option value="LICENSE"> Chứng chỉ</option>
                                </select>
                            </div>
                            <br>
                            <div class="form-group">
                                <label>Tải lên hồ sơ của bạn : </label>
                                <input type="file" name="detailNote" class="form-control"  id="detailNote"
                                       value="">
                                <input type="text" name="detailNoteFile" hidden id="detailNoteFile">

                            </div>
                            <br>
                            <p>Bạn đã có hồ sơ <input type="checkbox" id="ckHoSo"></p>
                            <div class="form-group" id="divHoSo" style="display: none;">
                                <p>Nhập link hồ sơ</p>
                                <input type="text" class="form-control" placeholder="nhập hồ sơ" name="detailLink">
                            </div>
                            <br><br>
                            <button type="submit" class="btn btn-success">Ứng Tuyển</button>
                            <br><br>
                        </form>

                    </div>
                    <div class="col">
                        <img src="https://data-gcdn.basecdn.net/202007/sys1954/hiring/18/20/WLGKALH4NL/89182dbf4577a34120fda4453ed2a168/QNC57FLLCRELF/ae/28/b9/19/b1/23dbef7b613e2731de7a653ac6cf4477/ispeaking_gvta_size2_3.jpg"
                             width="100%" height="100%">
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->
</body>

</html>