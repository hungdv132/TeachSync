package com.teachsync.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MiscUtil {
    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean isAsc) {
        if (isAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

    public Pageable defaultPaging() {
        return makePaging(0, 10, "id", true);
    }

    public List<String> sortSearchableField (Field[] fields) {
        List<String> searchableFieldList = new ArrayList<>();
        String classType;

        for (Field field : fields) {
            classType = field.getType().getSimpleName();
            if (classType.equals("String") || classType.equals("Long")) {
                searchableFieldList.add(field.getName());
            }
        }

        return searchableFieldList;
    }

    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static String generateRandomName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = now.format(formatter);

        String randomName =  timestamp ;
        return randomName;
    }

    /* Validate data */
    /** Check if string start with white space */
    public boolean startWithWhiteSpace(String string) {
        return string.matches("^\\s.*");
    }

    /** Check if string end with white space */
    public boolean endWithWhiteSpace(String string) {
        return string.matches(".*\\s$");
    }

    public String validateString(String string, int minLength, int maxLength,  Collection<String> validateOption) {
        String specialCharacterRegex = "[!@#$%^&*(),.?\":{}|<>]";
        String errorMsg = "";

        /* required */
        if (validateOption.contains("required")) {
            if (string.isEmpty()) {
                return "Xin hãy điền vào trường này.";
            }
        }

        /* null or minLength (if null ignore, if not check minlength) */
        if (validateOption.contains("nullOrMinLength")) {
            if (!string.isEmpty()) {
                if (string.length() < minLength) {
                    errorMsg += "Tối thiểu "+minLength+" ký tự, hoặc để trống trường này.\n";
                }
            }
        }

        /* minLength */
        if (validateOption.contains("minLength")) {
            if (string.length() < minLength) {
                errorMsg += "Tối thiểu "+minLength+" ký tự.\n";
            }
        }

        /* maxLength */
        if (validateOption.contains("maxLength")) {
            if (string.length() > maxLength) {
                errorMsg += "Tối đa "+maxLength+" ký tự.\n";
            }
        }

        /* only white space */
        if (validateOption.contains("onlyBlank")) {
            if (!string.isEmpty()) {
                if (string.isBlank()) {
                    errorMsg += "Không được phép chỉ chứa khoảng trắng.\n";
                }
            }
        }

        /* start with white space */
        if (validateOption.contains("startBlank")) {
            if (!string.isEmpty()) {
                if (string.isBlank()) {
                    if (!validateOption.contains("onlyBlank")) {
                        errorMsg += "Không được phép chỉ chứa khoảng trắng.\n";
                    }
                } else if (startWithWhiteSpace(string)) {
                    errorMsg += "Không được phép bắt đầu với khoảng trắng.\n";
                }
            }
        }

        /* end with white space */
        if (validateOption.contains("endBlank")) {
            if (string != "") {
                if (string.trim() == "") {
                    if (!validateOption.contains("onlyBlank")) {
                        errorMsg += "Không được phép chỉ chứa khoảng trắng.\n";
                    }
                } else if (endWithWhiteSpace(string)) {
                    errorMsg += "Không được phép kết thúc với khoảng trắng.\n";
                }
            }
        }

        /* special character */
        if (validateOption.contains("specialChar")) {
            if (string.matches(specialCharacterRegex)) {
                errorMsg += "Không được phép chứa ký tự đặc biệt ( !@#$%^&*(),.?\":{}|<> ).\n";
            }
        }

        return errorMsg;
    }
}
