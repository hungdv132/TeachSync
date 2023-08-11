package com.teachsync.controllers;

import com.teachsync.entities.User;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.MiscUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private MiscUtil miscUtil;



    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "login/forgot-password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {

            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = miscUtil.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (UnsupportedEncodingException e) {
            model.addAttribute("error", "Error while sending email");
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", ex.getMessage());
        }

        return "login/forgot-password";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("capstone.teach.sync@gmail.com", "TeachSync Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) throws Exception {
        User user = userService.getByResetPasswordToken(token);


        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "login/message";
        }

        return "login/reset-password";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) throws Exception {
        try {
            String token = request.getParameter("token");
            String password = request.getParameter("password");
            System.out.println(token);

            User user = userService.getByResetPasswordToken(token);
            model.addAttribute("title", "Reset your password");

            if (user == null) {
                model.addAttribute("message", "Invalid Token");
                return "login/message";
            } else {
                userService.updatePassword(user, password);

                model.addAttribute("message", "You have successfully changed your password.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return "login/message";
    }
}
