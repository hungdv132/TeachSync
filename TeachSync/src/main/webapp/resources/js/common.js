function showById(id) {
	$("#" + id).removeClass("visually-hidden");
}

function hideById(id) {
    $("#" + id).addClass("visually-hidden");
}

function enableAllInputIn(id, type) {
    if (type == null) {
        $("#"+id+" input").prop("disabled", false);
    } else {
        $("#"+id+" input[type="+type+"]").prop("disabled", false);
    }
}

function disableAllInputIn(id, type) {
    if (type == null) {
        $("#"+id+" input").prop("disabled", true);
    } else {
        $("#"+id+" input[type="+type+"]").prop("disabled", true);
    }
}

function copyToClipboard(id) {
    const fadeTime = 1500;

    navigator.clipboard.writeText($("#" + id).text());

    $("body").prepend(
        '<div class="fixed-top d-flex justify-content-center" id="alert">' +
        '   <p class="ts-bg-grey-subtle rounded-pill py-2 px-5" style="width: fit-content;">' +
        '       Đã copy vào clipboard' +
        '   </p>' +
        '</div>');

    $("#alert").fadeOut(fadeTime);

    setTimeout(function() { $("#alert").remove(); }, fadeTime);
}

/** For single file input type <b>image/*</b>
 * @param inputId the id of the file input tag
 * @param imgId the id of the img tag
 * @param fileSizeLimit calculate in MegaByte (Firebase standard 1 MB = 1,048,576 Bytes)
 */
function updateImgFromInput(inputId, imgId, fileSizeLimit) {
    /* Không dùng JQuery vì lỗi setCustomValidity() */
    let fileInput = document.getElementById(inputId);
    let file = fileInput.files[0];

    if (fileSizeLimit != null) {
        let bytes = 1048576 * fileSizeLimit;
        if (file.size > bytes) {
            /* File quá cỡ */
            fileInput.setCustomValidity("File too big. Max " + fileSizeLimit + " MB.");
            fileInput.reportValidity();
            return;
        }
    }

    let reader = new FileReader();
    reader.onload = function (e) {
        $("#"+imgId).prop("src", e.target.result);
    }

    // you have to declare the file loading
    reader.readAsDataURL(file);
}

/* TODO: chưa import js cho datatable */
$(document).ready( function () {
    $('#myTable').DataTable();
} );

