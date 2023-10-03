/* =============================== UTIL ============================================================================= */
/* Show / hide */
function showById(id) {
    $("#" + id).removeClass("visually-hidden");
}
function hideById(id) {
    $("#" + id).addClass("visually-hidden");
}

/* Enable / disable */
function enableById(id) {
    $("#" + id).prop("disabled", false);
}
function disableById(id) {
    $("#" + id).prop("disabled", true);
}

/**  Remove the disabled property in a container by id
 * @param id Id of the container for input
 * @param type *Optional. The type of input to be enabled */
function enableAllInputIn(id, type) {
    if (type == null) {
        $("#"+id+" input").prop("disabled", false);
    } else {
        $("#"+id+" input[type="+type+"]").prop("disabled", false);
    }
}
/**  Add the disabled property in a container by id
 * @param id Id of the container for input
 * @param type *Optional. The type of input to be disabled */
function disableAllInputIn(id, type) {
    if (type == null) {
        $("#"+id+" input").prop("disabled", true);
    } else {
        $("#"+id+" input[type="+type+"]").prop("disabled", true);
    }
}

function enableAllSelectIn(id) {
    $("#"+id+" select").prop("disabled", false);
}
function disableAllSelectIn(id) {
    $("#"+id+" select").prop("disabled", true);
}

function enableAllTextAreaIn(id) {
    $("#"+id+" textarea").prop("disabled", false);
}
function disableAllTextAreaIn(id) {
    $("#"+id+" textarea").prop("disabled", true);
}

function enableAllButtonIn(id) {
    $("#"+id+" button").prop("disabled", false);
}
function disableAllButtonIn(id) {
    $("#"+id+" button").prop("disabled", true);
}

function enableAllFormElementIn(id) {
    $("#" + id).find("input, textarea, select, button").prop("disabled", false);
}
function disableAllFormElementIn(id) {
    $("#" + id).find("input, textarea, select, button").prop("disabled", true);
}

/* Validate data */
/** Check if string start with white space */
function startWithWhiteSpace(string) {
    return /^\s/.test(string);
}

/** Check if string end with white space */
function endWithWhiteSpace(string) {
    return /\s$/.test(string);
}

function validateTextInput(textInput, minLength, maxLength, validateOption) {
    let specialCharacterRegex = /[!@#$%^&*(),.?":{}|<>]/g;
    let errorMsg = ``;

    let value = textInput.value;

    /* required */
    if (validateOption.includes("required")) {
        if (value === ``) {
            textInput.setCustomValidity(`Xin hãy điền vào trường này.`);
            textInput.reportValidity();
            return false;
        }
    }

    /* null or minLength (if null ignore, if not check minlength) */
    if (validateOption.includes("nullOrMinLength")) {
        if (value !== ``) {
            if (value.length < minLength) {
                errorMsg += `Tối thiểu `+minLength+` ký tự, hoặc để trống trường này.\n`;
            }
        }
    }

    /* minLength */
    if (validateOption.includes("minLength")) {
        if (value.length < minLength) {
            errorMsg += `Tối thiểu `+minLength+` ký tự.\n`;
        }
    }

    /* maxLength */
    if (validateOption.includes("maxLength")) {
        if (value.length > maxLength) {
            errorMsg += `Tối đa `+maxLength+` ký tự.\n`;
        }
    }

    /* only white space */
    if (validateOption.includes("onlyBlank")) {
        if (value !== ``) {
            if (value.trim() === ``) {
                errorMsg += `Không được phép chỉ chứa khoảng trắng.\n`;
            }
        }
    }

    /* start with white space */
    if (validateOption.includes("startBlank")) {
        if (value !== ``) {
            if (value.trim() === ``) {
                if (!validateOption.includes("onlyBlank")) {
                    errorMsg += `Không được phép chỉ chứa khoảng trắng.\n`;
                }
            } else if (startWithWhiteSpace(value)) {
                errorMsg += `Không được phép bắt đầu với khoảng trắng.\n`;
            }
        }
    }

    /* end with white space */
    if (validateOption.includes("endBlank")) {
        if (value !== ``) {
            if (value.trim() === ``) {
                if (!validateOption.includes("onlyBlank")) {
                    errorMsg += `Không được phép chỉ chứa khoảng trắng.\n`;
                }
            } else if (endWithWhiteSpace(value)) {
                errorMsg += `Không được phép kết thúc với khoảng trắng.\n`;
            }
        }
    }

    /* special character */
    if (validateOption.includes("specialChar")) {
        if (specialCharacterRegex.test(value)) {
            errorMsg += `Không được phép chứa ký tự đặc biệt ( !@#$%^&*(),.?":{}|<> ).\n`;
        }
    }

    textInput.setCustomValidity(errorMsg);
    if (errorMsg !== ``) {
        textInput.reportValidity();
        return false;
    }

    return true;
}

/* Copy to clipboard */
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
/* =============================== UTIL ============================================================================= */

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

