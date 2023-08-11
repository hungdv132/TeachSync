package com.teachsync.controllers;

import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.dtos.user.UserUpdateDTO;
import com.teachsync.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model) {

        /* Đã lưu session, */
//        try {
//            //get data
//            UserReadDTO oldDTO = (UserReadDTO) session.getAttribute("user");
//
//            UserReadDTO dto = userService.getDTOById(oldDTO.getId());
//
//            if (dto == null) {
//                model.addAttribute("errorMsg", "Not found by id");
//            }
//
//            session.setAttribute("user", dto);
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("errorMsg", "Server error, try again later");
//        }

        return "profile/profile";
    }

    @PostMapping("/profile")
    public String editProfile(
            HttpSession session,
            Model model,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address
    ) {
        UserReadDTO readDTO = (UserReadDTO) session.getAttribute("user");

        UserUpdateDTO updateDTO = mapper.map(readDTO, UserUpdateDTO.class);

        updateDTO.setFullName(fullName);
        updateDTO.setEmail(email);
        updateDTO.setPhone(phone);
        updateDTO.setAddress(address);
        try {
            readDTO = userService.updateDTOUser(updateDTO);

            session.setAttribute("user", readDTO);
            model.addAttribute("msg", "Edit success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "profile/profile";
    }
}
