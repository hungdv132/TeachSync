package com.teachsync.controllers;

import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.address.AddressUpdateDTO;
import com.teachsync.dtos.center.CenterCreateDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.user.UserCreateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.dtos.user.UserUpdateAccountDTO;
import com.teachsync.dtos.user.UserUpdateDTO;
import com.teachsync.entities.LocationUnit;
import com.teachsync.entities.User;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.address.AddressService;
import com.teachsync.services.locationUnit.LocationUnitService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.Gender;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private LocationUnitService locationUnitService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("add-user")
    public String createUserPage(
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


        return "user/add-user";
    }

    @PostMapping("add-user")
    public String addCenter(
            @ModelAttribute AddressCreateDTO addressCreateDTO,
            @ModelAttribute UserCreateDTO userCreateDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO,
            RedirectAttributes redirect
    ){

        UserReadDTO userReadDTO = null;

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

            userCreateDTO.setAddressId(addressReadDTO.getId());

            userReadDTO = userService.createUserByDTO(userCreateDTO);

            userReadDTO.setAddress(addressReadDTO);

        }catch (Exception e){
            e.printStackTrace();

        }
        return "redirect:/center";
    }
    @GetMapping("list-user")
    public String userListPage(Model model)  {

        try{
            List<User> listUser = userRepository.findAll();
            model.addAttribute("listUser",listUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "user/list-user";
    }
//    @GetMapping("/lst-user")
//    public String lstUser(@RequestParam(value = "page", required = false) Integer page, Model model) {
//        if (page == null) {
//            page = 0;
//        }
//        if (page < 0) {
//            page = 0;
//        }
//        PageRequest pageable = PageRequest.of(page, 3);
//        Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);
//        model.addAttribute("lstUserSearch", users);
//        model.addAttribute("pageNo", users.getPageable().getPageNumber());
//        model.addAttribute("pageTotal", users.getTotalPages());
//        return "user/list-user";
//    }

//    @GetMapping("/searchuserbyusername")
//    public String searchUserByUserName(@RequestParam(value = "page", required = false) Integer page, @RequestParam("searchText") String name, Model model) {
//        if (page == null) {
//            page = 0;
//        }
//        if (page < 0) {
//            page = 0;
//        }
//        PageRequest pageable = PageRequest.of(page, 3);
//        Page<User> users = userRepository.findAllByUsernameContainingOrderByCreatedAtDesc(name, pageable);
//        model.addAttribute("lstUserSearch", users);
//        model.addAttribute("pageNo", users.getPageable().getPageNumber());
//        model.addAttribute("pageTotal", users.getTotalPages());
//        model.addAttribute("searchText", name);
//        return "user/lst-user-search";
//    }
//
//    @GetMapping("/lst-user-by-type")
//    public String lstStudentUser(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "searchText", required = false) String name,
//                                 @RequestParam("type") Long typeUser, Model model) {
//        if (page == null) {
//            page = 0;
//        }
//        if (page < 0) {
//            page = 0;
//        }
//        PageRequest pageable = PageRequest.of(page, 3);
//        Page<User> users = userRepository.findAllByRoleId(typeUser,pageable);
//        model.addAttribute("lstUserSearch", users);
//        model.addAttribute("pageNo", users.getPageable().getPageNumber());
//        model.addAttribute("pageTotal", users.getTotalPages());
//        model.addAttribute("searchText", name);
//        return "user/lst-user-search";
//    }

    @GetMapping("user-detail")
    public String userPage(
            Long id,
            Model model,
            RedirectAttributes redirect,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }


        try {
            UserReadDTO userReadDTO = userService.getDTOById(id,null);
            List<LocationUnit> countryList = locationUnitService.getAllByLevel(0);
            model.addAttribute("countryList", countryList);

            Long addressId = userReadDTO.getAddressId();
            if (addressId != null) {
                AddressReadDTO addressDTO = addressService.getDTOById(addressId, null);
                model.addAttribute("address", addressDTO);

                Long wardId = addressDTO.getUnitId();
                Map<Integer, Long> levelUnitIdMap =
                        locationUnitService.mapLevelUnitIdByBottomChildId(wardId, null);
                model.addAttribute("levelUnitIdMap", levelUnitIdMap);

                List<LocationUnit> provinceList = locationUnitService.getAllByParentId(levelUnitIdMap.get(0));
                model.addAttribute("provinceList", provinceList);

                List<LocationUnit> districtList = locationUnitService.getAllByParentId(levelUnitIdMap.get(1));;
                model.addAttribute("districtList", districtList);

                List<LocationUnit> wardList = locationUnitService.getAllByParentId(levelUnitIdMap.get(2));
                model.addAttribute("wardList", wardList);
            } else {
                Long parentId = countryList.get(0).getId();
                List<LocationUnit> provinceList = locationUnitService.getAllByParentId(parentId);
                model.addAttribute("provinceList", provinceList);

                parentId = provinceList.get(0).getId();
                List<LocationUnit> districtList = locationUnitService.getAllByParentId(parentId);;
                model.addAttribute("districtList", districtList);

                parentId = districtList.get(0).getId();
                List<LocationUnit> wardList = locationUnitService.getAllByParentId(parentId);
                model.addAttribute("wardList", wardList);
            }
            model.addAttribute("user1",userReadDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "user/user-detail";
    }

    @PostMapping("/user-detail/{option}")
    public String editProfile(
            @PathVariable("option") String option,
            RedirectAttributes redirect,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "userAvatar", required = false) String userAvatar,
            @RequestParam(value = "about", required = false) String about,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "gender", required = false) Gender gender,
            @RequestParam(value = "addressNo", required = false) String addressNo,
            @RequestParam(value = "street", required = false) String street,
            @RequestParam(value = "unitId", required = false) Long unitId,
            HttpSession session,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        /* Check login */
        if (ObjectUtils.isEmpty(userDTO)) {
            redirect.addFlashAttribute("mess", "Làm ơn đăng nhập");
            return "redirect:/index";
        }

        try {
            UserReadDTO userReadDTO = userService.getDTOById(userId,null);
            UserUpdateDTO userUpdateDTO = mapper.map(userReadDTO, UserUpdateDTO.class);
            userUpdateDTO.setUpdatedBy(userDTO.getId());

            switch (option) {
                case "avatar" -> {
                    userUpdateDTO.setUserAvatar(userAvatar);
                    userUpdateDTO.setAbout(about);
                }

                case "detail" -> {
                    userUpdateDTO.setFullName(fullName);
                    userUpdateDTO.setEmail(email);
                    userUpdateDTO.setPhone(phone);
                    userUpdateDTO.setGender(gender);
                }

                case "address" -> {
                    AddressReadDTO addressReadDTO;

                    if (userReadDTO.getAddressId() == null) {
                        AddressCreateDTO addressCreateDTO = new AddressCreateDTO();
                        addressCreateDTO.setAddressNo(addressNo);
                        addressCreateDTO.setStreet(street);
                        addressCreateDTO.setUnitId(unitId);
                        addressCreateDTO.setCreatedBy(userDTO.getId());

                        addressReadDTO = addressService.createAddressByDTO(addressCreateDTO);

                        userUpdateDTO.setAddressId(addressReadDTO.getId());
                    } else {
                        addressReadDTO = addressService.getDTOById(userReadDTO.getAddressId(), null);

                        AddressUpdateDTO addressUpdateDTO = mapper.map(addressReadDTO, AddressUpdateDTO.class);

                        addressService.updateAddressByDTO(addressUpdateDTO);
                    }
                }
            }

            userService.updateDTOUser(userUpdateDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/user-detail?id="+userId;
    }

    @PutMapping("/api/user-detail/{option}")
    @ResponseBody
    public Map<String, String> editAccount(
            @PathVariable("option") String option,
            HttpSession session,
            @RequestBody UserUpdateAccountDTO accountDTO,
            @SessionAttribute(value = "user", required = false) UserReadDTO userDTO) {

        Map<String, String> response = new HashMap<>();

        try {
            UserUpdateDTO userUpdateDTO = mapper.map(userDTO, UserUpdateDTO.class);
            userUpdateDTO.setUpdatedBy(userDTO.getId());

            switch (option) {
                case "username" -> {
                    if (userService.getAllByUsernameAndIdNot(accountDTO.getUsername(), userDTO.getId()) != null) {
                        throw new IllegalArgumentException("Tên tài khoản này đã được sử dụng. Xin chọn tên khác.");
                    }

                    userService.loginDTO(userDTO.getUsername(), accountDTO.getPassword());

                    userUpdateDTO.setUsername(accountDTO.getUsername());
                }

                case "password" -> {
                    if (accountDTO.getPassword().equals(accountDTO.getOldPassword())) {
                        throw new IllegalArgumentException("Mật khẩu mới giống mật khẩu cũ. Hủy thay đổi.");
                    }
                    userService.loginDTO(userDTO.getUsername(), accountDTO.getOldPassword());

                    userUpdateDTO.setPassword(accountDTO.getPassword());
                }
            }

            userDTO = userService.updateDTOUser(userUpdateDTO);
            session.setAttribute("user", userDTO);
            response.put("msg", "Cập nhập thành công.");
        } catch (BadCredentialsException bCE) {
            bCE.printStackTrace();
            response.put("error", "Sai mật khẩu.");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return response;
        }

        response.put("view", "/user-detail");
        return response;
    }

}
