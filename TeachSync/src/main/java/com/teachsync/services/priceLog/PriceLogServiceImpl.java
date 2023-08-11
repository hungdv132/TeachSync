package com.teachsync.services.priceLog;

import com.teachsync.dtos.priceLog.PriceLogCreateDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.priceLog.PriceLogUpdateDTO;
import com.teachsync.entities.PriceLog;
import com.teachsync.repositories.PriceLogRepository;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.Status;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PriceLogServiceImpl implements PriceLogService {
    @Autowired
    private PriceLogRepository priceLogRepository;
    
    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private ModelMapper mapper;
    
    
    
    /* =================================================== CREATE =================================================== */
    @Override
    public PriceLog createPriceLog(PriceLog price) throws Exception {
        /* Validate input */
        if (Objects.nonNull(price.getValidTo())) {
            if (price.getValidFrom().isAfter(price.getValidTo())) {
                throw new IllegalArgumentException(
                        "Invalid datetime: validTo cannot be before validFrom");
            }
        }

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

        /* Save to DB */
        price = priceLogRepository.save(price);

        return price;
    }
    @Override
    public PriceLogReadDTO createPriceLogByDTO(PriceLogCreateDTO createDTO) throws Exception {
        PriceLog price = mapper.map(createDTO, PriceLog.class);

        price = createPriceLog(price);

        return wrapDTO(price);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public Page<PriceLog> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging(); }

        Page<PriceLog> priceLogPage =
                priceLogRepository.findAllByStatusNot(Status.DELETED, paging);

        if (priceLogPage.isEmpty()) {
            return null; }

        return priceLogPage;
    }
    @Override
    public Page<PriceLogReadDTO> getPageDTOAll(Pageable paging) throws Exception {
        Page<PriceLog> priceLogPage = getPageAll(paging);

        if (priceLogPage == null) {
            return null; }

        return wrapPageDTO(priceLogPage);
    }

    @Override
    public Page<PriceLog> getPageAllLatestPromotion(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging(); }

        Page<PriceLog> priceLogPage =
                priceLogRepository.findAllByValidBetweenAndIsPromotionTrueAndStatusNot(
                        LocalDateTime.now(), Status.DELETED, paging);

        if (priceLogPage.isEmpty()) {
            return null; }

        return priceLogPage;
    }
    @Override
    public Page<PriceLogReadDTO> getPageAllLatestPromotionDTO(Pageable paging) throws Exception {
        Page<PriceLog> priceLogPage = getPageAllLatestPromotion(paging);

        if (priceLogPage == null) {
            return null; }

        return wrapPageDTO(priceLogPage);
    }

    /* id */
    @Override
    public PriceLog getById(Long id) throws Exception {
        Optional<PriceLog> priceLog =
                priceLogRepository.findByIdAndStatusNot(id, Status.DELETED);

        return priceLog.orElse(null);
    }
    @Override
    public PriceLogReadDTO getDTOById(Long id) throws Exception {
        PriceLog priceLog = getById(id);

        if (priceLog == null) {
            return null; }

        return wrapDTO(priceLog);
    }

    /* courseId */
    @Override
    public PriceLog getLatestByCourseId(Long courseId) throws Exception {
        Optional<PriceLog> priceLog =
                priceLogRepository.findByCourseIdAndValidBetweenAndStatusNot(
                        courseId, LocalDateTime.now(), Status.DELETED);

        return priceLog.orElse(null);
    }
    @Override
    public PriceLogReadDTO getLatestDTOByCourseId(Long CourseId) throws Exception {
        PriceLog priceLog = getLatestByCourseId(CourseId);

        if (priceLog == null) {
            return null; }

        return wrapDTO(priceLog);
    }

    @Override
    public List<PriceLog> getAllCurrentByCourseIdIn(Collection<Long> courseIdCollection) throws Exception {
        List<PriceLog> priceLogList =
                priceLogRepository.findAllByCourseIdInAndValidBetweenAndStatusNot(
                        courseIdCollection, LocalDateTime.now(), Status.DELETED);

        if (priceLogList.isEmpty()) {
            return null; }

        return priceLogList;
    }
    @Override
    public List<PriceLogReadDTO> getAllCurrentDTOByCourseIdIn(Collection<Long> courseIdCollection) throws Exception {
        List<PriceLog> priceLogList = getAllCurrentByCourseIdIn(courseIdCollection);

        if (priceLogList == null) {
            return null; }

        return wrapListDTO(priceLogList);
    }
    @Override
    public Map<Long, PriceLogReadDTO> mapCourseIdCurrentPriceDTOByCourseIdIn(Collection<Long> courseIdCollection) throws Exception {
        List<PriceLogReadDTO> priceLodDTOList = getAllCurrentDTOByCourseIdIn(courseIdCollection);

        if (priceLodDTOList == null) {
            return new HashMap<>(); }

        return priceLodDTOList.stream()
                .collect(Collectors.toMap(PriceLogReadDTO::getCourseId, Function.identity()));
    }

    @Override
    public List<PriceLog> getAllByCourseId(Long courseId) throws Exception {
        List<PriceLog> priceLogList =
                priceLogRepository.findAllByCourseIdAndStatusNot(courseId ,Status.DELETED);

        if (priceLogList.isEmpty()) {
            return null; }

        return priceLogList;
    }
    @Override
    public List<PriceLogReadDTO> getAllDTOByCourseId(Long courseId) throws Exception {
        List<PriceLog> priceLogList = getAllByCourseId(courseId);

        if (priceLogList == null) {
            return null; }

        return wrapListDTO(priceLogList);
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public PriceLog updatePriceLog(PriceLog price) throws Exception {
        /* Check exist */
        PriceLog oldPrice = getById(price.getId());
        if (Objects.isNull(oldPrice)) {
            throw new IllegalArgumentException(
                    "No Price found with id: " + price.getId());
        }
        price.setCreatedAt(oldPrice.getCreatedAt());
        price.setCreatedBy(oldPrice.getCreatedBy());

        /* Validate input */
        if (Objects.nonNull(price.getValidTo())) {
            if (price.getValidFrom().isAfter(price.getValidTo())) {
                throw new IllegalArgumentException(
                        "Invalid datetime: validTo cannot be before validFrom");
            }
        }

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

        /* Save to DB */
        price = priceLogRepository.save(price);

        return price;
    }

    @Override
    public PriceLogReadDTO updatePriceLogByDTO(PriceLogUpdateDTO updateDTO) throws Exception {
        PriceLog price = mapper.map(updateDTO, PriceLog.class);

        price = updatePriceLog(price);

        return wrapDTO(price);
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public PriceLogReadDTO wrapDTO(PriceLog priceLog) throws Exception {
        mapper.typeMap(PriceLog.class, PriceLogReadDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(PriceLogReadDTO::setPrice);
                    mapping.skip(PriceLogReadDTO::setPromotionAmount);
                });

        PriceLogReadDTO dto = mapper.map(priceLog, PriceLogReadDTO.class);

        DecimalFormat moneyFormat = new DecimalFormat("#,###");
        dto.setPrice(moneyFormat.format(priceLog.getPrice()));

        Double finalPrice = priceLog.getPrice();

        if (priceLog.getIsPromotion()) {
            switch (priceLog.getPromotionType()) {
                case AMOUNT -> {
                    finalPrice = priceLog.getPrice() - priceLog.getPromotionAmount();
                    dto.setPromotionAmount(moneyFormat.format(priceLog.getPromotionAmount()));
                }
                case PERCENT -> {
                    finalPrice = priceLog.getPrice() * ((100.0 - priceLog.getPromotionAmount()) / 100.0);
                    long roundedFinalPrice = Math.round(finalPrice); /* Bỏ số lẻ */
                    finalPrice = ((roundedFinalPrice + 99) / 100) * 100.0; /* Nâng lên hàng trăm */

                    DecimalFormat percentFormat = new DecimalFormat("#,###.##");
                    dto.setPromotionAmount(percentFormat.format(priceLog.getPromotionAmount()));
                }
            }
        }

        dto.setFinalPrice(moneyFormat.format(finalPrice));
        
        return dto;
    }

    @Override
    public List<PriceLogReadDTO> wrapListDTO(Collection<PriceLog> priceLogCollection) throws Exception {
        mapper.typeMap(PriceLog.class, PriceLogReadDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(PriceLogReadDTO::setPrice);
                    mapping.skip(PriceLogReadDTO::setPromotionAmount);
                });
        DecimalFormat moneyFormat = new DecimalFormat("#,###");
        DecimalFormat percentFormat = new DecimalFormat("#,###.##");

        List<PriceLogReadDTO> dtoList = new ArrayList<>();

        PriceLogReadDTO dto;

        Double finalPrice;
        for (PriceLog priceLog : priceLogCollection) {
            dto = mapper.map(priceLog, PriceLogReadDTO.class);

            finalPrice = priceLog.getPrice();

            if (priceLog.getIsPromotion()) {
                switch (priceLog.getPromotionType()) {
                    case AMOUNT -> {
                        finalPrice = priceLog.getPrice() - priceLog.getPromotionAmount();
                        dto.setPromotionAmount(moneyFormat.format(priceLog.getPromotionAmount()));
                    }
                    case PERCENT -> {
                        finalPrice = priceLog.getPrice() * ((100.0 - priceLog.getPromotionAmount()) / 100.0);
                        long roundedFinalPrice = Math.round(finalPrice); /* Bỏ số lẻ */
                        finalPrice = ((roundedFinalPrice + 99) / 100) * 100.0; /* Nâng lên hàng trăm */

                        dto.setPromotionAmount(percentFormat.format(priceLog.getPromotionAmount()));
                    }
                }
            }

            dto.setFinalPrice(moneyFormat.format(finalPrice));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<PriceLogReadDTO> wrapPageDTO(Page<PriceLog> priceLogPage) throws Exception {
        return new PageImpl<>(
                wrapListDTO(priceLogPage.getContent()),
                priceLogPage.getPageable(),
                priceLogPage.getTotalPages());
    }
}
