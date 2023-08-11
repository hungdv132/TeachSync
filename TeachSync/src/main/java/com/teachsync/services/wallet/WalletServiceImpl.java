package com.teachsync.services.wallet;

import com.teachsync.dtos.wallet.WalletReadDTO;
import com.teachsync.entities.Wallet;
import com.teachsync.repositories.WalletRepository;
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
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public WalletReadDTO wrapDTO(Wallet wallet, Collection<DtoOption> options) throws Exception {
        WalletReadDTO dto = mapper.map(wallet, WalletReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(wallet.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(wallet.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<WalletReadDTO> wrapListDTO(Collection<Wallet> walletCollection, Collection<DtoOption> options) throws Exception {
        List<WalletReadDTO> dtoList = new ArrayList<>();
        WalletReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Wallet wallet : walletCollection) {
                fkIdSet.add(wallet.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Wallet wallet : walletCollection) {
            dto = mapper.map(wallet, WalletReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(wallet.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(wallet.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<WalletReadDTO> wrapPageDTO(Page<Wallet> walletPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(walletPage.getContent(), options),
                walletPage.getPageable(),
                walletPage.getTotalPages());
    }
}
