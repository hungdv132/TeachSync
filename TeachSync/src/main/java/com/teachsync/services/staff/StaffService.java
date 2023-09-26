package com.teachsync.services.staff;

import com.teachsync.dtos.staff.StaffReadDTO;
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
    Boolean existsById(Long id) throws Exception;
    Staff getById(Long id) throws Exception;
    StaffReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception;
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

    
    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    StaffReadDTO wrapDTO(Staff staff, Collection<DtoOption> options) throws Exception;
    List<StaffReadDTO> wrapListDTO(Collection<Staff> staffCollection, Collection<DtoOption> options) throws Exception;
    Page<StaffReadDTO> wrapPageDTO(Page<Staff> staffPage, Collection<DtoOption> options) throws Exception;
}
