package com.teachsync.services.answer;

import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.entities.Answer;
import com.teachsync.repositories.AnswerRepository;
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
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public AnswerReadDTO wrapDTO(Answer answer, Collection<DtoOption> options) throws Exception {
        AnswerReadDTO dto = mapper.map(answer, AnswerReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(answer.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(answer.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<AnswerReadDTO> wrapListDTO(Collection<Answer> answerCollection, Collection<DtoOption> options) throws Exception {
        List<AnswerReadDTO> dtoList = new ArrayList<>();
        AnswerReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Answer answer : answerCollection) {
                fkIdSet.add(answer.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Answer answer : answerCollection) {
            dto = mapper.map(answer, AnswerReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(answer.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(answer.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<AnswerReadDTO> wrapPageDTO(Page<Answer> answerPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(answerPage.getContent(), options),
                answerPage.getPageable(),
                answerPage.getTotalPages());
    }
}
