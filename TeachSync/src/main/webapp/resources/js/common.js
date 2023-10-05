/* =============================== PARAM ============================================================================ */
const requiredErrorMsg = `Xin hãy điền vào trường này.`;


/* =============================== UTIL ============================================================================= */
/* Show / hide */
function showById(id) {
    $(`#`+id).removeClass(`visually-hidden`);
}
function hideById(id) {
    $(`#`+id).addClass(`visually-hidden`);
}

/* Enable / disable */
function enableById(id) {
    $(`#`+id).prop(`disabled`, false);
}
function disableById(id) {
    $(`#`+id).prop(`disabled`, true);
}

/**  Remove the disabled property in a container by id
 * @param id Id of the container for input
 * @param type *Optional. The type of input to be enabled */
function enableAllInputIn(id, type) {
    if (type == null) {
        $(`#`+id+` input`).prop(`disabled`, false);
    } else {
        $(`#`+id+` input[type=`+type+`]`).prop(`disabled`, false);
    }
}
/**  Add the disabled property in a container by id
 * @param id Id of the container for input
 * @param type *Optional. The type of input to be disabled */
function disableAllInputIn(id, type) {
    if (type == null) {
        $(`#`+id+` input`).prop(`disabled`, true);
    } else {
        $(`#`+id+` input[type=`+type+`]`).prop(`disabled`, true);
    }
}

function enableAllSelectIn(id) {
    $(`#`+id+` select`).prop(`disabled`, false);
}
function disableAllSelectIn(id) {
    $(`#`+id+` select`).prop(`disabled`, true);
}

function enableAllTextAreaIn(id) {
    $(`#`+id+` textarea`).prop(`disabled`, false);
}
function disableAllTextAreaIn(id) {
    $(`#`+id+` textarea`).prop(`disabled`, true);
}

function enableAllButtonIn(id) {
    $(`#`+id+` button`).prop(`disabled`, false);
}
function disableAllButtonIn(id) {
    $(`#`+id+` button`).prop(`disabled`, true);
}

function enableAllFormElementIn(id) {
    $(`#`+id+` :input`).prop(`disabled`, false);
}
function disableAllFormElementIn(id) {
    $(`#`+id+` :input`).prop(`disabled`, true);
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

function isStep(number, step) {
    // Calculate the remainder of dividing the number by the step
    let remainder = number % step;

    // Check if the remainder is close to zero or the step, accounting for floating-point errors
    let epsilon = 1e-10; // A small tolerance value

    return Math.abs(remainder) < epsilon || Math.abs(remainder - step) < epsilon;
}

function validateTextInput(textInput, minLength, maxLength, validateOption) {
    textInput.setCustomValidity(``);

    let specialCharacterRegex = /[!@#$%^&*(),.?":{}|<>]/g;
    let errorMsg = ``;

    let textValue = textInput.value;

    if (textValue === ``) {
        /* required */
        if (validateOption.includes(`required`)) {
            errorMsg = requiredErrorMsg;
        }

        /* if not required, then nothing to validate against */
    } else {
        /* null or minLength (if null ignore, if not check minlength) */
        if (validateOption.includes(`nullOrMinLength`)) {
            if (textValue.length < minLength) {
                errorMsg+= `Tối thiểu `+minLength+` ký tự, hoặc để trống trường này.\n`;
            }
        }

        /* minLength */
        if (validateOption.includes(`minLength`)) {
            if (textValue.length < minLength) {
                errorMsg+= `Tối thiểu `+minLength+` ký tự.\n`;
            }
        }

        /* maxLength */
        if (validateOption.includes(`maxLength`)) {
            if (textValue.length > maxLength) {
                errorMsg+= `Tối đa `+maxLength+` ký tự.\n`;
            }
        }

        /* only white space */
        if (validateOption.includes(`onlyBlank`)) {
            if (textValue.trim() === ``) {
                errorMsg+= `Không được phép chỉ chứa khoảng trắng.\n`;
            }
        }

        /* start with white space */
        if (validateOption.includes(`startBlank`)) {
            if (textValue.trim() === ``) {
                if (!validateOption.includes(`onlyBlank`)) {
                    /* if not yet validated against only white space */
                    errorMsg+= `Không được phép chỉ chứa khoảng trắng.\n`;
                }
            } else if (startWithWhiteSpace(textValue)) {
                errorMsg+= `Không được phép bắt đầu với khoảng trắng.\n`;
            }
        }

        /* end with white space */
        if (validateOption.includes(`endBlank`)) {
            if (textValue.trim() === ``) {
                if (!validateOption.includes(`onlyBlank`)) {
                    /* if not yet validated against only white space */
                    errorMsg+= `Không được phép chỉ chứa khoảng trắng.\n`;
                }
            } else if (endWithWhiteSpace(textValue)) {
                errorMsg+= `Không được phép kết thúc với khoảng trắng.\n`;
            }
        }

        /* special character */
        if (validateOption.includes(`specialChar`)) {
            if (specialCharacterRegex.test(textValue)) {
                errorMsg+= `Không được phép chứa ký tự đặc biệt ( !@#$%^&*(),.?":{}|<> ).\n`;
            }
        }
    }

    textInput.setCustomValidity(errorMsg);
    if (errorMsg !== ``) {
        textInput.reportValidity();
        return false;
    }

    return true;
}

/** TODO:  */
function validateTextInputAsNumber(textInputAsNumberInput, min, max, step, validateOption) {
    textInputAsNumberInput.setCustomValidity(``);

    let errorMsg = ``;

    let textValue = textInputAsNumberInput.value;

    if (textValue === ``) {
        /* required */
        if (validateOption.includes(`required`)) {
            errorMsg = requiredErrorMsg;
        }

        /* if not required, then nothing to validate against */
    } else {
        let numberValue = Number(textValue.replace(/./g, ``).replace(/,/, `.`));

        if (isNaN(numberValue)) {
            /* required */
            errorMsg = "";
        } else {
            /* null or min (if null ignore, if not check minlength) */
            if (validateOption.includes(`nullOrMin`)) {
                if (numberValue < min) {
                    errorMsg+= `Giá trị cần lớn hơn `+min+` , hoặc để trống trường này.\n`;
                }
            }

            /* min */
            if (validateOption.includes(`min`)) {
                if (numberValue < min) {
                    errorMsg+= `Giá trị cần lớn hơn `+min+` .\n`;
                }
            }

            /* max */
            if (validateOption.includes(`max`)) {
                if (numberValue > max) {
                    errorMsg+= `Giá trị cần nhỏ hơn `+max+` .\n`;
                }
            }

            /* step */
            if (validateOption.includes(`step`)) {
                let numberInput = document.createElement(`input`);
                numberInput.type = `number`;
                numberInput.step = step;
                numberInput.value = numberValue.toString();
                if (numberInput.validity.stepMismatch) {
                    errorMsg+= `Đơn vị biến đổi nhỏ nhất: ±`+step+` .\n`;
                }
                numberInput = null;
            }
        }
    }

    textInputAsNumberInput.setCustomValidity(errorMsg);
    if (errorMsg !== ``) {
        textInputAsNumberInput.reportValidity();
        return false;
    }

    return true;
}

function validateNumberInput(numberInput, min, max, step, validateOption) {
    numberInput.setCustomValidity(``);

    let errorMsg = ``;

    let numberValue = numberInput.valueAsNumber;

    if (isNaN(numberValue)) {
        /* required */
        if (validateOption.includes(`required`)) {
            errorMsg = requiredErrorMsg;
        }

        /* if not required, then nothing to validate against */
    } else {
        /* null or min (if null ignore, if not check minlength) */
        if (validateOption.includes(`nullOrMin`)) {
            if (numberInput.validity.rangeUnderflow) {
                errorMsg+= `Giá trị cần lớn hơn `+min+` , hoặc để trống trường này.\n`;
            }
        }

        /* min */
        if (validateOption.includes(`min`)) {
            if (numberInput.validity.rangeUnderflow) {
                errorMsg+= `Giá trị cần lớn hơn `+min+` .\n`;
            }
        }

        /* max */
        if (validateOption.includes(`max`)) {
            if (numberInput.validity.rangeOverflow) {
                errorMsg+= `Giá trị cần nhỏ hơn `+max+` .\n`;
            }
        }

        /* step */
        if (validateOption.includes(`step`)) {
            if (numberInput.validity.stepMismatch) {
                errorMsg+= `Đơn vị biến đổi nhỏ nhất: ±`+step+` .\n`;
            }
        }
    }

    numberInput.setCustomValidity(errorMsg);
    if (errorMsg !== ``) {
        numberInput.reportValidity();
        return false;
    }

    return true;
}

/* Copy to clipboard */
function copyToClipboard(id) {
    const fadeTime = 1500;

    navigator.clipboard.writeText($(`#`+id).text());

    $(`body`).prepend(`
        <div class="fixed-top d-flex justify-content-center" id="alert">
           <p class="ts-bg-grey-subtle rounded-pill py-2 px-5" style="width: fit-content;">
               Đã copy vào clipboard
           </p>
        </div>`);

    $(`#alert`).fadeOut(fadeTime);

    setTimeout(function() { $(`#alert`).remove(); }, fadeTime);
}


/* =============================== FIREBASE ========================================================================= */
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
            fileInput.setCustomValidity(`File too big. Max `+fileSizeLimit+` MB.`);
            fileInput.reportValidity();
            return;
        }
    }

    let reader = new FileReader();
    reader.onload = function (e) {
        $(`#`+imgId).prop(`src`, e.target.result);
    }

    // you have to declare the file loading
    reader.readAsDataURL(file);
}

/* TODO: chưa import js cho datatable */
$(document).ready( function () {
    $(`#myTable`).DataTable();
} );

