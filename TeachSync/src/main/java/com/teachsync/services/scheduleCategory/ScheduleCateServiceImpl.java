package com.teachsync.services.scheduleCategory;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCaReadDTO;
import com.teachsync.entities.ScheduleCategory;
import com.teachsync.repositories.ScheduleCateRepository;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScheduleCateServiceImpl implements ScheduleCateService{

    @Autowired
    private ScheduleCateRepository scheduleCateRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MiscUtil miscUtil;

    @Override
    public Page<ScheduleCategory> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging();
        }

        Page<ScheduleCategory> scheduleCategoryPage =
                scheduleCateRepository.findAllByStatusNot(Status.DELETED, paging);

        if (scheduleCategoryPage.isEmpty()) {
            return null;
        }

        return scheduleCategoryPage;
    }

    @Override
    public Page<ScheduleCaReadDTO> getPageDTOAll(Pageable paging) throws Exception {
        Page<ScheduleCategory> scheduleCategoryPage = getPageAll(paging);

        if (scheduleCategoryPage == null) {
            return null;
        }

        return wrapPageDTO(scheduleCategoryPage, null);
    }

    @Override
    public Page<ScheduleCaReadDTO> getPageDTOAll(Pageable paging, Collection<DtoOption> options) throws Exception {
        Page<ScheduleCategory> scheduleCategoryPage = getPageAll(paging);

        if (scheduleCategoryPage == null) {
            return null;
        }

        return wrapPageDTO(scheduleCategoryPage, options);
    }

    @Override
    public List<ScheduleCategory> getAll() throws Exception {

        List<ScheduleCategory> scheduleCategoryList =
                scheduleCateRepository.findAllByStatusNot(Status.DELETED);

        if (scheduleCategoryList.isEmpty()) {
            return null;
        }

        return scheduleCategoryList;
    }

    @Override
    public List<ScheduleCaReadDTO> getAllDTO() throws Exception {
        List<ScheduleCategory> scheduleCategoryList = getAll();

        if (scheduleCategoryList == null) {
            return null;
        }

        return wrapListDTO(scheduleCategoryList, null);
    }

    @Override
    public ScheduleCategory getById(Long id) throws Exception {
        return scheduleCateRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }

    @Override
    public ScheduleCaReadDTO getDTOById(Long id) throws Exception {
        return wrapDTO(getById(id), null);
    }

    @Override
    public List<ScheduleCategory> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<ScheduleCategory> scheduleCategorieList =
                scheduleCateRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);
        if (scheduleCategorieList.isEmpty()) {
            return null; }

        return scheduleCategorieList;
    }

    @Override
    public List<ScheduleCaReadDTO> getAllDTOByIdIn(Collection<Long> idCollection) throws Exception {
        return wrapListDTO(getAllByIdIn(idCollection),null);
    }

    

    @Override
    public Map<Long, ScheduleCaReadDTO> mapScheduleIdScheduleDescByIdIn(Collection<Long> idCollection) throws Exception {
        List<ScheduleCaReadDTO> scheduleCategoryList = getAllDTOByIdIn(idCollection);

        if (ObjectUtils.isEmpty(scheduleCategoryList)) {
            return new HashMap<>(); }

        return scheduleCategoryList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }
    


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ScheduleCaReadDTO wrapDTO(ScheduleCategory scheduleCategory, Collection<DtoOption> options) throws Exception {
        if (scheduleCategory == null){
            return null;
        }

        ScheduleCaReadDTO dto = mapper.map(scheduleCategory, ScheduleCaReadDTO.class);



        return dto;
    }

    @Override
    public List<ScheduleCaReadDTO> wrapListDTO(
            Collection<ScheduleCategory> scheduleCategoryCollection, Collection<DtoOption> options) throws Exception {
        List<ScheduleCaReadDTO> dtoList = new ArrayList<>();

        ScheduleCaReadDTO dto;

        if (scheduleCategoryCollection == null){
            return null;
        }


        for (ScheduleCategory scheduleCategory : scheduleCategoryCollection) {
            dto = mapper.map(scheduleCategory, ScheduleCaReadDTO.class);


            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ScheduleCaReadDTO> wrapPageDTO(
        Page<ScheduleCategory> scheduleCategoryPage, Collection<DtoOption> options) throws Exception {
            return new PageImpl<>(
                    wrapListDTO(scheduleCategoryPage.getContent(), options),
                    scheduleCategoryPage.getPageable(),
                    scheduleCategoryPage.getTotalPages());
    }


}
