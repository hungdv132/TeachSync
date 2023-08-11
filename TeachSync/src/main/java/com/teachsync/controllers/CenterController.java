package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.entities.Address;
import com.teachsync.repositories.AddressRepository;
import com.teachsync.services.address.AddressService;
import com.teachsync.services.center.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CenterController {
    @Autowired
    private CenterService centerService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping("/center")
    public String center(Model model) {
        try{
            List<CenterReadDTO> centerList = centerService.getAllDTO(null);
            Set<Long> addressIdSet = centerList.stream().map(CenterReadDTO::getAddressId).collect(Collectors.toSet());
            Map<Long, Address> addressIdAddressMap = addressService.mapIdAddressByIdIn(addressIdSet);
            centerList = centerList.stream().peek(centerReadDTO ->
            {centerReadDTO.setAddress(addressIdAddressMap.get(centerReadDTO.getAddressId()));}).collect(Collectors.toList());

            model.addAttribute("centerList",centerList);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "center/list-center";
    }

    @GetMapping("/room")
    public String room(){
        return "center/list-room";
    }

    @GetMapping("/center-detail")
    public String centerDetail(
            Model model,
            @RequestParam Long id
    ){
        try{
            CenterReadDTO centerReadDTO = centerService.getDTOById(id,null);
            Address address = addressService.getById(centerReadDTO.getAddressId());
            centerReadDTO.setAddress(address);
            model.addAttribute("address",address);
            model.addAttribute("center",centerReadDTO);
        }catch (Exception e){

        }

        return "center/center-detail";
    }

    @PostMapping("edit-center")
    public String editCenter(Model model,
                             @RequestParam Long id){
        try{
            CenterReadDTO centerReadDTO = centerService.getDTOById(id,null);
            Address address = addressService.getById(centerReadDTO.getAddressId());
            centerReadDTO.setAddress(address);
            model.addAttribute("address",address);
            model.addAttribute("center",centerReadDTO);


        }catch (Exception e){

        }
        return "edit-center";
    }

}
