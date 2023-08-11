<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <title>Title</title>
  
  <link rel="stylesheet" href="../../resources/css/bootstrap-5.3.0/bootstrap.css">
  <link rel="stylesheet" href="../../resources/css/teachsync_style.css">
  
  <script src="../../resources/js/jquery/jquery-3.6.3.js"></script>
  <script src="../../resources/js/bootstrap-5.3.0/bootstrap.bundle.js"></script>
  
  
  <!-- Import the SDKs you need -->
  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js"></script>
  <script src="https://www.gstatic.com/firebasejs/8.10.0/firebase-storage.js"></script>
  
  <script src="../../resources/js/firebase.js"></script>
  
  
  <script src="../../resources/js/common.js"></script>
</head>
<body>
  <form onsubmit="doUpload(event)">
  
  </form>
  
  
  <script>
    async function doUpload(event) {
        event.preventDefault();
        
        let file = $("#id").prop('files')[0]

        // let fileInput = document.getElementById('id');
        // let file = fileInput.files[0];
        
        let url = await uploadFileToFirebaseAndGetURL(file);

        let dto = {
            'name' : 'get name',
            'fileLink' : url
        }
        
        $.ajax({
            type: "POST",
            data: JSON.stringify(dto),
            url: "/controller-addr",
            contentType: "application/json",
            success: function(response) {
                if (response['view'] != null) {
                    location.href = response['view'];
                }
            }
        })
    }
  </script>
</body>
</html>
