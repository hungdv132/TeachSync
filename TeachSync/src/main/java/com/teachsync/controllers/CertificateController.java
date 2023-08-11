package com.teachsync.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CertificateController {
    @GetMapping("/certificate")
    public String certificate() {
        return "certificate/list-certificate";
    }

    @GetMapping("/add-certificate")
    public String addCertificate() {
        return "add-certificate";
    }

    @GetMapping("/certificate-detail")
    public String certificateDetail() {
        return "certificate/certificate-detail";
    }
}
