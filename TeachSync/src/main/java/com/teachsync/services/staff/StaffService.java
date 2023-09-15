package com.teachsync.services.staff;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.center.CenterUpdateDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.dtos.staff.StaffUpdateDTO;
import com.teachsync.entities.Center;
import com.teachsync.entities.Staff;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StaffService {
    /* =================================================== CREATE =================================================== */
    

    /* =================================================== READ ===================================================== */
    /* id */
    Staff getById(Long id) throws Exception;
    StaffReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Staff> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<StaffReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, StaffReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* centerId */
    List<Staff> getAllByCenterId(Long centerId) throws Exception;
    List<StaffReadDTO> getAllDTOByCenterId(Long centerId, Collection<DtoOption> options) throws Exception;

    List<Staff> getAllByCenterIdIn(Collection<Long> centerIdCollection) throws Exception;
    List<StaffReadDTO> getAllDTOByCenterIdIn(Collection<Long> centerIdCollection, Collection<DtoOption> options) throws Exception;

    /* userId */
    List<Staff> getAllByUserId(Long userId) throws Exception;
    List<StaffReadDTO> getAllDTOByUserId(Long userId, Collection<DtoOption> options) throws Exception;

    List<Staff> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception;
    List<StaffReadDTO> getAllDTOByUserIdIn(Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Staff updateStaff(Staff staff) throws Exception;
    StaffReadDTO updateStaffByDTO(StaffUpdateDTO updateDTO) throws Exception;
    
    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    StaffReadDTO wrapDTO(Staff staff, Collection<DtoOption> options) throws Exception;
    List<StaffReadDTO> wrapListDTO(Collection<Staff> staffCollection, Collection<DtoOption> options) throws Exception;
    Page<StaffReadDTO> wrapPageDTO(Page<Staff> staffPage, Collection<DtoOption> options) throws Exception;
}
