package com.teachsync.repositories;

import com.teachsync.entities.ApplicationDetail;
import com.teachsync.entities.CampaignApplication;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignApplicationRepository extends JpaRepository<CampaignApplication, Long> {

    Page<CampaignApplication> findAllByStatusNotOrderByCreatedAtDesc(Status status, Pageable pageable);

    List<CampaignApplication> findAllByCreatedByAndStatusNot(Long id, Status status);

    /* id */
    Optional<CampaignApplication> findByIdAndStatusNot(Long id, Status status);

    /* campaignId */
    List<CampaignApplication> findAllByCampaignIdAndStatusNot(Long campaignId, Status status);

    List<CampaignApplication> findAllByCampaignIdInAndStatusNot(Collection<Long> campaignIdCollection, Status status);


    /* userId */

    Page<CampaignApplication> findAllByCreatedByAndStatusNotOrderByCreatedAtDesc(Long userId, Status status, Pageable pageable);

    List<CampaignApplication> findAllByApplicantIdAndStatusNot(Long userId, Status status);

    List<CampaignApplication> findAllByApplicantIdInAndStatusNot(Collection<Long> userIdCollection, Status status);
}