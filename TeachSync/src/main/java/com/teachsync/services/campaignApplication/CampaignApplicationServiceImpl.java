package com.teachsync.services.campaignApplication;

import com.teachsync.dtos.applicationDetail.ApplicationDetailReadDTO;
import com.teachsync.dtos.campaignApplication.CampaignApplicationReadDTO;
import com.teachsync.dtos.recruitmentCampaign.RecruitmentCampaignReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.CampaignApplication;
import com.teachsync.entities.RecruitmentCampaign;
import com.teachsync.entities.Request;
import com.teachsync.entities.User;
import com.teachsync.repositories.CampaignApplicationRepository;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.applicationDetail.ApplicationDetailService;
import com.teachsync.services.recruitmentCampaign.RecruitmentCampaignService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class CampaignApplicationServiceImpl implements CampaignApplicationService {
    @Autowired
    private CampaignApplicationRepository campaignApplicationRepository;

    @Lazy
    @Autowired
    private RecruitmentCampaignService recruitmentCampaignService;
    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationDetailService applicationDetailService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */

    @Override
    public Page<CampaignApplicationReadDTO> getAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<CampaignApplication> campaignApplicationPage = campaignApplicationRepository.findAllByStatusNotOrderByCreatedAtDesc(Status.DELETED, pageable);

        if (campaignApplicationPage == null) {
            return null;
        }

        Page<CampaignApplicationReadDTO> campaignApplicationList = wrapPageDTO(campaignApplicationPage, options);
        for (CampaignApplicationReadDTO campaignApplication : campaignApplicationList.getContent()) {
            if (!ObjectUtils.isEmpty(campaignApplication.getDetailList())) {
                campaignApplication.setApplicationDetail(campaignApplication.getDetailList().get(0));
            }
        }

        return campaignApplicationList;
    }

    /* id */
    @Override
    public CampaignApplication getById(Long id) throws Exception {
        return campaignApplicationRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }

    @Override
    public CampaignApplicationReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        CampaignApplication application = getById(id);

        if (application == null) {
            return null;
        }

        return wrapDTO(application, options);
    }

    /* campaignId */
    @Override
    public List<CampaignApplication> getAllByCampaignId(Long campaignId) throws Exception {
        List<CampaignApplication> applicationList =
                campaignApplicationRepository.findAllByCampaignIdAndStatusNot(campaignId, Status.DELETED);

        if (applicationList.isEmpty()) {
            return null;
        }

        return applicationList;
    }

    @Override
    public List<CampaignApplicationReadDTO> getAllDTOByCampaignId(
            Long campaignId, Collection<DtoOption> options) throws Exception {
        List<CampaignApplication> applicationList = getAllByCampaignId(campaignId);

        if (applicationList == null) {
            return null;
        }

        return wrapListDTO(applicationList, options);
    }

    @Override
    public List<CampaignApplication> getAllByCampaignIdIn(Collection<Long> campaignIdCollection) throws Exception {
        List<CampaignApplication> applicationList =
                campaignApplicationRepository.findAllByCampaignIdInAndStatusNot(campaignIdCollection, Status.DELETED);

        if (applicationList.isEmpty()) {
            return null;
        }

        return applicationList;
    }

    @Override
    public List<CampaignApplicationReadDTO> getAllDTOByCampaignIdIn(
            Collection<Long> campaignIdCollection, Collection<DtoOption> options) throws Exception {
        List<CampaignApplication> applicationList = getAllByCampaignIdIn(campaignIdCollection);

        if (applicationList == null) {
            return null;
        }

        return wrapListDTO(applicationList, options);
    }

    @Override
    public Map<Long, List<CampaignApplicationReadDTO>> mapCampaignIdListDTOByCampaignIdIn(
            Collection<Long> campaignIdCollection, Collection<DtoOption> options) throws Exception {
        List<CampaignApplicationReadDTO> applicationDTOList = getAllDTOByCampaignIdIn(campaignIdCollection, options);

        if (applicationDTOList == null) {
            return new HashMap<>();
        }

        Map<Long, List<CampaignApplicationReadDTO>> campaignIdDTOListMap = new HashMap<>();
        Long campaignId;
        List<CampaignApplicationReadDTO> tmpApplicationDTOList;

        for (CampaignApplicationReadDTO applicationDTO : applicationDTOList) {
            campaignId = applicationDTO.getCampaignId();

            tmpApplicationDTOList = campaignIdDTOListMap.get(campaignId);

            if (tmpApplicationDTOList == null) {
                campaignIdDTOListMap.put(campaignId, new ArrayList<>(List.of(applicationDTO)));
            } else {
                tmpApplicationDTOList.add(applicationDTO);
                campaignIdDTOListMap.put(campaignId, tmpApplicationDTOList);
            }
        }

        return campaignIdDTOListMap;
    }

    /* userId */
    @Override
    public List<CampaignApplication> getAllByUserId(Long userId) throws Exception {
        List<CampaignApplication> applicationList =
                campaignApplicationRepository.findAllByApplicantIdAndStatusNot(userId, Status.DELETED);

        if (applicationList.isEmpty()) {
            return null;
        }

        return applicationList;
    }

    @Override
    public Page<CampaignApplicationReadDTO> getAllPageDTOByUserId(Pageable pageable, Long userId, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<CampaignApplication> campaignApplicationPage = campaignApplicationRepository.findAllByCreatedByAndStatusNotOrderByCreatedAtDesc(userId, Status.DELETED, pageable);

        if (campaignApplicationPage == null) {
            return null;
        }

        Page<CampaignApplicationReadDTO> campaignApplicationList = wrapPageDTO(campaignApplicationPage, options);
        for (CampaignApplicationReadDTO campaignApplication : campaignApplicationList.getContent()) {
            if (!ObjectUtils.isEmpty(campaignApplication.getDetailList())) {
                campaignApplication.setApplicationDetail(campaignApplication.getDetailList().get(0));
            }
        }

        return campaignApplicationList;
    }

    @Override
    public List<CampaignApplicationReadDTO> getAllDTOByUserId(
            Long userId, Collection<DtoOption> options) throws Exception {
        List<CampaignApplication> applicationList = getAllByUserId(userId);

        if (applicationList == null) {
            return null;
        }

        return wrapListDTO(applicationList, options);
    }

    @Override
    public List<CampaignApplication> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        List<CampaignApplication> applicationList =
                campaignApplicationRepository.findAllByApplicantIdInAndStatusNot(userIdCollection, Status.DELETED);

        if (applicationList.isEmpty()) {
            return null;
        }

        return applicationList;
    }

    @Override
    public List<CampaignApplicationReadDTO> getAllDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception {
        List<CampaignApplication> applicationList = getAllByUserIdIn(userIdCollection);

        if (applicationList == null) {
            return null;
        }

        return wrapListDTO(applicationList, options);
    }

    @Override
    public Map<Long, List<CampaignApplicationReadDTO>> mapUserIdListDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception {
        List<CampaignApplicationReadDTO> applicationDTOList = getAllDTOByUserIdIn(userIdCollection, options);

        if (applicationDTOList == null) {
            return new HashMap<>();
        }

        Map<Long, List<CampaignApplicationReadDTO>> userIdDTOListMap = new HashMap<>();
        Long userId;
        List<CampaignApplicationReadDTO> tmpApplicationDTOList;

        for (CampaignApplicationReadDTO applicationDTO : applicationDTOList) {
            userId = applicationDTO.getApplicantId();

            tmpApplicationDTOList = userIdDTOListMap.get(userId);

            if (tmpApplicationDTOList == null) {
                userIdDTOListMap.put(userId, new ArrayList<>(List.of(applicationDTO)));
            } else {
                tmpApplicationDTOList.add(applicationDTO);
                userIdDTOListMap.put(userId, tmpApplicationDTOList);
            }
        }

        return userIdDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */

    @Transactional
    @Override
    public void changeStatus(Long Id, String operation) throws Exception {
        CampaignApplication campaignApplication = campaignApplicationRepository.findByIdAndStatusNot(Id, Status.DELETED).orElseThrow(() -> new Exception("không tìm thấy yêu cầu"));
        if (operation.equals("approve")) {
            User user = userRepository.findByIdAndStatusNot(campaignApplication.getCreatedBy(), Status.DELETED).orElseThrow(() -> new Exception("không tìm thấy tài khoản"));
            user.setRoleId(Constants.ROLE_TEACHER);
            userRepository.save(user);
            campaignApplication.setResult("Đã duyệt");
        } else {
            campaignApplication.setResult("Đã từ chối");
        }
        CampaignApplication requestDB = campaignApplicationRepository.save(campaignApplication);
        if (ObjectUtils.isEmpty(requestDB)) {
            throw new Exception("Lỗi khi update yêu cầu");
        }
    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CampaignApplicationReadDTO wrapDTO(
            CampaignApplication application, Collection<DtoOption> options) throws Exception {
        CampaignApplicationReadDTO dto = mapper.map(application, CampaignApplicationReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CAMPAIGN)) {
                RecruitmentCampaignReadDTO campaignDTO =
                        recruitmentCampaignService.getDTOById(application.getCampaignId(), options);
                dto.setCampaign(campaignDTO);
            }

            if (options.contains(DtoOption.USER)) {
                UserReadDTO userDTO = userService.getDTOById(application.getApplicantId(), options);
                dto.setApplicant(userDTO);
            }

            if (options.contains(DtoOption.APPLICATION_DETAIL_LIST)) {
                List<ApplicationDetailReadDTO> detailDTOList =
                        applicationDetailService.getAllDTOByApplicationId(application.getId(), options);
                dto.setDetailList(detailDTOList);
            }
        }

        return dto;
    }

    @Override
    public List<CampaignApplicationReadDTO> wrapListDTO(
            Collection<CampaignApplication> applicationCollection, Collection<DtoOption> options) throws Exception {
        List<CampaignApplicationReadDTO> dtoList = new ArrayList<>();
        CampaignApplicationReadDTO dto;

        Map<Long, RecruitmentCampaignReadDTO> campaignIdCampaignDTOMap = new HashMap<>();
        Map<Long, UserReadDTO> userIdUserDTOMap = new HashMap<>();
        Map<Long, List<ApplicationDetailReadDTO>> applicationIdDetailDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> campaignIdSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();
            Set<Long> applicationIdSet = new HashSet<>();

            for (CampaignApplication application : applicationCollection) {
                campaignIdSet.add(application.getCampaignId());
                userIdSet.add(application.getApplicantId());
                applicationIdSet.add(application.getId());
            }

            if (options.contains(DtoOption.CAMPAIGN)) {
                campaignIdCampaignDTOMap = recruitmentCampaignService.mapIdDTOByIdIn(campaignIdSet, options);
            }

            if (options.contains(DtoOption.USER)) {
                userIdUserDTOMap = userService.mapIdDTOByIdIn(userIdSet, options);
            }

            if (options.contains(DtoOption.APPLICATION_DETAIL_LIST)) {
                applicationIdDetailDTOListMap =
                        applicationDetailService.mapApplicationIdListDTOByApplicationIdIn(applicationIdSet, options);
            }
        }

        for (CampaignApplication application : applicationCollection) {
            dto = mapper.map(application, CampaignApplicationReadDTO.class);

            /* Add dependency */
            dto.setCampaign(campaignIdCampaignDTOMap.get(application.getCampaignId()));

            dto.setApplicant(userIdUserDTOMap.get(application.getApplicantId()));

            dto.setDetailList(applicationIdDetailDTOListMap.get(application.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<CampaignApplicationReadDTO> wrapPageDTO(
            Page<CampaignApplication> applicationPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(applicationPage.getContent(), options),
                applicationPage.getPageable(),
                applicationPage.getTotalPages());
    }


}
