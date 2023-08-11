package com.teachsync.dtos.campaignApplication;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.applicationDetail.ApplicationDetailReadDTO;
import com.teachsync.dtos.recruitmentCampaign.RecruitmentCampaignReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.CampaignApplication}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignApplicationReadDTO extends BaseReadDTO {
    private RecruitmentCampaignReadDTO campaign;
    private Long campaignId;

    private UserReadDTO applicant;
    private Long applicantId;

    private LocalDateTime appliedAt;

    private String appliedAtShow;

    private List<ApplicationDetailReadDTO> detailList;

    private String result;

    private LocalDateTime resultDate;

    private ApplicationDetailReadDTO applicationDetail;

    public String getAppliedAtShow() {
        String pattern = "dd-MM-yyyy | HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ObjectUtils.isEmpty(appliedAt) ? null : appliedAt.format(formatter);
    }
}