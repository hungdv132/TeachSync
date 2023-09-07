package com.teachsync.services.scheduleCategory;

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

import java.util.*;

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
    public ScheduleCategory getById(Long id) throws Exception {
        return scheduleCateRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }

    @Override
    public ScheduleCaReadDTO getDTOById(Long id) throws Exception {
        ScheduleCategory scheduleCategory = getById(id);

        if (scheduleCategory == null) {
            return null;
        }

        return wrapDTO(scheduleCategory, null);
    }

    @Override
    public ScheduleCaReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        ScheduleCategory scheduleCategory = getById(id);

        if (scheduleCategory == null) {
            return null;
        }

        return wrapDTO(scheduleCategory, options);
    }

    /* =================================================== WRAPPER ================================================== */
    @Override
    public ScheduleCaReadDTO wrapDTO(ScheduleCategory scheduleCategory, Collection<DtoOption> options) throws Exception {
        ScheduleCaReadDTO dto = mapper.map(scheduleCategory, ScheduleCaReadDTO.class);


        return dto;
    }

    @Override
    public List<ScheduleCaReadDTO> wrapListDTO(
            Collection<ScheduleCategory> scheduleCategoryCollection, Collection<DtoOption> options) throws Exception {
        List<ScheduleCaReadDTO> dtoList = new ArrayList<>();

        ScheduleCaReadDTO dto;



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
