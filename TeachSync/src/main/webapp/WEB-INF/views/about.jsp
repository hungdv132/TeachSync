<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>About</title>

  <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../resources/css/teachsync_style.css">

  <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  <script src="../../resources/js/common.js"></script>
</head>
<body class="min-vh-100 container-fluid d-flex flex-column ts-bg-white-subtle">
<!-- ================================================== Header ===================================================== -->
<%@ include file="/WEB-INF/fragments/header.jspf" %>
<!-- ================================================== Header ===================================================== -->

<!-- ================================================== Main Body ================================================== -->
<div class="row ts-bg-white border ts-border-teal rounded-3 pt-3 mx-2 mb-3">
  <div class="col">

    <div class="container">
      <div class="row">
        <div class="col">
          <h1>Chào mừng bạn đến với trang web quản lý trung tâm tiếng Anh!</h1>

          <p>Trang web này là nền tảng quản lý toàn diện và hiệu quả cho trung tâm tiếng Anh. Chúng tôi cung
             cấp một loạt các công cụ và tính năng giúp quản lý trung tâm của bạn dễ dàng hơn và tăng cường sự
             chuyên nghiệp của hoạt động giảng dạy.</p>

          <h2>Với trang web quản lý trung tâm tiếng Anh, bạn có thể:</h2>

          <ol>
            <li>Quản lý học viên: Ghi danh học viên, theo dõi tiến trình học tập, quản lý thông tin cá nhân
              và liên lạc với học viên dễ dàng.
            </li>
            <li>Lập lịch học: Tạo lịch học linh hoạt và tiện lợi, theo dõi sự hiện diện của học viên, và
              quản lý các khóa học và lớp học.
            </li>
            <li>Quản lý giáo viên: Theo dõi thông tin giáo viên, phân công giảng dạy, quản lý lịch trình và
              nhiệm vụ của giáo viên.
            </li>
            <li>Quản lý tài liệu: Tải lên và quản lý tài liệu học tập, bài giảng, bài kiểm tra và tài liệu
              tham khảo cho học viên và giáo viên.
            </li>
            <li>Thống kê và báo cáo: Theo dõi kết quả học tập, tiến trình học viên và tình hình hoạt động
              của trung tâm thông qua các báo cáo chi tiết và dễ hiểu.
            </li>
            <li>Liên lạc và thông báo: Gửi thông báo, tin nhắn và email cho học viên, giáo viên và phụ huynh
              để duy trì sự liên lạc và thông tin chính xác.
            </li>
          </ol>

          <p>Trang web quản lý trung tâm tiếng Anh mang đến cho bạn sự tiện lợi, tăng cường hiệu quả quản lý
             và nâng cao trải nghiệm học tập cho học viên. Hãy truy cập ngay và khám phá các tính năng tuyệt
             vời mà chúng tôi cung cấp.</p>

          <p>Cảm ơn bạn đã ghé thăm trang web của chúng tôi. Chúng tôi luôn sẵn lòng hỗ trợ bạn trong việc
             quản lý trung tâm tiếng Anh một cách hiệu quả nhất.</p>
        </div>
        <div class="col">
          <img src="https://hatinhtoplist.vn/wp-content/uploads/2022/10/Trung-tam-tieng-anh-tai-Ha-Tinh-1.jpg"
             width="100%" height="80%">
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