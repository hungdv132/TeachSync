package com.teachsync.services.certificateIssue;

import com.teachsync.dtos.certificateIssue.CertificateIssueReadDTO;
import com.teachsync.entities.CertificateIssue;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface CertificateIssueService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    CertificateIssueReadDTO wrapDTO(CertificateIssue certificateIssue, Collection<DtoOption> options) throws Exception;
    List<CertificateIssueReadDTO> wrapListDTO(Collection<CertificateIssue> certificateIssueCollection, Collection<DtoOption> options) throws Exception;
    Page<CertificateIssueReadDTO> wrapPageDTO(Page<CertificateIssue> certificateIssuePage, Collection<DtoOption> options) throws Exception;
}
