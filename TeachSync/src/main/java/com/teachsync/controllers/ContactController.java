package com.teachsync.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {
    @GetMapping("")
    public String contact() {
        /* TODO: add news, promotion,... to model */
        return "contact";
    }
}