package com.teachsync.services.session;

import com.teachsync.dtos.session.SessionReadDTO;
import com.teachsync.entities.Session;
import com.teachsync.repositories.SessionRepository;
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
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public SessionReadDTO wrapDTO(Session session, Collection<DtoOption> options) throws Exception {
        SessionReadDTO dto = mapper.map(session, SessionReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(session.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(session.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<SessionReadDTO> wrapListDTO(Collection<Session> sessionCollection, Collection<DtoOption> options) throws Exception {
        List<SessionReadDTO> dtoList = new ArrayList<>();
        SessionReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Session session : sessionCollection) {
                fkIdSet.add(session.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Session session : sessionCollection) {
            dto = mapper.map(session, SessionReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(session.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(session.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<SessionReadDTO> wrapPageDTO(Page<Session> sessionPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(sessionPage.getContent(), options),
                sessionPage.getPageable(),
                sessionPage.getTotalPages());
    }
}
