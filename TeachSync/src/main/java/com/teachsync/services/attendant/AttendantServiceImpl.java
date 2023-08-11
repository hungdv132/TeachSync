package com.teachsync.services.attendant;

import com.teachsync.dtos.attendant.AttendantReadDTO;
import com.teachsync.entities.Attendant;
import com.teachsync.repositories.AttendantRepository;
import com.teachsync.utils.enums.DtoOption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AttendantServiceImpl implements AttendantService {
    @Autowired
    private AttendantRepository attendantRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public AttendantReadDTO wrapDTO(Attendant attendant, Collection<DtoOption> options) throws Exception {
        AttendantReadDTO dto = mapper.map(attendant, AttendantReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(attendant.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(attendant.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<AttendantReadDTO> wrapListDTO(Collection<Attendant> attendantCollection, Collection<DtoOption> options) throws Exception {
        List<AttendantReadDTO> dtoList = new ArrayList<>();
        AttendantReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Attendant attendant : attendantCollection) {
                fkIdSet.add(attendant.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Attendant attendant : attendantCollection) {
            dto = mapper.map(attendant, AttendantReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(attendant.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(attendant.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<AttendantReadDTO> wrapPageDTO(Page<Attendant> attendantPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(attendantPage.getContent(), options),
                attendantPage.getPageable(),
                attendantPage.getTotalPages());
    }
}
