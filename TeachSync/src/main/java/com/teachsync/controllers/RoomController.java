package com.teachsync.controllers;

import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.address.AddressUpdateDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.center.CenterUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.room.RoomUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Room;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class RoomController {
    @Autowired
    CenterService centerService;

    @Autowired
    RoomService roomService;

    @GetMapping("/list-room")
    public String roomListPage(
            Model model,
            @RequestParam Long id
    ){
        try{
            CenterReadDTO centerReadDTO = centerService.getDTOById(id,null);
            List<RoomReadDTO> roomList = roomService.getAllDTOByCenterId(centerReadDTO.getId(),null);
            model.addAttribute("center", centerReadDTO);
            model.addAttribute("roomList",roomList);
        }catch (Exception e){

        }

        return "room/list-room";
    }

    @GetMapping("/room-detail")
    public String roomDetailPage(
            Model model,
            @RequestParam Long id
    ){
        try{
            Room room = roomService.getById(id);
            CenterReadDTO centerReadDTO = centerService.getDTOById(room.getCenterId(),null);
            model.addAttribute("center",centerReadDTO);
            model.addAttribute("room",room);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "room/room-detail";
    }
    @GetMapping("edit-room")
    public String editRoomPage(
            Model model,
            @RequestParam Long id,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect
    ){
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try{
            Room room = roomService.getById(id);
            model.addAttribute("room",room);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "room/edit-room";
    }

    @PostMapping("edit-room")
    public String editRoom(
            Model model,
            @ModelAttribute RoomUpdateDTO roomUpdateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect){

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try{
            roomUpdateDTO.setUpdatedBy(userDTO.getId());

            RoomReadDTO roomReadDTO =
                    roomService.updateRoomByDTO(roomUpdateDTO);

            model.addAttribute("room", roomReadDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "room/room-detail";
    }


}
