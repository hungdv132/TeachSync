package com.teachsync.services.campaignApplication;

import com.teachsync.dtos.campaignApplication.CampaignApplicationReadDTO;
import com.teachsync.entities.CampaignApplication;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CampaignApplicationService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */

    Page<CampaignApplicationReadDTO> getAllDTO(Pageable pageable,Collection<DtoOption> options) throws Exception;

    /* id */
    CampaignApplication getById(Long id) throws Exception;
    CampaignApplicationReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    /* campaignId */
    List<CampaignApplication> getAllByCampaignId(Long campaignId) throws Exception;
    List<CampaignApplicationReadDTO> getAllDTOByCampaignId(
            Long campaignId, Collection<DtoOption> options) throws Exception;

    List<CampaignApplication> getAllByCampaignIdIn(Collection<Long> campaignIdCollection) throws Exception;
    List<CampaignApplicationReadDTO> getAllDTOByCampaignIdIn(
            Collection<Long> campaignIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<CampaignApplicationReadDTO>> mapCampaignIdListDTOByCampaignIdIn(
            Collection<Long> campaignIdCollection, Collection<DtoOption> options) throws Exception;

    /* userId */
    List<CampaignApplication> getAllByUserId(Long userId) throws Exception;

    Page<CampaignApplicationReadDTO> getAllPageDTOByUserId(Pageable pageable,Long userId, Collection<DtoOption> options) throws Exception;

    List<CampaignApplicationReadDTO> getAllDTOByUserId(Long userId, Collection<DtoOption> options) throws Exception;

    List<CampaignApplication> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception;
    List<CampaignApplicationReadDTO> getAllDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, List<CampaignApplicationReadDTO>> mapUserIdListDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */

    void changeStatus(Long Id, String operation) throws Exception;

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    CampaignApplicationReadDTO wrapDTO(CampaignApplication application, Collection<DtoOption> options) throws Exception;
    List<CampaignApplicationReadDTO> wrapListDTO(
            Collection<CampaignApplication> applicationCollection, Collection<DtoOption> options) throws Exception;
    Page<CampaignApplicationReadDTO> wrapPageDTO(
            Page<CampaignApplication> applicationPage, Collection<DtoOption> options) throws Exception;
}
