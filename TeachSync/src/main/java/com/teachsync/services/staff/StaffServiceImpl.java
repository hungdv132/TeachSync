package com.teachsync.services.staff;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.dtos.staff.StaffUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Center;
import com.teachsync.entities.Staff;
import com.teachsync.repositories.StaffRepository;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepository staffRepository;

    @Lazy
    @Autowired
    private CenterService centerService;
    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Boolean existsById(Long id) throws Exception {
        return staffRepository
                .existsByIdAndStatusNot(id, Status.DELETED);
    }
    @Override
    public Staff getById(Long id) throws Exception {
        return staffRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public StaffReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Staff staff = getById(id);

        if (staff == null) {
            return null;
        }

        return wrapDTO(staff, options);
    }

    @Override
    public Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception {
        return staffRepository
                .existsAllByIdInAndStatusNot(idCollection, Status.DELETED);
    }
    @Override
    public List<Staff> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Staff> staffList =
                staffRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (staffList.isEmpty()) {
            return null; }

        return staffList;
    }
    @Override
    public List<StaffReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Staff> staffList = getAllByIdIn(idCollection);

        if (staffList == null) {
            return null; }

        return wrapListDTO(staffList, options);
    }
    @Override
    public Map<Long, StaffReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<StaffReadDTO> staffDTOList = getAllDTOByIdIn(idCollection, options);

        if (staffDTOList == null) {
            return new HashMap<>();
        }

        return staffDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* clazzId */
    @Override
    public List<Staff> getAllByCenterId(Long centerId) throws Exception {
        List<Staff> staffList =
                staffRepository.findAllByCenterIdAndStatusNot(centerId, Status.DELETED);

        if (staffList.isEmpty()) {
            return null; }

        return staffList;
    }
    @Override
    public List<StaffReadDTO> getAllDTOByCenterId(Long centerId, Collection<DtoOption> options) throws Exception {
        List<Staff> staffList = getAllByCenterId(centerId);

        if (staffList == null) {
            return null; }

        return wrapListDTO(staffList, options);
    }

    @Override
    public List<Staff> getAllByCenterIdIn(Collection<Long> centerIdCollection) throws Exception {
        List<Staff> staffList =
                staffRepository.findAllByCenterIdInAndStatusNot(centerIdCollection, Status.DELETED);

        if (staffList.isEmpty()) {
            return null; }

        return staffList;
    }
    @Override
    public List<StaffReadDTO> getAllDTOByCenterIdIn(
            Collection<Long> centerIdCollection, Collection<DtoOption> options) throws Exception {
        List<Staff> staffList = getAllByCenterIdIn(centerIdCollection);

        if (staffList == null) {
            return null; }

        return wrapListDTO(staffList, options);
    }

    /* userId */
    @Override
    public List<Staff> getAllByUserId(Long userId) throws Exception {
        List<Staff> staffList =
                staffRepository.findAllByUserIdAndStatusNot(userId, Status.DELETED);

        if (staffList.isEmpty()) {
            return null; }

        return staffList;
    }
    @Override
    public List<StaffReadDTO> getAllDTOByUserId(Long userId, Collection<DtoOption> options) throws Exception {
        List<Staff> staffList = getAllByUserId(userId);

        if (staffList == null) {
            return null; }

        return wrapListDTO(staffList, options);
    }

    @Override
    public List<Staff> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        List<Staff> staffList =
                staffRepository.findAllByUserIdInAndStatusNot(userIdCollection, Status.DELETED);

        if (staffList.isEmpty()) {
            return null; }

        return staffList;
    }
    @Override
    public List<StaffReadDTO> getAllDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception {
        List<Staff> staffList = getAllByUserIdIn(userIdCollection);

        if (staffList == null) {
            return null; }

        return wrapListDTO(staffList, options);
    }





    /* =================================================== UPDATE =================================================== */
    @Override
    public Staff updateStaff(Staff staff) throws Exception {
        Staff oldStaff = getById(staff.getId());
        if(oldStaff == null){
            throw new IllegalArgumentException("Loi update - khong tim thay staff id");
        }
        staff.setCreatedAt(oldStaff.getCreatedAt());
        staff.setCreatedBy(oldStaff.getCreatedBy());

        /* Validate input */

        /* Check FK */
        /* No FK */

        /* Update to DB */
        staff = staffRepository.saveAndFlush(staff);

        return staff;
    }

    @Override
    public StaffReadDTO updateStaffByDTO(StaffUpdateDTO updateDTO) throws Exception {
        Staff staff = mapper.map(updateDTO,Staff.class);

        staff = updateStaff(staff);

        /* create dependencies */

        /* update dependencies */

        return wrapDTO(staff,null);
    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public StaffReadDTO wrapDTO(Staff staff, Collection<DtoOption> options) throws Exception {
        StaffReadDTO dto = mapper.map(staff, StaffReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CENTER)) {
                CenterReadDTO centerDTO = centerService.getDTOById(staff.getCenterId(), options);
                dto.setCenter(centerDTO);
            }

            if (options.contains(DtoOption.USER)) {
                UserReadDTO userDTO = userService.getDTOById(staff.getUserId(), options);
                dto.setUser(userDTO);
            }
        }

        return dto;
    }

    @Override
    public List<StaffReadDTO> wrapListDTO(
            Collection<Staff> staffCollection, Collection<DtoOption> options) throws Exception {
        List<StaffReadDTO> dtoList = new ArrayList<>();
        StaffReadDTO dto;

        Map<Long, CenterReadDTO> centerIdCenterDTOMap = new HashMap<>();
        Map<Long, UserReadDTO> userIdUserDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> centerIdSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();

            for (Staff staff : staffCollection) {
                centerIdSet.add(staff.getCenterId());
                userIdSet.add(staff.getUserId());
            }

            if (options.contains(DtoOption.CENTER)) {
                /* TODO: */
            }

            if (options.contains(DtoOption.USER)) {
                userIdUserDTOMap = userService.mapIdDTOByIdIn(userIdSet, options);
            }
        }

        for (Staff staff : staffCollection) {
            dto = mapper.map(staff, StaffReadDTO.class);

            dto.setCenter(centerIdCenterDTOMap.get(staff.getCenterId()));

            dto.setUser(userIdUserDTOMap.get(staff.getUserId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<StaffReadDTO> wrapPageDTO(Page<Staff> staffPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(staffPage.getContent(), options),
                staffPage.getPageable(),
                staffPage.getTotalPages());
    }
}
