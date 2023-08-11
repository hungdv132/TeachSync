package com.teachsync.services.applicationDetail;

import com.teachsync.dtos.applicationDetail.ApplicationDetailReadDTO;
import com.teachsync.dtos.recruitmentCampaign.RecruitmentCampaignReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.ApplicationDetail;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ApplicationDetailService {
    /* =================================================== CREATE =================================================== */

    void add(ApplicationDetailReadDTO applicationDetailReadDTO, UserReadDTO userDTO,Long campaignId) throws Exception;

    /* =================================================== READ ===================================================== */
    /* applicationId */
    List<ApplicationDetail> getAllByApplicationId(Long applicationId) throws Exception;
    List<ApplicationDetailReadDTO> getAllDTOByApplicationId(
            Long applicationId, Collection<DtoOption> options) throws Exception;

    List<ApplicationDetail> getAllByApplicationIdIn(Collection<Long> applicationIdCollection) throws Exception;
    List<ApplicationDetailReadDTO> getAllDTOByApplicationIdIn(
            Collection<Long> applicationIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<ApplicationDetailReadDTO>> mapApplicationIdListDTOByApplicationIdIn(
            Collection<Long> applicationIdCollection, Collection<DtoOption> options) throws Exception;

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    ApplicationDetailReadDTO wrapDTO(
            ApplicationDetail applicationDetail, Collection<DtoOption> options) throws Exception;
    List<ApplicationDetailReadDTO> wrapListDTO(
            Collection<ApplicationDetail> applicationDetailCollection, Collection<DtoOption> options) throws Exception;
    Page<ApplicationDetailReadDTO> wrapPageDTO(
            Page<ApplicationDetail> applicationDetailPage, Collection<DtoOption> options) throws Exception;
}
