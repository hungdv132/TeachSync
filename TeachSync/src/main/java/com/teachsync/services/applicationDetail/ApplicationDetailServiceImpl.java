package com.teachsync.services.applicationDetail;

import com.teachsync.dtos.applicationDetail.ApplicationDetailReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.ApplicationDetail;
import com.teachsync.entities.CampaignApplication;
import com.teachsync.entities.RecruitmentCampaign;
import com.teachsync.repositories.ApplicationDetailRepository;
import com.teachsync.repositories.CampaignApplicationRepository;
import com.teachsync.services.campaignApplication.CampaignApplicationService;
import com.teachsync.utils.enums.ApplicationDetailType;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApplicationDetailServiceImpl implements ApplicationDetailService {
    @Autowired
    private ApplicationDetailRepository applicationDetailRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CampaignApplicationRepository campaignApplicationRepository;


    /* =================================================== CREATE =================================================== */

    @Override
    @Transactional
    public void add(ApplicationDetailReadDTO applicationDetailReadDTO, UserReadDTO userDTO, Long campaignId) throws Exception {

        List<CampaignApplication> campaignApplicationCheck = campaignApplicationRepository.findAllByCreatedByAndStatusNot(userDTO.getId(), Status.DELETED);
        for (CampaignApplication campaign : campaignApplicationCheck) {
            if (!ObjectUtils.isEmpty(campaign)) {
                if (campaign.getResult().equals("Đang chờ duyệt")) {
                    throw new Exception("Tài khoản đã tạo đơn ứng tuyển và dang chờ duyệt , vui lòng chờ đợi kết quả");
                } else if (campaign.getResult().equals("Đã duyệt")) {
                    throw new Exception("Tài khoản đã tạo đơn ứng tuyển và đã được duyêt , vui lòng không tạo đơn nữa");
                }
            }
        }

        //add campaign Application
        CampaignApplication campaignApplication = new CampaignApplication();
        campaignApplication.setCampaignId(campaignId);
        campaignApplication.setApplicantId(userDTO.getId());
        campaignApplication.setAppliedAt(LocalDateTime.now());
        campaignApplication.setResult("Đang chờ duyệt");
        campaignApplication.setResultDate(LocalDateTime.now());

        campaignApplication.setCreatedBy(userDTO.getId());
        campaignApplication.setUpdatedBy(userDTO.getId());
        campaignApplication.setStatus(Status.CREATED);

        CampaignApplication campaignApplicationDb = campaignApplicationRepository.save(campaignApplication);

        if (ObjectUtils.isEmpty(campaignApplicationDb)) {
            throw new Exception("Lỗi khi tạo campaign Application ");
        }

        // add ApplicationDetail
        ApplicationDetail applicationDetail = new ApplicationDetail();
        applicationDetail.setApplicationId(campaignApplicationDb.getId());
        applicationDetail.setDetailType(applicationDetailReadDTO.getDetailType());
        applicationDetail.setDetailLink(applicationDetailReadDTO.getDetailLink());
        applicationDetail.setDetailNote(applicationDetailReadDTO.getDetailNote());//TODO:upload file
        applicationDetail.setSubmitAt(LocalDateTime.now());

        applicationDetail.setCreatedBy(userDTO.getId());
        applicationDetail.setUpdatedBy(userDTO.getId());
        applicationDetail.setStatus(Status.CREATED);

        ApplicationDetail applicationDetailDb = applicationDetailRepository.save(applicationDetail);
        if (ObjectUtils.isEmpty(applicationDetailDb)) {
            throw new Exception("Lỗi khi tạo đơn ứng tuyển");
        }

    }

    /* =================================================== READ ===================================================== */

    @Override
    public ApplicationDetailReadDTO getById(Long id) throws Exception {
        ApplicationDetail applicationDetail = applicationDetailRepository.findById(id).orElseThrow(() -> new Exception("không tìm đơn của ứng viên"));
        return mapper.map(applicationDetail, ApplicationDetailReadDTO.class);
    }

    /* applicationId */
    @Override
    public List<ApplicationDetail> getAllByApplicationId(Long applicationId) throws Exception {
        List<ApplicationDetail> detailList =
                applicationDetailRepository.findAllByApplicationIdAndStatusNot(applicationId, Status.DELETED);

        if (detailList.isEmpty()) {
            return null;
        }

        return detailList;
    }

    @Override
    public List<ApplicationDetailReadDTO> getAllDTOByApplicationId(
            Long applicationId, Collection<DtoOption> options) throws Exception {
        List<ApplicationDetail> detailList = getAllByApplicationId(applicationId);

        if (detailList == null) {
            return null;
        }

        return wrapListDTO(detailList, options);
    }

    @Override
    public List<ApplicationDetail> getAllByApplicationIdIn(Collection<Long> applicationIdCollection) throws Exception {
        List<ApplicationDetail> detailList =
                applicationDetailRepository.findAllByApplicationIdInAndStatusNot(applicationIdCollection, Status.DELETED);

        if (detailList.isEmpty()) {
            return null;
        }

        return detailList;
    }

    @Override
    public List<ApplicationDetailReadDTO> getAllDTOByApplicationIdIn(
            Collection<Long> applicationIdCollection, Collection<DtoOption> options) throws Exception {
        List<ApplicationDetail> detailList = getAllByApplicationIdIn(applicationIdCollection);

        if (detailList == null) {
            return null;
        }

        return wrapListDTO(detailList, options);
    }

    @Override
    public Map<Long, List<ApplicationDetailReadDTO>> mapApplicationIdListDTOByApplicationIdIn(
            Collection<Long> applicationIdCollection, Collection<DtoOption> options) throws Exception {
        List<ApplicationDetailReadDTO> detailDTOList = getAllDTOByApplicationIdIn(applicationIdCollection, options);

        if (detailDTOList == null) {
            return new HashMap<>();
        }

        Map<Long, List<ApplicationDetailReadDTO>> applicationIdDTOListMap = new HashMap<>();
        Long applicationId;
        List<ApplicationDetailReadDTO> tmpDetailDTOList;

        for (ApplicationDetailReadDTO detailDTO : detailDTOList) {
            applicationId = detailDTO.getApplicationId();

            tmpDetailDTOList = applicationIdDTOListMap.get(applicationId);

            if (tmpDetailDTOList == null) {
                applicationIdDTOListMap.put(applicationId, new ArrayList<>(List.of(detailDTO)));
            } else {
                tmpDetailDTOList.add(detailDTO);
                applicationIdDTOListMap.put(applicationId, detailDTOList);
            }
        }

        return applicationIdDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ApplicationDetailReadDTO wrapDTO(
            ApplicationDetail applicationDetail, Collection<DtoOption> options) throws Exception {
        ApplicationDetailReadDTO dto = mapper.map(applicationDetail, ApplicationDetailReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(applicationDetail.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(applicationDetail.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<ApplicationDetailReadDTO> wrapListDTO(
            Collection<ApplicationDetail> applicationDetailCollection, Collection<DtoOption> options) throws Exception {
        List<ApplicationDetailReadDTO> dtoList = new ArrayList<>();
        ApplicationDetailReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (ApplicationDetail applicationDetail : applicationDetailCollection) {
                fkIdSet.add(applicationDetail.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (ApplicationDetail applicationDetail : applicationDetailCollection) {
            dto = mapper.map(applicationDetail, ApplicationDetailReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(applicationDetail.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(applicationDetail.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ApplicationDetailReadDTO> wrapPageDTO(
            Page<ApplicationDetail> applicationDetailPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(applicationDetailPage.getContent(), options),
                applicationDetailPage.getPageable(),
                applicationDetailPage.getTotalPages());
    }
}
