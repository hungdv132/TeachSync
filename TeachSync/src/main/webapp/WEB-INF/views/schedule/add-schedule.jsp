<%@ page import="com.teachsync.utils.enums.ScheduleType" %>
<%@ page import="com.teachsync.utils.enums.Slot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
    <%--@elvariable id="createDTO" type="com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO"--%>
    <form:form modelAttribute="createDTO" action="/add-schedule" method="post" class="row">
      <input type="hidden" name="clazzId" value="${clazz.id}">
      <input type="hidden" id="txtStaffId" name="staffId" value="${clazz.staffId}">
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
        <select class="form-select" id="selScheduleCaId" name="scheduleCategoryId" onchange="checkSchedule()">
          <c:forEach items="${scheduleCateList}" var="scheduleCate">
            <option value="${scheduleCate.id}">${scheduleCate.categoryName}</option>
          </c:forEach>
        </select>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="dateStart">Thời gian bắt đầu</label>
        <input type="date" class="form-control" min="" max=""
               id="dateStart" name="startDate" value="" onchange="checkSchedule()" required>
      </div>
    
      <div class="col-sm-6 col-md-4 mb-3">
        <label class="form-label" for="dateEnd">Thời gian kết thúc</label>
        <input type="date" class="form-control" min="" max=""
               id="dateEnd" name="endDate" value="" onchange="checkSchedule()" required>
      </div>
  
      <div class="col-12 mb-3">
        <p id="txtConflict" class="visually-hidden">
          Đã có lớp sử dụng phòng và tiết và lịch học này trong cùng khoảng thời gian học
        </p>
      </div>
      
      <h5>Tiết học</h5>
      <p>(Số tiết học của khóa: <span id="txtNumSession"></span>/${clazz.course.numSession} ; N/A: Thời gian ngoài hạn)</p>
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
    </form:form>
  </div>
  <!-- Content -->
</div>
<!-- ================================================== Main Body ================================================== -->


<!-- ================================================== Footer ===================================================== -->
<%@ include file="/WEB-INF/fragments/footer.jspf" %>
<!-- ================================================== Footer ===================================================== -->


<!-- ================================================== Script ===================================================== -->
<script>
    var mess = `${mess}`
    if (mess != '') {
        alert(mess);
    }
    
    
    const numSessionTotal = ${clazz.course.numSession};
    let numSession = 0;

    function checkSessionNum() {
        let chkArray = $('#tbodySession input[type="checkbox"]:checked');
        let unChkArray = $('#tbodySession input[type="checkbox"]:not(:checked)')
        
        numSession = chkArray.length;

        if (numSession < numSessionTotal) {
            unChkArray.prop("disabled", false);
            hideById("btnSubmit");
        }
        
        if (numSession === numSessionTotal) {
            unChkArray.prop("disabled", true);
            showById("btnSubmit");
        }
        
        if (numSession > numSessionTotal) {
            unChkArray.prop("disabled", true);
            chkArray[0].prop("checked", false);
            checkSessionNum();
        }

        chkArray.each(function(i, obj) {
            let date = $(obj).val();
            enableAllFormElementIn(date);
            
            $("#hidSesStart"+date).attr("name", "sessionCreateDTOList["+i+"].sessionStart");
            $("#hidSesEnd"+date).attr("name", "sessionCreateDTOList["+i+"].sessionEnd");
            $("#selSesSlot"+date).attr("name", "sessionCreateDTOList["+i+"].slot");
            $("#selSesStaffId"+date).attr("name", "sessionCreateDTOList["+i+"].staffId");
            $("#selSesRoomId"+date).attr("name", "sessionCreateDTOList["+i+"].roomId");
        });

        unChkArray.each(function(i, obj) {
            let date = $(obj).val();
            disableAllFormElementIn(date);

            $("#hidSesStart"+date).removeAttr("name");
            $("#hidSesEnd"+date).removeAttr("name");
            $("#selSesSlot"+date).removeAttr("name");
            $("#selSesStaffId"+date).removeAttr("name");
            $("#selSesRoomId"+date).removeAttr("name");
        });
        
        $("#txtNumSession").text(numSession);
    }
    
    function updateSession() {
        let inNumSession = 0;
        
        let scheduleCa = $("#selScheduleCaId option:selected").text();
        let scheduleCaArray = scheduleCa.split(",");
        
        let tbodySession = $("#tbodySession");
        tbodySession.empty();
        let staffId = $("#txtStaffId").val();
        let roomId = $("#selRoomId").val();
        let startDate = $("#dateStart").val();
        let endDate = $("#dateEnd").val();
        let slot = $("#selSlot").val();

        let startDateObj = new Date(startDate);
        let endDateObj = new Date(endDate);
        
        let dateObj = new Date(startDate);
        
        let weekdayArray = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'];
        /* Date.getDay() return 0 -> 6. 0 being sunday */
        let weekday = weekdayArray[dateObj.getDay()];

        if (weekday !== 'T2') {
            /* Set counter to Monday */
            if (weekday === 'CN') {
                dateObj.setDate(startDateObj.getDate() - 6);
            } else {
                dateObj.setDate(startDateObj.getDate() - (startDateObj.getDay() - 1));
            }
        }
        
        while (dateObj <= endDateObj) {
            let tr = $("<tr>");
            
            for (let i = 0; i < 7; i++) {
                if (dateObj < startDateObj) {
                    tr.append("<td>N/A</td>");
                } else {
                    if (dateObj <= endDateObj) {
                        let isoDateString = dateObj.toISOString().split('T')[0];
                        let splitDate = isoDateString.split('-');
                        let viVNDateString = splitDate[2]+'/'+splitDate[1]+'/'+splitDate[0];

                        let isChk = false;
                        if (scheduleCaArray.includes(weekday)) {
                            if (inNumSession < numSessionTotal) {
                                isChk = true;
                                inNumSession++;
                            } else {
                                isChk = false;
                            }
                        } else {
                            isChk = false;
                        }
                        
                        tr.append(`
                            <td>
                              <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       `+(isChk ? `checked` : ``)+`
                                       id="chk`+isoDateString+`" value="`+isoDateString+`"
                                       onclick="checkSessionNum()">
                                <label class="form-check-label" for="chk`+isoDateString+`">`+viVNDateString+`</label>
                              </div>
                              <div id="`+isoDateString+`">
                                <input type="hidden" id="hidSesStart`+isoDateString+`"
                                       `+(isChk ? `
                                       name="sessionCreateDTOList[`+inNumSession+`].sessionStart"` : `
                                       class="visually-hidden" disabled`)+`
                                       value="`+isoDateString+`T`+slotStartTime[slot]+`">
                                <input type="hidden" id="hidSesEnd`+isoDateString+`"
                                       `+(isChk ? `
                                       name="sessionCreateDTOList[`+inNumSession+`].sessionEnd"` : `
                                       class="visually-hidden" disabled`)+`
                                       value="`+isoDateString+`T`+slotEndTime[slot]+`">
                                <input type="hidden" id="selSesSlot`+isoDateString+`"
                                       `+(isChk ? `
                                       name="sessionCreateDTOList[`+inNumSession+`].slot"` : `
                                       class="visually-hidden" disabled`)+`
                                       value="`+slot+`">
                                <input type="hidden" id="selSesStaffId`+isoDateString+`"
                                       `+(isChk ? `
                                       name="sessionCreateDTOList[`+inNumSession+`].staffId"` : `
                                       class="visually-hidden" disabled`)+`
                                       value="`+staffId+`">
                                <input type="hidden" id="selSesRoomId`+isoDateString+`"
                                       `+(isChk ? `
                                       name="sessionCreateDTOList[`+inNumSession+`].roomId"` : `
                                       class="visually-hidden" disabled`)+`
                                       value="`+roomId+`">
                              </div>
                            </td>
                        `);

                    } else {
                        tr.append("<td>N/A</td>");
                    }
                }
                
                dateObj.setDate(dateObj.getDate()+1);
                weekday = weekdayArray[dateObj.getDay()];
            }

            tbodySession.append(tr);
        }

        checkSessionNum();
    }
    
    function checkSchedule() {
        let roomId = $("#selRoomId").val();
        let scheduleCaId = $("#selScheduleCaId").val();
        let slot = $("#selSlot").val();
        let startDate = $("#dateStart").val();
        let endDate = $("#dateEnd").val();

        $.ajax({
            type: "GET",
            url: "/api/check-conflict-schedule?roomId="+roomId+"&scheduleCaId="+scheduleCaId+
                "&slot="+slot+"&startDate="+startDate+"&endDate="+endDate,
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