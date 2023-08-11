package com.teachsync.services.semester;

import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.entities.Semester;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SemesterService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    Page<Semester> getPageAll(Pageable paging) throws Exception;
    Page<SemesterReadDTO> getPageAllDTO(Pageable paging, Collection<DtoOption> options) throws Exception;

    List<Semester> getAll() throws Exception;
    List<SemesterReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception;

    List<Semester> getAllByStartDateAfter(LocalDate date) throws Exception;
    List<SemesterReadDTO> getAllDTOByStartDateAfter(LocalDate date, Collection<DtoOption> options) throws Exception;
    Map<Long, SemesterReadDTO> mapIdDTOByStartDateAfter(LocalDate date, Collection<DtoOption> options) throws Exception;
    
    
    /* id */
    Semester getById(Long id) throws Exception;
    SemesterReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Semester> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<SemesterReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, SemesterReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    SemesterReadDTO wrapDTO(
            Semester courseSemester, Collection<DtoOption> options) throws Exception;
    List<SemesterReadDTO> wrapListDTO(
            Collection<Semester> courseSemesterCollection, Collection<DtoOption> options) throws Exception;
    Page<SemesterReadDTO> wrapPageDTO(
            Page<Semester> courseSemesterPage, Collection<DtoOption> options) throws Exception;
}
