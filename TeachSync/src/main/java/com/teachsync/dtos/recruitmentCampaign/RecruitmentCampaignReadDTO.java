package com.teachsync.dtos.recruitmentCampaign;
import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.campaignApplication.CampaignApplicationReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.RecruitmentCampaign}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentCampaignReadDTO extends BaseReadDTO {
    private CenterReadDTO center;
    private Long centerId;

    private String campaignImg;

    private String campaignName;

    private String campaignDesc;

    private String position;

    private Integer openingSlot;

    private LocalDateTime recruitFrom;

    private String recruitFromShow;

    private LocalDateTime recruitTo;

    private String recruitToShow;

    private List<CampaignApplicationReadDTO> applicationList;

    public String getRecruitFromShow() {
        String pattern = "dd-MM-yyyy | HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ObjectUtils.isEmpty(recruitFrom) ? null : recruitFrom.format(formatter);
    }

    public String getRecruitToShow() {
        String pattern = "dd-MM-yyyy | HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ObjectUtils.isEmpty(recruitTo) ? null : recruitTo.format(formatter);
    }
}