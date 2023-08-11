package com.teachsync.services.attendant;

import com.teachsync.dtos.attendant.AttendantReadDTO;
import com.teachsync.entities.Attendant;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface AttendantService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    AttendantReadDTO wrapDTO(Attendant attendant, Collection<DtoOption> options) throws Exception;
    List<AttendantReadDTO> wrapListDTO(Collection<Attendant> attendantCollection, Collection<DtoOption> options) throws Exception;
    Page<AttendantReadDTO> wrapPageDTO(Page<Attendant> attendantPage, Collection<DtoOption> options) throws Exception;
}
