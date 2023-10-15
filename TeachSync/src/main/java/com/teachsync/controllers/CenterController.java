package com.teachsync.controllers;

import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.address.AddressUpdateDTO;
import com.teachsync.dtos.center.CenterCreateDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.center.CenterUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.LocationUnit;
import com.teachsync.entities.User;
import com.teachsync.services.address.AddressService;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.locationUnit.LocationUnitService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.teachsync.utils.enums.DtoOption.*;

@Controller
public class CenterController {
    @Autowired
    private CenterService centerService;

    @Autowired
    private LocationUnitService locationUnitService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RoomService roomService;

    /* =================================================== CREATE =================================================== */

    @GetMapping("add-center")
    public String createCenterPage(
            Model model,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect
    ){
        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try{
            List<LocationUnit> countryList = locationUnitService.getAllByLevel(0);
            model.addAttribute("countryList", countryList);
            Long parentId = countryList.get(0).getId();
            List<LocationUnit> provinceList = locationUnitService.getAllByParentId(parentId);
            model.addAttribute("provinceList", provinceList);

            parentId = provinceList.get(0).getId();
            List<LocationUnit> districtList = locationUnitService.getAllByParentId(parentId);
            model.addAttribute("districtList", districtList);

            parentId = districtList.get(0).getId();
            List<LocationUnit> wardList = locationUnitService.getAllByParentId(parentId);
            model.addAttribute("wardList", wardList);
        }catch (Exception e){
            e.printStackTrace();
        }


        return "center/add-center";
    }

    @PostMapping("add-center")
    public String addCenter(
            @ModelAttribute AddressCreateDTO addressCreateDTO,
            @ModelAttribute CenterCreateDTO centerCreateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect
    ){

        CenterReadDTO centerReadDTO = null;

        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try{

            addressCreateDTO.setCreatedBy(userDTO.getId());

            AddressReadDTO addressReadDTO =
                    addressService.createAddressByDTO(addressCreateDTO);

            centerCreateDTO.setAddressId(addressReadDTO.getId());

            centerReadDTO = centerService.createCenterByDTO(centerCreateDTO);

            centerReadDTO.setAddress(addressReadDTO);

        }catch (Exception e){
            e.printStackTrace();

        }
        return "redirect:/center";
    }

    /* =================================================== READ ===================================================== */
    @GetMapping("/center")
    public String centerListPage(Model model) {
        try{
            List<CenterReadDTO> centerList = centerService.getAllDTO(List.of(ADDRESS));

            model.addAttribute("centerList",centerList);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "center/list-center";
    }

    @GetMapping("/center-detail")
    public String centerDetail(
            Model model,
            @RequestParam Long id){
        try{
            CenterReadDTO centerReadDTO = centerService.getDTOById(id, List.of(ADDRESS));

            model.addAttribute("center",centerReadDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "center/center-detail";
    }




    /* =================================================== UPDATE =================================================== */
    @GetMapping("edit-center")
    public String editCenterPage(
            Model model,
            @RequestParam Long id,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect){

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "Bạn không đủ quyền");
            return "redirect:/index";
        }

        try{
            CenterReadDTO centerDTO = centerService.getDTOById(id, List.of(ADDRESS));
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

        } catch (Exception e){
            e.printStackTrace();
        }

        return "center/edit-center";
    }

    @PostMapping ("edit-center")
    public String editCenter(
            Model model,
            @ModelAttribute AddressUpdateDTO addressUpdateDTO,
            @ModelAttribute CenterUpdateDTO centerUpdateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect){

        //check login
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            redirect.addFlashAttribute("mess", "bạn không đủ quyền");
            return "redirect:/index";
        }

        try{
            addressUpdateDTO.setUpdatedBy(userDTO.getId());

            AddressReadDTO addressReadDTO =
                    addressService.updateAddressByDTO(addressUpdateDTO);

            centerUpdateDTO.setAddressId(addressReadDTO.getId());
            centerUpdateDTO.setUpdatedBy(userDTO.getId());

            CenterReadDTO centerReadDTO =
                    centerService.updateCenterByDTO(centerUpdateDTO);

            centerReadDTO.setAddress(addressReadDTO);

            model.addAttribute("center", centerReadDTO);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "center/center-detail";
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== API ====================================================== */
    /* TODO: move to addressController or LocationUnitController */
    @GetMapping(value = "api/refresh-location-unit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<Integer, List<LocationUnit>> refreshLocationUnit(
            @RequestParam Long unitId,
            @RequestParam Integer level) {

        Map<Integer, List<LocationUnit>> levelUnitListMap = new HashMap<>();

        try {
            List<LocationUnit> unitList = locationUnitService.getAllByParentId(unitId);
            switch (level) {
                case 1:
                    levelUnitListMap.put(1, unitList);

                    unitId = unitList.get(0).getId();
                    unitList = locationUnitService.getAllByParentId(unitId);
                case 2: /* No break, let fall through due to repeat code */
                    levelUnitListMap.put(2, unitList);

                    unitId = unitList.get(0).getId();
                    unitList = locationUnitService.getAllByParentId(unitId);
                case 3: /* No break, let fall through due to repeat code */
                    levelUnitListMap.put(3, unitList);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return levelUnitListMap;
    }

    @GetMapping("/api/center-detail")
    @ResponseBody
    public Map<String, Object> getCenterDetail(
            @RequestParam Long centerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            CenterReadDTO centerDTO = centerService.getDTOById(centerId, List.of(ADDRESS));
            response.put("center", centerDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
