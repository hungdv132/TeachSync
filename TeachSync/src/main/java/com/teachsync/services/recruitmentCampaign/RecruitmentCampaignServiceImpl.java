package com.teachsync.services.recruitmentCampaign;

import com.teachsync.dtos.campaignApplication.CampaignApplicationReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.recruitmentCampaign.RecruitmentCampaignReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Homework;
import com.teachsync.entities.RecruitmentCampaign;
import com.teachsync.repositories.RecruitmentCampaignRepository;
import com.teachsync.services.campaignApplication.CampaignApplicationService;
import com.teachsync.services.center.CenterService;
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
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RecruitmentCampaignServiceImpl implements RecruitmentCampaignService {
    @Autowired
    private RecruitmentCampaignRepository recruitmentCampaignRepository;

    @Lazy
    @Autowired
    private CenterService centerService;
    @Autowired
    private CampaignApplicationService campaignApplicationService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */

    @Override
    public void add(RecruitmentCampaignReadDTO recruitmentCampaignReadDTO, UserReadDTO userDTO) throws Exception {

        RecruitmentCampaign recruitmentCampaign = new RecruitmentCampaign();

        recruitmentCampaign.setCampaignName(recruitmentCampaignReadDTO.getCampaignName());
        recruitmentCampaign.setCenterId(recruitmentCampaignReadDTO.getCenterId());
        recruitmentCampaign.setCampaignImg(recruitmentCampaignReadDTO.getCampaignImg());
        recruitmentCampaign.setCampaignDesc(recruitmentCampaignReadDTO.getCampaignDesc());
        recruitmentCampaign.setPosition(recruitmentCampaignReadDTO.getPosition());
        recruitmentCampaign.setOpeningSlot(recruitmentCampaignReadDTO.getOpeningSlot());
        recruitmentCampaign.setRecruitFrom(recruitmentCampaignReadDTO.getRecruitFrom());
        recruitmentCampaign.setRecruitTo(recruitmentCampaignReadDTO.getRecruitTo());

        recruitmentCampaign.setCreatedBy(userDTO.getId());
        recruitmentCampaign.setUpdatedBy(userDTO.getId());
        recruitmentCampaign.setStatus(Status.CREATED);

        RecruitmentCampaign recruitmentCampaignDb = recruitmentCampaignRepository.save(recruitmentCampaign);
        if (ObjectUtils.isEmpty(recruitmentCampaignDb)) {
            throw new Exception("Lỗi khi tạo chiến dịch");
        }
    }

    /* =================================================== READ ===================================================== */
    @Override
    public Page<RecruitmentCampaign> getPageAll(Pageable pageable) throws Exception {
        Page<RecruitmentCampaign> campaignPage =
                recruitmentCampaignRepository.findAllByStatusNot(Status.DELETED, pageable);

        if (campaignPage.isEmpty()) {
            return null;
        }

        return campaignPage;
    }

    @Override
    public Page<RecruitmentCampaignReadDTO> getPageAllDTO(
            Pageable pageable, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<RecruitmentCampaign> campaignPage = getPageAll(pageable);

        if (campaignPage == null) {
            return null;
        }

        return wrapPageDTO(campaignPage, options);
    }

    /* id */
    @Override
    public RecruitmentCampaign getById(Long id) throws Exception {
        return recruitmentCampaignRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }

    @Override
    public RecruitmentCampaignReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        RecruitmentCampaign campaign = getById(id);

        if (campaign == null) {
            return null;
        }

        return wrapDTO(campaign, options);
    }

    @Override
    public List<RecruitmentCampaign> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<RecruitmentCampaign> campaignList =
                recruitmentCampaignRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (campaignList.isEmpty()) {
            return null;
        }

        return campaignList;
    }

    @Override
    public List<RecruitmentCampaignReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<RecruitmentCampaign> campaignList = getAllByIdIn(idCollection);

        if (campaignList == null) {
            return null;
        }

        return wrapListDTO(campaignList, options);
    }

    @Override
    public Map<Long, RecruitmentCampaignReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        return null;
    }

    /* centerId */
    @Override
    public Page<RecruitmentCampaign> getPageAllByCenterId(Long centerId, Pageable pageable) throws Exception {
        Page<RecruitmentCampaign> campaignPage =
                recruitmentCampaignRepository.findAllByCenterIdAndStatusNot(centerId, Status.DELETED, pageable);

        if (campaignPage.isEmpty()) {
            return null;
        }

        return campaignPage;
    }

    @Override
    public Page<RecruitmentCampaignReadDTO> getPageAllDTOByCenterId(
            Long centerId, Pageable pageable, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<RecruitmentCampaign> campaignPage = getPageAllByCenterId(centerId, pageable);

        if (campaignPage == null) {
            return null;
        }

        return wrapPageDTO(campaignPage, options);
    }

    @Override
    public List<RecruitmentCampaign> getAllByCenterId(Long centerId) throws Exception {
        List<RecruitmentCampaign> campaignList =
                recruitmentCampaignRepository.findAllByCenterIdAndStatusNot(centerId, Status.DELETED);

        if (campaignList.isEmpty()) {
            return null;
        }

        return campaignList;
    }

    @Override
    public List<RecruitmentCampaignReadDTO> getAllDTOByCenterId(
            Long centerId, Collection<DtoOption> options) throws Exception {
        List<RecruitmentCampaign> campaignList = getAllByCenterId(centerId);

        if (campaignList == null) {
            return null;
        }

        return wrapListDTO(campaignList, options);
    }


    /* =================================================== UPDATE =================================================== */

    @Override
    public void edit(RecruitmentCampaignReadDTO recruitmentCampaignReadDTO, UserReadDTO userDTO) throws Exception {
        RecruitmentCampaign recruitmentCampaign = recruitmentCampaignRepository.findById(recruitmentCampaignReadDTO.getId()).orElseThrow(() -> new Exception("không tìm thấy chiến dịch"));

        recruitmentCampaign.setCampaignName(recruitmentCampaignReadDTO.getCampaignName());
        recruitmentCampaign.setCenterId(recruitmentCampaignReadDTO.getCenterId());
        recruitmentCampaign.setCampaignImg(recruitmentCampaignReadDTO.getCampaignImg());
        recruitmentCampaign.setCampaignDesc(recruitmentCampaignReadDTO.getCampaignDesc());
        recruitmentCampaign.setPosition(recruitmentCampaignReadDTO.getPosition());
        recruitmentCampaign.setOpeningSlot(recruitmentCampaignReadDTO.getOpeningSlot());
        recruitmentCampaign.setRecruitFrom(recruitmentCampaignReadDTO.getRecruitFrom());
        recruitmentCampaign.setRecruitTo(recruitmentCampaignReadDTO.getRecruitTo());

        recruitmentCampaign.setCreatedBy(userDTO.getId());
        recruitmentCampaign.setUpdatedBy(userDTO.getId());
        recruitmentCampaign.setStatus(Status.UPDATED);

        RecruitmentCampaign recruitmentCampaignDb = recruitmentCampaignRepository.save(recruitmentCampaign);
        if (ObjectUtils.isEmpty(recruitmentCampaignDb)) {
            throw new Exception("Lỗi khi sửa chiến dịch");
        }
    }

    /* =================================================== DELETE =================================================== */

    @Override
    public void delete(Long Id, UserReadDTO userDTO) throws Exception {
        RecruitmentCampaign recruitmentCampaign = recruitmentCampaignRepository.findById(Id).orElseThrow(() -> new Exception("không tìm thấy chiến dịch"));

        recruitmentCampaign.setUpdatedBy(userDTO.getId());
        recruitmentCampaign.setStatus(Status.DELETED);
        RecruitmentCampaign recruitmentCampaignDb = recruitmentCampaignRepository.save(recruitmentCampaign);
        if (ObjectUtils.isEmpty(recruitmentCampaignDb)) {
            throw new Exception("Lỗi khi sửa chiến dịch");
        }
    }

    /* =================================================== WRAPPER ================================================== */
    @Override
    public RecruitmentCampaignReadDTO wrapDTO(
            RecruitmentCampaign campaign, Collection<DtoOption> options) throws Exception {
        RecruitmentCampaignReadDTO dto = mapper.map(campaign, RecruitmentCampaignReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CENTER)) {
                CenterReadDTO centerDTO = centerService.getDTOById(campaign.getCenterId(), options);
                dto.setCenter(centerDTO);
            }

            if (options.contains(DtoOption.APPLICATION_LIST)) {
                List<CampaignApplicationReadDTO> applicationDTOList =
                        campaignApplicationService.getAllDTOByCampaignId(campaign.getId(), options);
                dto.setApplicationList(applicationDTOList);
            }
        }

        return dto;
    }

    @Override
    public List<RecruitmentCampaignReadDTO> wrapListDTO(
            Collection<RecruitmentCampaign> campaignCollection, Collection<DtoOption> options) throws Exception {
        List<RecruitmentCampaignReadDTO> dtoList = new ArrayList<>();
        RecruitmentCampaignReadDTO dto;

        Map<Long, CenterReadDTO> centerIdCenterDTOMap = new HashMap<>();
        Map<Long, List<CampaignApplicationReadDTO>> campaignIdApplicationListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> centerIdSet = new HashSet<>();
            Set<Long> campaignIdSet = new HashSet<>();

            for (RecruitmentCampaign campaign : campaignCollection) {
                centerIdSet.add(campaign.getCenterId());
                campaignIdSet.add(campaign.getId());
            }

            if (options.contains(DtoOption.CENTER)) {
                centerIdCenterDTOMap = centerService.mapIdDTOByIdIn(centerIdSet, options);
            }

            if (options.contains(DtoOption.APPLICATION_LIST)) {
                campaignIdApplicationListMap =
                        campaignApplicationService.mapCampaignIdListDTOByCampaignIdIn(campaignIdSet, options);
            }
        }

        for (RecruitmentCampaign campaign : campaignCollection) {
            dto = mapper.map(campaign, RecruitmentCampaignReadDTO.class);

            /* Add dependency */
            dto.setCenter(centerIdCenterDTOMap.get(campaign.getCenterId()));

            dto.setApplicationList(campaignIdApplicationListMap.get(campaign.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<RecruitmentCampaignReadDTO> wrapPageDTO(
            Page<RecruitmentCampaign> campaignPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(campaignPage.getContent(), options),
                campaignPage.getPageable(),
                campaignPage.getTotalPages());
    }
}
