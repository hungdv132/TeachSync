package com.teachsync.repositories;

import com.teachsync.entities.ApplicationDetail;
import com.teachsync.entities.CampaignApplication;
import com.teachsync.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationDetailRepository extends JpaRepository<ApplicationDetail, Long> {

    ApplicationDetail findByCreatedByAndStatusNot(Long id, Status status);

    /* applicationId */
    List<ApplicationDetail> findAllByApplicationIdAndStatusNot(Long applicationId, Status status);
    List<ApplicationDetail> findAllByApplicationIdInAndStatusNot(Collection<Long> applicationIdCollection, Status status);
}