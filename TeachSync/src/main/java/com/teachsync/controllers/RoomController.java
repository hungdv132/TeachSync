package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            model.addAttribute("roomList",roomList);
        }catch (Exception e){

        }

        return "room/list-room";
    }

    @GetMapping("/room-detail")
    public String roomDetailPage(
    ){

        return "center/room-detail";
    }

}
