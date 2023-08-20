<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Contact</title>

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
    <!-- Content here -->
    <h1>Liên Hệ</h1>
    <div class="container">
      <div class="row">
        <div class="col mb-3">
          <table>
            <tbody>
            <tr>
              <td class="left bold">Website:</td>
              <td class="right"><a href="https://www.TeachSync.vn">www.TeachSync.vn</a></td>
            </tr>
            <tr>
              <td class="left bold">Cộng đồng hỗ trợ:</td>
              <td class="right"><a href="TeachSync">Cộng đồng TeachSync</a></td>
            </tr>
            <tr>
              <th>Phòng Kinh Doanh</th>
            </tr>
            <tr>
              <td class="left">Điện thoại:</td>
              <td class="right"><a href="cal:0968457623">0968457623</a></td>
            </tr>
            <tr>
              <td class="left">Email:</td>
              <td class="right"><a href="mailto:kinhdoanh@zioncom.net">kinhdoanh@zioncom.net</a> (hỗ trợ
                                                         các vấn
                                                         đề về
                                                         kinh
                                                         doanh).
              </td>
            </tr>
            <tr>
              <th>Phòng Kỹ Thuật</th>
            </tr>
            <tr>
              <td class="left">Điện thoại:</td>
              <td class="right"><a href="cal:0968457623">0968457623</a></td>
            </tr>
            <tr>
              <td class="left">Email:</td>
              <td class="right"><a href="mailto:hotro@zioncom.net">hotro@zioncom.net</a> (hỗ trợ kỹ thuật
                                                     nhanh chóng kịp
                                                     thời).
              </td>
            </tr>

            <tr>
              <th>Văn Phòng Nhà Máy</th>
            </tr>
            <tr>
              <td class="left">Điện thoại:</td>
              <td class="right"><a href="cal:0968457623">0968457623</a></td>
            </tr>
            <tr>
            </tr>

            <tr>
              <td class="left bold">Thời gian làm việc:</td>
              <td class="right"><p>Thứ 2 – 6:&nbsp; 8:00 – 12:00, 13:00 – 17:00</p>
                <p>Thứ 7:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 8:00 – 12:00</p></td>
            </tr>

            <tr>
              <td class="left bold">VP Hà Nội:</td>
              <td class="right">Số nhà 39, Ngõ 262A, Đường Nguyễn Trãi, Quận Thanh Xuân, Hà Nội.</td>
            </tr>

            </tbody>
          </table>
        </div>
        <div class="col">
          <img src="https://phapluat.tuoitrethudo.com.vn/stores/news_dataimages/vuthihuyen/062019/11/08/in_article/bai-1-trung-tam-anh-ngu-phu-song-tu-doi-thuc-den-coi-face.jpg"
             width="100%" height="100%">
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