package com.teachsync.services.priceLog;

import com.teachsync.dtos.priceLog.PriceLogCreateDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.priceLog.PriceLogUpdateDTO;
import com.teachsync.entities.PriceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PriceLogService {
    /* =================================================== CREATE =================================================== */
    PriceLog createPriceLog(PriceLog price) throws Exception;
    PriceLogReadDTO createPriceLogByDTO(PriceLogCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    Page<PriceLog> getPageAll(Pageable paging) throws Exception;
    Page<PriceLogReadDTO> getPageAllDTO(Pageable paging) throws Exception;

    Page<PriceLog> getPageAllLatestPromotion(Pageable paging) throws Exception;
    Page<PriceLogReadDTO> getPageAllLatestPromotionDTO(Pageable paging) throws Exception;

    List<PriceLog> getAllLatestPromotion() throws Exception;
    List<PriceLogReadDTO> getAllLatestPromotionDTO() throws Exception;

    /* id */
    PriceLog getById(Long id) throws Exception;
    PriceLogReadDTO getDTOById(Long id) throws Exception;

    /* courseId */
    PriceLog getCurrentByCourseId(Long courseId) throws Exception;
    PriceLogReadDTO getCurrentDTOByCourseId(Long courseId) throws Exception;

    PriceLog getByCourseIdAt(Long courseId, LocalDateTime searchAt) throws Exception;
    PriceLogReadDTO getDTOByCourseIdAt(Long courseId, LocalDateTime searchAt) throws Exception;

    List<PriceLog> getAllCurrentByCourseIdIn(Collection<Long> courseIdCollection) throws Exception;
    List<PriceLogReadDTO> getAllCurrentDTOByCourseIdIn(Collection<Long> courseIdCollection) throws Exception;
    Map<Long, PriceLogReadDTO> mapCourseIdCurrentPriceDTOByCourseIdIn(Collection<Long> courseIdCollection) throws Exception;

    List<PriceLog> getAllByCourseId(Long courseId) throws Exception;
    List<PriceLogReadDTO> getAllDTOByCourseId(Long courseId) throws Exception;


    /* =================================================== UPDATE =================================================== */
    PriceLog updatePriceLog(PriceLog price) throws Exception;
    PriceLogReadDTO updatePriceLogByDTO(PriceLogUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */
    Boolean deletePriceLog(Long id) throws Exception;

    Boolean deleteAllByCourseId(Long courseId) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    PriceLogReadDTO wrapDTO(PriceLog priceLog) throws Exception;

    List<PriceLogReadDTO> wrapListDTO(Collection<PriceLog> priceLogCollection) throws Exception;

    Page<PriceLogReadDTO> wrapPageDTO(Page<PriceLog> priceLogPage) throws Exception;
}
