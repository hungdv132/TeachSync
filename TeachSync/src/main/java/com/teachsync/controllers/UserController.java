package com.teachsync.controllers;

import com.teachsync.entities.Test;
import com.teachsync.entities.User;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/lst-user")
    public String lstUser(@RequestParam(value = "page", required = false) Integer page, Model model) {
        if (page == null) {
            page = 0;
        }
        if (page < 0) {
            page = 0;
        }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);
        model.addAttribute("lstUserSearch", users);
        model.addAttribute("pageNo", users.getPageable().getPageNumber());
        model.addAttribute("pageTotal", users.getTotalPages());
        return "user/list-user";
    }
    @GetMapping("/searchuserbyusername")
    public String searchUserByUserName(@RequestParam(value = "page", required = false) Integer page, @RequestParam("searchText") String name, Model model){
        if (page == null) { page = 0; }
        if(page < 0) { page = 0; }
        PageRequest pageable = PageRequest.of(page, 3);
        Page<User> users = userRepository.findAllByUsernameContainingOrderByCreatedAtDesc(name, pageable);
        model.addAttribute("lstUserSearch", users);
        model.addAttribute("pageNo", users.getPageable().getPageNumber());
        model.addAttribute("pageTotal", users.getTotalPages());
        model.addAttribute("searchText", name);
        return "user/lst-user-search";
    }
}
