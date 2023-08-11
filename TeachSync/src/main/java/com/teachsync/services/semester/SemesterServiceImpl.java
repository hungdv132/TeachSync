package com.teachsync.services.semester;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.entities.Semester;
import com.teachsync.repositories.SemesterRepository;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;
    
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;
    
    
    /* =================================================== CREATE =================================================== */
    
    
    /* =================================================== READ ===================================================== */
    @Override
    public Page<Semester> getPageAll(Pageable paging) throws Exception {
        Page<Semester> semesterPage =
                semesterRepository.findAllByStatusNot(Status.DELETED, paging);

        if (semesterPage.isEmpty()) {
            return null; }

        return semesterPage;
    }
    @Override
    public Page<SemesterReadDTO> getPageAllDTO(Pageable paging, Collection<DtoOption> options) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging(); }

        Page<Semester> semesterPage = getPageAll(paging);

        if (semesterPage == null) {
            return null; }

        return wrapPageDTO(semesterPage, options);
    }

    @Override
    public List<Semester> getAll() throws Exception {
        List<Semester> semesterList =
                semesterRepository.findAllByStatusNotOrderByStartDateDesc(Status.DELETED);

        if (semesterList.isEmpty()) {
            return null; }

        return semesterList;
    }
    @Override
    public List<SemesterReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception {
        List<Semester> semesterList = getAll();

        if (semesterList == null) {
            return null; }

        return wrapListDTO(semesterList, options);
    }

    @Override
    public List<Semester> getAllByStartDateAfter(LocalDate date) throws Exception {
        List<Semester> semesterList =
                semesterRepository.findAllByStartDateAfterAndStatusNot(date, Status.DELETED);

        if (semesterList.isEmpty()) {
            return null; }

        return semesterList;
    }
    @Override
    public List<SemesterReadDTO> getAllDTOByStartDateAfter(LocalDate date, Collection<DtoOption> options) throws Exception {
        List<Semester> semesterList = getAllByStartDateAfter(date);

        if (semesterList == null) {
            return null; }

        return wrapListDTO(semesterList, options);
    }

    /* id */
    @Override
    public Semester getById(Long id) throws Exception {
        return semesterRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public SemesterReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Semester semester = getById(id);
        
        if (semester == null) {
            return null; }
        
        return wrapDTO(semester, options);
    }
    
    @Override
    public List<Semester> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Semester> semesterList =
                semesterRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);
        
        if (semesterList.isEmpty()) {
            return null; }
        
        return semesterList;
    }
    @Override
    public List<SemesterReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Semester> semesterList = getAllByIdIn(idCollection);
    
        if (semesterList == null) {
            return null; }
    
        return wrapListDTO(semesterList, options);
    }
    @Override
    public Map<Long, SemesterReadDTO> mapSemesterIdSemesterDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<SemesterReadDTO> semesterDTOList = getAllDTOByIdIn(idCollection, options);
        
        if (semesterDTOList == null) {
            return new HashMap<>(); }
    
        return semesterDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }
    
    
    /* =================================================== UPDATE =================================================== */
    
    
    /* =================================================== DELETE =================================================== */
    
    
    /* =================================================== WRAPPER ================================================== */
    @Override
    public SemesterReadDTO wrapDTO(Semester semester, Collection<DtoOption> options) throws Exception {
        SemesterReadDTO dto = mapper.map(semester, SemesterReadDTO.class);
    
        /* Add Dependency */
//        if (options != null && !options.isEmpty()) {
//            if (options.contains(DtoOption.FK)) {
//                FkDTO fkDTO = fkService.getDTOById(dto.getFkId());
//                dto.setFk(fkDTO);
//            }
//        }
    
        return dto;
    }
    @Override
    public List<SemesterReadDTO> wrapListDTO(Collection<Semester> semesterCollection, Collection<DtoOption> options) throws Exception {
        List<SemesterReadDTO> dtoList = new ArrayList<>();
    
        SemesterReadDTO dto;
    
//        Map<Long, FkDTO> fkIdFkDTOMap = new HashMap<>();
//
//        if (options != null && !options.isEmpty()) {
//            Set<Long> fkIdSet = new HashSet<>();
//
//            for (Semester semester : semesterCollection) {
//                fkIdSet.add(semester.getFkId());
//            }
//
//            if (options.contains(DtoOption.FK)) {
//                fkIdFkDTOMap = fkService.mapFkIdFkDTOByIdIn(fkIdSet);
//            }
//        }
    
        for (Semester semester : semesterCollection) {
            dto = mapper.map(semester, SemesterReadDTO.class);
        
            /* Add Dependency */
//            dto.setFk(fkIdFkDTOMap.get(semester.getFkId()));
        
            dtoList.add(dto);
        }
    
        return dtoList;
    }
    @Override
    public Page<SemesterReadDTO> wrapPageDTO(Page<Semester> semesterPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(semesterPage.getContent(), options),
                semesterPage.getPageable(),
                semesterPage.getTotalPages());
    }
}
