package com.teachsync.repositories;

import com.teachsync.entities.RecruitmentCampaign;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentCampaignRepository extends JpaRepository<RecruitmentCampaign, Long> {

    Page<RecruitmentCampaign> findAllByStatusNot(Status status, Pageable pageable);

    /* id */
    Optional<RecruitmentCampaign> findByIdAndStatusNot(Long id, Status status);
    List<RecruitmentCampaign> findAllByIdInAndStatusNot(Collection<Long> idCollection, Status status);

    /* centerId */
    Page<RecruitmentCampaign> findAllByCenterIdAndStatusNot(Long centerId, Status status, Pageable pageable);
    List<RecruitmentCampaign> findAllByCenterIdAndStatusNot(Long centerId, Status status);
}