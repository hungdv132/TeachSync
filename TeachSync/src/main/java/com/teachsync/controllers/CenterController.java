package com.teachsync.controllers;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.center.CenterUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Address;
import com.teachsync.entities.Center;
import com.teachsync.entities.LocationUnit;
import com.teachsync.entities.Room;
import com.teachsync.repositories.AddressRepository;
import com.teachsync.services.address.AddressService;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.locationUnit.LocationUnitService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.CenterType;
import com.teachsync.utils.enums.DtoOption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private RoomService roomService;

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

        return "list-center";
    }

    @GetMapping("/list-room")
    public String room(
            Model model,
            @RequestParam Long id
    ){
        try{
            CenterReadDTO centerReadDTO = centerService.getDTOById(id,null);
            List<RoomReadDTO> roomList = roomService.getAllDTOByCenterId(centerReadDTO.getId(),null);
            model.addAttribute("roomList",roomList);
        }catch (Exception e){

        }

        return "list-room";
    }

    @GetMapping("/room-detail")
    public String roomDetail(
    ){


        return "room-detail";
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

        return "center-detail";
    }

    @GetMapping("edit-center")
    public String editCenterPage(
            Model model,
            @RequestParam Long id,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect){

        //check login
//        if (ObjectUtils.isEmpty(userDTO)) {
//            redirect.addAttribute("mess", "Làm ơn đăng nhập");
//            return "/index";
//        }
//
//        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
//            redirect.addAttribute("mess", "bạn không đủ quyền");
//            return "/index";
//        }

        try{
            CenterReadDTO centerDTO = centerService.getDTOById(id, List.of(DtoOption.ADDRESS));
            model.addAttribute("center", centerDTO);

            AddressReadDTO addressDTO = centerDTO.getAddress();

            Long wardId = addressDTO.getUnitId();
            Map<Integer, Long> levelUnitIdMap =
                    locationUnitService.mapLevelUnitIdByBottomChildId(wardId, null);
            model.addAttribute("levelUnitIdMap", levelUnitIdMap);

            List<LocationUnit> countryList = locationUnitService.getAllByLevel(0);
            List<LocationUnit> provinceList = locationUnitService.getAllByParentId(levelUnitIdMap.get(0));
            List<LocationUnit> districtList = locationUnitService.getAllByParentId(levelUnitIdMap.get(1));;
            List<LocationUnit> wardList = locationUnitService.getAllByParentId(levelUnitIdMap.get(2));

            model.addAttribute("countryList", countryList);
            model.addAttribute("provinceList", provinceList);
            model.addAttribute("districtList", districtList);
            model.addAttribute("wardList", wardList);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "edit-center";
    }

    @PostMapping ("edit-center")
    public String editCenter(
            Model model,
            @RequestParam Long centerId,
            @RequestParam String centerName,
            @RequestParam CenterType centerType,
            @RequestParam Integer centerSize,
            @RequestParam String addrNo,
            @RequestParam String street,
            @RequestParam Long wardId,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect){

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addAttribute("mess", "Làm ơn đăng nhập");
            return "/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addAttribute("mess", "bạn không đủ quyền");
            return "/index";
        }
        try{
            AddressCreateDTO addressCreateDTO = new AddressCreateDTO();
            addressCreateDTO.setAddressNo(addrNo);
            addressCreateDTO.setStreet(street);
            addressCreateDTO.setUnitId(wardId);
            addressCreateDTO.setCreatedBy(userDTO.getId());

            AddressReadDTO addressReadDTO =
                    addressService.createAddressByDTO(addressCreateDTO);

            CenterUpdateDTO centerUpdateDTO = new CenterUpdateDTO();
            centerUpdateDTO.setId(centerId);
            centerUpdateDTO.setAddressId(addressReadDTO.getId());
            centerUpdateDTO.setCenterName(centerName);
            centerUpdateDTO.setCenterType(centerType);
            centerUpdateDTO.setCenterSize(centerSize);
            centerUpdateDTO.setUpdatedBy(userDTO.getId());

            CenterReadDTO centerReadDTO =
                    centerService.updateCenterByDTO(centerUpdateDTO);

            centerReadDTO.setAddress(addressReadDTO);

            model.addAttribute("center", centerReadDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "center-detail";
    }

    /* TODO: move to addressController or LocationUnitCOntroller */
    @GetMapping("api/refresh-location-unit")
    @ResponseBody
    public Map<Integer, List<LocationUnit>> refreshLocationUnit(
            @RequestParam Long unitId,
            @RequestParam Integer level) {

        Map<Integer, List<LocationUnit>> levelUnitListMap = new HashMap<>();

        try {
            List<LocationUnit> unitList = locationUnitService.getAllByParentId(unitId);
            switch (level) {
                case 1 -> {
                    levelUnitListMap.put(1, unitList);

                    unitId = unitList.get(0).getId();
                    unitList = locationUnitService.getAllByParentId(unitId);
                    levelUnitListMap.put(2, unitList);

                    unitId = unitList.get(0).getId();
                    unitList = locationUnitService.getAllByParentId(unitId);
                    levelUnitListMap.put(3, unitList);
                }
                case 2 -> {
                    levelUnitListMap.put(2, unitList);

                    unitId = unitList.get(0).getId();
                    unitList = locationUnitService.getAllByParentId(unitId);
                    levelUnitListMap.put(3, unitList);
                }
                case 3 -> {
                    levelUnitListMap.put(3, unitList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return levelUnitListMap;
    }

}
