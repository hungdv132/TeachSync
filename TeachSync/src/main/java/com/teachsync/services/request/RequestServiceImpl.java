package com.teachsync.services.request;

import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.entities.Request;
import com.teachsync.repositories.RequestRepository;
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
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RequestReadDTO wrapDTO(Request request, Collection<DtoOption> options) throws Exception {
        RequestReadDTO dto = mapper.map(request, RequestReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(request.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(request.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<RequestReadDTO> wrapListDTO(Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception {
        List<RequestReadDTO> dtoList = new ArrayList<>();
        RequestReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Request request : requestCollection) {
                fkIdSet.add(request.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Request request : requestCollection) {
            dto = mapper.map(request, RequestReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(request.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(request.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<RequestReadDTO> wrapPageDTO(Page<Request> requestPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(requestPage.getContent(), options),
                requestPage.getPageable(),
                requestPage.getTotalPages());
    }
}
