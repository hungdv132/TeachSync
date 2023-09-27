<%@ page import="com.teachsync.utils.enums.ScheduleType" %>
<%@ page import="com.teachsync.utils.enums.Slot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi" dir="ltr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Thêm lịch học</title>
  
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
<div class="row">
  <!-- Breadcrumb -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal px-5 mb-3">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb ts-txt-sm ts-txt-bold my-2">
        <li class="breadcrumb-item">
          <a href="/index">
            <i class="bi-house-door"></i>&nbsp;Trang chủ
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          <a href="/schedule-clazz">
            QL Lịch Học
          </a>
        </li>
        <li class="breadcrumb-item active" aria-current="page">
          Thêm Lịch Học
        </li>
      </ol>
    </nav>
  </div>
  <!-- Breadcrumb -->
  
  
  <!-- Content -->
  <div class="col-12 ts-bg-white border-top border-bottom ts-border-teal pt-3 px-5 mb-3">
    <form action="/add-schedule" method="post" class="row">
      <input type="hidden" name="clazzId" value="${clazz.id}">
      <input type="hidden" name="scheduleType" value="${ScheduleType.SCHEDULE}">
  
      <h5>Lịch học</h5>
      
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label">Tên lớp</label>
        <input type="text" class="form-control" name="clazzName" value="${clazz.clazzName}" disabled readonly>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="selRoomId">Phòng học</label>
        <select class="form-select" id="selRoomId" name="roomId" onchange="checkSchedule()">
          <c:forEach items="${roomList}" var="room">
            <option value="${room.id}">${room.roomName}</option>
          </c:forEach>
        </select>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="selSlot">Tiết</label>
        <select class="form-select" id="selSlot" name="slot" onchange="checkSchedule()">
          <option value="${Slot._1.slot}">${Slot._1.slot} (${Slot._1.start} - ${Slot._1.end})</option>
          <option value="${Slot._2.slot}">${Slot._2.slot} (${Slot._2.start} - ${Slot._2.end})</option>
          <option value="${Slot._3.slot}">${Slot._3.slot} (${Slot._3.start} - ${Slot._3.end})</option>
          <option value="${Slot._4.slot}">${Slot._4.slot} (${Slot._4.start} - ${Slot._4.end})</option>
          <option value="${Slot._5.slot}">${Slot._5.slot} (${Slot._5.start} - ${Slot._5.end})</option>
          <option value="${Slot._6.slot}">${Slot._6.slot} (${Slot._6.start} - ${Slot._6.end})</option>
          <option value="${Slot._7.slot}">${Slot._7.slot} (${Slot._7.start} - ${Slot._7.end})</option>
          <option value="${Slot._8.slot}">${Slot._8.slot} (${Slot._8.start} - ${Slot._8.end})</option>
        </select>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="selScheduleCaId">Lịch học</label>
        <select class="form-select" id="selScheduleCaId" name="schedulecaId" onchange="checkSchedule()">
          <c:forEach items="${scheduleCateList}" var="scheduleCate">
            <option value="${scheduleCate.id}">${scheduleCate.scheduleDesc}</option>
          </c:forEach>
        </select>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="dateStart">Thời gian bắt đầu</label>
        <input type="date" class="form-control" min="${semester.startDate}" max="${semester.endDate}"
               id="dateStart" name="startDate" value="${semester.startDate}" onchange="checkSchedule()" required>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="dateEnd">Thời gian kết thúc</label>
        <input type="date" class="form-control" min="${semester.startDate}" max="${semester.endDate}"
               id="dateEnd" name="endDate" value="${semester.endDate}" onchange="checkSchedule()" required>
      </div>
  
      <div class="col-12 mb-3">
        <p id="txtConflict" class="visually-hidden">
          Đã có lớp sử dụng phòng và tiết và lịch học này trong cùng khoảng thời gian học
        </p>
      </div>
      
      <h5>Tiết học</h5>
      <p>(Số tiết học của khóa: ${clazz.courseSemester.course.numSession} ; N/A: Thời gian ngoài hạn)</p>
      <div class="col-12 mb-3">
        <table class="table table-striped table-bordered" style="table-layout: fixed;">
          <thead class="table-primary">
          <tr>
            <th scope="col">T2</th>
            <th scope="col">T3</th>
            <th scope="col">T4</th>
            <th scope="col">T5</th>
            <th scope="col">T6</th>
            <th scope="col">T7</th>
            <th scope="col">CN</th>
          </tr>
          </thead>
    
          <tbody class="align-middle" id="tbodySession">
          </tbody>
        </table>
      </div>
      
      <div class="col-12 text-center mb-3">
        <button type="submit" id="btnSubmit" class="btn btn-primary w-50 visually-hidden">Xác nhận</button>
      </div>
    </form>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->

<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->

<!-- ================================================== Script ===================================================== -->
<script>
    var mess = '${mess}'
    if (mess != '') {
        alert(mess);
    }

    function updateSession() {
        let tbodySession = $("#tbodySession");
        tbodySession.empty();
        let startDate = $("#dateStart").val();
        let endDate = $("#dateEnd").val();

        let dateObj = new Date(startDate);
        let endDateObj = new Date(endDate);
        
        let weekdayArray = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        /* Date.getDay() return 0 -> 6. 0 being sunday */
        let weekday = weekdayArray[dateObj.getDay()];
        
        if (weekday !== 'Mon') {
            let tmpDateObj = new Date(dateObj.getTime());
            if (weekday === 'Sun') {
                tmpDateObj.setDate(tmpDateObj.getDate() - 6);
            } else {
                tmpDateObj.setDate(tmpDateObj.getDate() - (tmpDateObj.getDay() - 1));
            }

            let tr = $("<tr>");
            
            while (tmpDateObj < dateObj) {
                tr.append("<td>N/A</td>");
                tmpDateObj.setDate(tmpDateObj.getDate() + 1);
            }

            while (weekday !== 'Mon') {
                let isoDateString = dateObj.toISOString().split('T')[0];
                let splitDate = isoDateString.split('-');
                let viVNDateString = splitDate[2] + '/' + splitDate[1] + '/' + splitDate[0];
                
                tr.append(
                    "<td id='" + isoDateString + "'>" +
                    "  <div class='form-check'>" +
                    "    <input class='form-check-input' type='checkbox' " +
                    "           id='chk"+ isoDateString +"' value='"+ isoDateString +"'" +
                    "           onclick=''>" +
                    "    <label class='form-check-label' for='chk"+ isoDateString +"'>" +
                    "      2023-02-06" +
                    "    </label>" +
                    "  </div>                       " +
                    "                               " +
                    "                               " +
                    "                               " +
                    "                               " +
                    "                               " +
                    "                               " +
                    "                               " +
                    "</td>"
                );
                
                
                dateObj.setDate(dateObj.getDate() + 1);
                weekday = weekdayArray[dateObj.getDay()];
            }
            
            tbodySession.append(tr);
        }
        
        while (dateObj < endDateObj) {
            let tr = $("<tr>");
            
            for (let i = 0; i < 7; i++) {
                if (dateObj <= endDateObj) {
                    tr.append("<td id='" + dateObj.toISOString().split('T')[0] + "'>-</td>");
                } else {
                    tr.append("<td>N/A</td>");
                }
                dateObj.setDate(dateObj.getDate() + 1);
            }

            tbodySession.append(tr);
        }
        
        
    }
    
    function checkSchedule() {
        let roomId = $("#selRoomId").val();
        let scheduleCaId = $("#selScheduleCaId").val();
        let slot = $("#selSlot").val();
        let startDate = $("#dateStart").val();
        let endDate = $("#dateEnd").val();

        $.ajax({
            type: "GET",
            url: "/api/check-conflict-schedule?roomId=" + roomId + "&scheduleCaId=" + scheduleCaId +
                "&slot=" + slot + "&startDate=" + startDate + "&endDate=" + endDate,
            success: function(response) {
                if (response) {
                    /* if have conflict */
                    showById("txtConflict");
                    hideById("btnSubmit");
                    return;
                }
                /* if no conflict */
                hideById("txtConflict");
                showById("btnSubmit");
                
                updateSession();
            }
        })
    }
    
    checkSchedule();
</script>
<!-- ================================================== Script ===================================================== -->
</body>
</html>