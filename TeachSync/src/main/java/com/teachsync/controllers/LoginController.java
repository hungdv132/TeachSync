package com.teachsync.controllers;

import com.teachsync.dtos.user.UserCreateDTO;
import com.teachsync.dtos.user.UserLoginDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;



    @GetMapping("/sign-in")
    public String login(
            HttpSession session,
            @ModelAttribute("msg") String msg) {
        Object objUser = session.getAttribute("user");

        if (objUser instanceof UserReadDTO) {
            /* Already login */
            return "redirect:/index";
        }

        return "login/login";
    }


    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody /* Trả về String để href thêm lần nữa */
    public Map<String, Object> login(
            @RequestBody UserLoginDTO loginDTO,
            HttpSession session,
            @SessionAttribute(required = false) String targetUrl) {
        Map<String, Object> response = new HashMap<>();

        try {
            UserReadDTO user = userService.loginDTO(loginDTO.getUsername(), loginDTO.getPassword());

            if (user == null) {
                response.put("msg", "Incorrect username or password");
                return response;
            }

            session.setAttribute("user", user);

            if (targetUrl != null) {
                /* Quay lại trang cũ sau login */
                response.put("view", targetUrl);
                return response;
            }
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            e.printStackTrace();
            response.put("msg", e.getMessage());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errorMsg", e.getMessage());
            return response;
        }

        response.put("view", "index");
        return response;
    }

    @GetMapping("/sign-up")
    public String signup(Model model) {
        return "login/signup";
    }

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> signup(@RequestBody UserCreateDTO createDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            UserReadDTO readDTO = userService.signupDTO(createDTO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.put("msg", e.getMessage());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errorMsg", "Server error, please try again later");
            return response;
        }

        response.put("view", "sign-in");
        return response;
    }

    @GetMapping("/sign-out")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
