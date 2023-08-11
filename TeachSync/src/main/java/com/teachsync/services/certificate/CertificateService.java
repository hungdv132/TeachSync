package com.teachsync.services.certificate;

import com.teachsync.dtos.certificate.CertificateReadDTO;
import com.teachsync.entities.Certificate;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface CertificateService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    CertificateReadDTO wrapDTO(Certificate certificate, Collection<DtoOption> options) throws Exception;
    List<CertificateReadDTO> wrapListDTO(Collection<Certificate> certificateCollection, Collection<DtoOption> options) throws Exception;
    Page<CertificateReadDTO> wrapPageDTO(Page<Certificate> certificatePage, Collection<DtoOption> options) throws Exception;
}
