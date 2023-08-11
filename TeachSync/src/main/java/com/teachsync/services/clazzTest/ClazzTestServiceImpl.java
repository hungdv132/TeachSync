package com.teachsync.services.clazzTest;

import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.entities.ClazzTest;
import com.teachsync.repositories.ClazzTestRepository;
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
public class ClazzTestServiceImpl implements ClazzTestService {
    @Autowired
    private ClazzTestRepository clazzTestRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzTestReadDTO wrapDTO(ClazzTest clazzTest, Collection<DtoOption> options) throws Exception {
        ClazzTestReadDTO dto = mapper.map(clazzTest, ClazzTestReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(clazzTest.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(clazzTest.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<ClazzTestReadDTO> wrapListDTO(Collection<ClazzTest> clazzTestCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzTestReadDTO> dtoList = new ArrayList<>();
        ClazzTestReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (ClazzTest clazzTest : clazzTestCollection) {
                fkIdSet.add(clazzTest.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (ClazzTest clazzTest : clazzTestCollection) {
            dto = mapper.map(clazzTest, ClazzTestReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(clazzTest.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(clazzTest.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ClazzTestReadDTO> wrapPageDTO(Page<ClazzTest> clazzTestPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(clazzTestPage.getContent(), options),
                clazzTestPage.getPageable(),
                clazzTestPage.getTotalPages());
    }
}
