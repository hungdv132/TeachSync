package com.teachsync.services.wallet;

import com.teachsync.dtos.wallet.WalletReadDTO;
import com.teachsync.entities.Wallet;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface WalletService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    WalletReadDTO wrapDTO(Wallet wallet, Collection<DtoOption> options) throws Exception;
    List<WalletReadDTO> wrapListDTO(Collection<Wallet> walletCollection, Collection<DtoOption> options) throws Exception;
    Page<WalletReadDTO> wrapPageDTO(Page<Wallet> walletPage, Collection<DtoOption> options) throws Exception;
}
