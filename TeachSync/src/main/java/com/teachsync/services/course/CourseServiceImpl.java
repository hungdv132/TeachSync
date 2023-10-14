package com.teachsync.services.course;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.course.CourseCreateDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.course.CourseUpdateDTO;
import com.teachsync.dtos.material.MaterialReadDTO;
import com.teachsync.dtos.priceLog.PriceLogCreateDTO;
import com.teachsync.dtos.priceLog.PriceLogReadDTO;
import com.teachsync.dtos.priceLog.PriceLogUpdateDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.CourseRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.material.MaterialService;
import com.teachsync.services.priceLog.PriceLogService;
import com.teachsync.services.test.TestService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.Status.*;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MaterialService materialService;
    @Autowired
    private PriceLogService priceLogService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private TestService testService;

    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public Course createCourse(
            Course course) throws Exception {
        StringBuilder errorMsg = new StringBuilder();
        /* Validate input */
        /* courseAlias */
        errorMsg.append(
                miscUtil.validateString(
                        "Mã khóa học", course.getCourseAlias(), 1, 10,
                        List.of("required", "minLength", "maxLength", "onlyBlank",
                                "startBlank", "endBlank", "specialChar")));
        /* courseName */
        errorMsg.append(
                miscUtil.validateString(
                        "Tên khóa học", course.getCourseName(), 1, 45,
                        List.of("required", "minLength", "maxLength", "onlyBlank",
                                "startBlank", "endBlank", "specialChar")));
        /* courseDesc */
        errorMsg.append(
                miscUtil.validateString(
                        "Miêu tả khóa học", course.getCourseDesc(), 1, 9999,
                        List.of("nullOrMinLength", "maxLength", "onlyBlank", "startBlank",
                                "endBlank", "specialChar")));
        /* courseImg */
        /* TODO: check valid link */
        /* numSession */
        errorMsg.append(
                miscUtil.validateNumber(
                        "Số tiết học", course.getNumSession().doubleValue(), 1.0, 100.0, 1.0,
                        List.of("min", "max", "step")));
        /* minScore */
        errorMsg.append(
                miscUtil.validateNumber(
                        "Điểm tối thiểu", course.getMinScore(), 0.0, 10.0, 0.01,
                        List.of("min", "max", "step")));
        /* minAttendant */
        errorMsg.append(
                miscUtil.validateNumber(
                        "Điểm danh tối thiểu", course.getMinAttendant(), 0.0, 100.0, 0.01,
                        List.of("min", "max", "step")));

        /* Check FK */
        /* No FK */

        /* Check duplicate */
        if (courseRepository
                .existsByCourseAliasAndStatusNotIn(
                        course.getCourseAlias(),
                        List.of(Status.DELETED))) {
            errorMsg.append("Đã tồn tại Khóa Học khác với Mã: ").append(course.getCourseAlias());
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi tạo Khóa Học: \n" + errorMsg.toString());
        }

        /* Save to DB */
        return courseRepository.save(course);
    }
    @Override
    public CourseReadDTO createCourseByDTO(
            CourseCreateDTO createDTO) throws Exception {
        Course course = mapper.map(createDTO, Course.class);

        course = createCourse(course);

        /* Create dependency */
        try {
            PriceLogCreateDTO priceCreateDTO = createDTO.getPrice();

            priceCreateDTO.setCourseId(course.getId());

            if (priceCreateDTO.getIsPromotion() && priceCreateDTO.getValidTo() != null) {
                /* Regular price after promotion end */
                    PriceLogCreateDTO priceCreateDTOAfterPromotion =
                            new PriceLogCreateDTO(
                                    course.getId(), priceCreateDTO.getPrice(), false,
                                    null, null, null,
                                    priceCreateDTO.getValidTo(), null);

                priceLogService.createPriceLogByDTO(priceCreateDTO);

                priceLogService.createPriceLogByDTO(priceCreateDTOAfterPromotion);
            } else {
                priceLogService.createPriceLogByDTO(priceCreateDTO);
            }
        } catch (IllegalArgumentException iAE) {
            /* Revert create */
            deleteCourse(course.getId());

            throw new IllegalArgumentException(
                    "Lỗi tạo Khóa Học. Bắt nguồn từ: \n"+iAE.getMessage());
        }

        return wrapDTO(course, null);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public List<Course> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList;
        
        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            courseList =
                    courseRepository.findAllByStatusIn(
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }
            
            courseList = 
                    courseRepository.findAllByStatusNotIn(
                            statuses);
        }

        if (ObjectUtils.isEmpty(courseList)) { return null; }

        return courseList;
    }
    @Override
    public List<CourseReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        List<Course> courseList = getAll(statuses, isStatusIn);

        return wrapListDTO(courseList, options);
    }
    @Override
    public Map<Long, CourseReadDTO> mapIdDTO(
            Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        List<CourseReadDTO> courseDTOList = getAllDTO(statuses, isStatusIn, options);

        if (ObjectUtils.isEmpty(courseDTOList)) { return new HashMap<>(); }

        return courseDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    @Override
    public Page<Course> getPageAll(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Course> coursePage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            coursePage =
                    courseRepository.findAllByStatusIn(
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            coursePage =
                    courseRepository.findAllByStatusNotIn(
                            statuses,
                            paging);
        }

        if (ObjectUtils.isEmpty(coursePage)) { return null; }

        return coursePage;
    }
    @Override
    public Page<CourseReadDTO> getPageAllDTO(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        Page<Course> coursePage = getPageAll(paging, statuses, isStatusIn);

        return wrapPageDTO(coursePage, options);
    }

    @Override
    public Page<CourseReadDTO> getPageAllDTOOnSale(
            Pageable paging, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception {

        /* TODO: filter price */

        Page<Course> coursePage = getPageAll(paging, statuses, isStatusIn);

        return wrapPageDTO(coursePage, options);
    }

    /* id */
    @Override
    public Boolean existsById(
            Long id) throws Exception {

        return courseRepository
                .existsByIdAndStatusNotIn(id, List.of(DELETED));
    }
    @Override
    public Boolean existsAllByIdIn(
            Collection<Long> ids) throws Exception {

        return courseRepository
                .existsAllByIdInAndStatusNotIn(ids, List.of(DELETED));
    }

    @Override
    public Course getById(
            Long id, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            return courseRepository
                    .findByIdAndStatusIn(id, statuses)
                    .orElse(null);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            return courseRepository
                    .findByIdAndStatusNotIn(id, statuses)
                    .orElse(null);
        }
    }
    @Override
    public CourseReadDTO getDTOById(
            Long id, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        Course course = getById(id, statuses, isStatusIn);

        return wrapDTO(course, options);
    }
    
    @Override
    public List<Course> getAllByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            courseList =
                    courseRepository.findAllByIdInAndStatusIn(
                            ids,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            courseList =
                    courseRepository.findAllByIdInAndStatusNotIn(
                            ids,
                            statuses);
        }

        if (ObjectUtils.isEmpty(courseList)) { return null; }

        return courseList;
    }
    @Override
    public Map<Long, String> mapIdCourseAliasByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList = getAllByIdIn(ids, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(courseList)) { return new HashMap<>(); }

        return courseList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Course::getCourseAlias));
    }
    @Override
    public Map<Long, String> mapIdCourseNameByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList = getAllByIdIn(ids, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(courseList)) { return new HashMap<>(); }

        return courseList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Course::getCourseName));
    }
    @Override
    public List<CourseReadDTO> getAllDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        List<Course> courseList = getAllByIdIn(ids, statuses, isStatusIn);

        return wrapListDTO(courseList, options);
    }
    @Override
    public Map<Long, CourseReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        List<CourseReadDTO> courseDTOList = getAllDTOByIdIn(ids, statuses, isStatusIn, options);

        if (ObjectUtils.isEmpty(courseDTOList)) { return new HashMap<>(); }

        return courseDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    @Override
    public Page<Course> getPageAllByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Course> coursePage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            coursePage =
                    courseRepository.findAllByIdInAndStatusIn(
                            ids,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            coursePage =
                    courseRepository.findAllByIdInAndStatusNotIn(
                            ids,
                            statuses,
                            paging);
        }

        if (ObjectUtils.isEmpty(coursePage)) { return null; }

        return coursePage;
    }
    @Override
    public Page<CourseReadDTO> getPageAllDTOByIdIn(
            Pageable paging, Collection<Long> ids, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        Page<Course> coursePage = getPageAllByIdIn(paging, ids, statuses, isStatusIn);

        return wrapPageDTO(coursePage, options);
    }
    
    /* courseAlias */
    @Override
    public List<Course> getAllByAliasContains(
            String courseAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            courseList =
                    courseRepository.findAllByCourseAliasContainsAndStatusIn(
                            courseAlias,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            courseList =
                    courseRepository.findAllByCourseAliasContainsAndStatusNotIn(
                            courseAlias,
                            statuses);
        }

        if (ObjectUtils.isEmpty(courseList)) { return null; }

        return courseList;
    }
    @Override
    public List<CourseReadDTO> getAllDTOByAliasContains(
            String courseAlias, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        List<Course> courseList = getAllByAliasContains(courseAlias, statuses, isStatusIn);

        return wrapListDTO(courseList, options);
    }

    @Override
    public Page<Course> getPageAllByAliasContains(
            Pageable paging, String courseAlias, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Course> coursePage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            coursePage =
                    courseRepository.findAllByCourseAliasContainsAndStatusNotIn(
                            courseAlias,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            coursePage =
                    courseRepository.findAllByCourseAliasContainsAndStatusNotIn(
                            courseAlias,
                            statuses,
                            paging);
        }
        if (ObjectUtils.isEmpty(coursePage)) { return null; }

        return coursePage;
    }
    @Override
    public Page<CourseReadDTO> getPageAllDTOByAliasContains(
            Pageable paging, String courseAlias, Collection<Status> statuses, boolean isStatusIn, 
            Collection<DtoOption> options) throws Exception {

        Page<Course> coursePage = getPageAllByAliasContains(paging, courseAlias, statuses, isStatusIn);

        return wrapPageDTO(coursePage, options);
    }

    /* courseName */
    @Override
    public List<Course> getAllByNameContains(
            String courseName, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Course> courseList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            courseList =
                    courseRepository.findAllByCourseNameContainsAndStatusIn(
                            courseName,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            courseList =
                    courseRepository.findAllByCourseNameContainsAndStatusNotIn(
                            courseName,
                            statuses);
        }

        if (ObjectUtils.isEmpty(courseList)) { return null; }

        return courseList;
    }
    @Override
    public List<CourseReadDTO> getAllDTOByNameContains(
            String courseName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception {

        List<Course> courseList = getAllByNameContains(courseName, statuses, isStatusIn);

        return wrapListDTO(courseList, options);
    }

    @Override
    public Page<Course> getPageAllByNameContains(
            Pageable paging, String courseName, Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Course> coursePage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            coursePage =
                    courseRepository.findAllByCourseNameContainsAndStatusNotIn(
                            courseName,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            coursePage =
                    courseRepository.findAllByCourseNameContainsAndStatusNotIn(
                            courseName,
                            statuses,
                            paging);
        }
        if (ObjectUtils.isEmpty(coursePage)) { return null; }

        return coursePage;
    }
    @Override
    public Page<CourseReadDTO> getPageAllDTOByNameContains(
            Pageable paging, String courseName, Collection<Status> statuses, boolean isStatusIn,
            Collection<DtoOption> options) throws Exception {

        Page<Course> coursePage = getPageAllByNameContains(paging, courseName, statuses, isStatusIn);

        return wrapPageDTO(coursePage, options);
    }

    /* =================================================== UPDATE =================================================== */
    @Override
    public Course updateCourse(
            Course course) throws Exception {
        /* Check exists */
        Course oldCourse = getById(course.getId(), List.of(DELETED), false);
        if (Objects.isNull(oldCourse)) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Khóa Học. Không tìm thấy Khóa Học nào với id: " + course.getId());
        }
        course.setCreatedAt(oldCourse.getCreatedAt());
        course.setCreatedBy(oldCourse.getCreatedBy());

        StringBuilder errorMsg = new StringBuilder();

        /* Validate input */
        /* status */
        Status oldStatus = oldCourse.getStatus();
        Status newStatus = course.getStatus();
        if (!oldStatus.equals(newStatus)) {
            /* Change status => no change other attribute */
            switch (oldStatus) {
                case DESIGNING -> {
                    if (!AWAIT_REVIEW.equals(newStatus)) {
                        errorMsg.append("Khóa Học với trạng thái 'Đang thiết kế' chỉ được phép chuyển trạng thái thành 'Đang chờ xét duyệt'.");
                    }
                }
                case AWAIT_REVIEW -> {
                    if (!List.of(DESIGNING, OPENED).contains(newStatus)) {
                        errorMsg.append("Khóa Học với trạng thái 'Đang thiết kế' chỉ được phép chuyển trạng thái thành 'Đang thiết kế' hoặc 'Đang mở'.");
                    }
                }
                case OPENED -> {
                    if (!CLOSED.equals(newStatus)) {
                        errorMsg.append("Khóa Học với trạng thái 'Đang mở' chỉ được phép chuyển trạng thái thành 'Đã đóng'.");
                    }
                }
                case CLOSED -> {
                    if (!OPENED.equals(newStatus)) {
                        errorMsg.append("Khóa Học với trạng thái 'Đã đóng' chỉ được phép chuyển trạng thái thành 'Đang mở'.");
                    }
                }
            }
            /* Override old data */
            oldCourse.setStatus(newStatus);
            oldCourse.setUpdatedBy(course.getUpdatedBy());
            oldCourse.setUpdatedAt(course.getUpdatedAt());

            /* Copy from old data */
            course = oldCourse;

            /* No change of attribute => no need validate input, no need check FK */
        } else {
            /* No change status => change other attribute */
            switch (oldStatus) {
                case DESIGNING -> {
                    /* Validate input */
                    /* courseAlias */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Mã khóa học", course.getCourseAlias(), 1, 10,
                                    List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
                    /* courseName */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Tên khóa học", course.getCourseName(), 1, 45,
                                    List.of("required", "minLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
                    /* courseDesc */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Miêu tả khóa học", course.getCourseDesc(), 1, 9999,
                                    List.of("nullOrMinLength", "maxLength", "onlyBlank", "startBlank", "endBlank", "specialChar")));
                    /* courseImg */
                    /* TODO: check valid link */
                    /* numSession */
                    errorMsg.append(
                            miscUtil.validateNumber(
                                    "Số tiết học", course.getNumSession().doubleValue(), 1.0, 100.0, 1.0,
                                    List.of("min", "max", "step")));
                    /* minScore */
                    errorMsg.append(
                            miscUtil.validateNumber(
                                    "Điểm tối thiểu", course.getMinScore(), 0.0, 10.0, 0.01,
                                    List.of("min", "max", "step")));
                    /* minAttendant */
                    errorMsg.append(
                            miscUtil.validateNumber(
                                    "Điểm danh tối thiểu", course.getMinAttendant(), 0.0, 100.0, 0.01,
                                    List.of("min", "max", "step")));

                    /* Check FK */
                    /* No FK */

                    /* Check duplicate */
                    if (courseRepository
                            .existsByIdNotAndCourseAliasAndStatusNotIn(
                                    course.getId(),
                                    course.getCourseAlias(),
                                    List.of(Status.DELETED))) {
                        errorMsg.append("Đã tồn tại Khóa Học khác với Mã: ").append(course.getCourseAlias());
                    }
                }
                case AWAIT_REVIEW -> {
                    /* Await review don't allow change of attribute => Cancel update. */
                    return oldCourse;
                }
                case OPENED, CLOSED -> {
                    /* Override old data */
                    oldCourse.setCourseImg(course.getCourseImg());

                    oldCourse.setUpdatedBy(course.getUpdatedBy());
                    oldCourse.setUpdatedAt(course.getUpdatedAt());

                    /* Copy from old data */
                    course = oldCourse;

                    /* Validate input */
                    /* courseImg */
                    /* TODO: check valid link */
                }
            }
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Khóa Học: \n" + errorMsg.toString());
        }

        /* Save to DB */
        return courseRepository.save(course);
    }
    @Override
    public CourseReadDTO updateCourseByDTO(
            CourseUpdateDTO updateDTO) throws Exception {
        /* Check exists */
        Course oldCourse = getById(updateDTO.getId(), List.of(DELETED), false);
        if (Objects.isNull(oldCourse)) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Khóa Học. Không tìm thấy Khóa Học nào với id: " + updateDTO.getId());
        }

        Course course = mapper.map(updateDTO, Course.class);

        Status oldStatus = oldCourse.getStatus();
        Status newStatus = course.getStatus();

        course = updateCourse(course);

        if (oldStatus.equals(newStatus)) {
            /* If status not change => change price */
            try {
                PriceLogCreateDTO newPriceLog = updateDTO.getPrice();

                /* Update dependency */
                /* Close old price */
                PriceLogReadDTO oldPriceLog = priceLogService.getCurrentDTOByCourseId(course.getId());
                oldPriceLog.setValidTo(newPriceLog.getValidFrom());
                priceLogService.updatePriceLogByDTO(
                        mapper.map(oldPriceLog, PriceLogUpdateDTO.class));

                /* Create dependency */
                newPriceLog.setCourseId(course.getId());

                if (newPriceLog.getIsPromotion() && newPriceLog.getValidTo() != null) {
                    /* Regular price after promotion end */
                    PriceLogCreateDTO priceCreateDTOAfterPromotion =
                            new PriceLogCreateDTO(
                                    course.getId(), newPriceLog.getPrice(), false,
                                    null, null, null,
                                    newPriceLog.getValidTo(), null);

                    priceLogService.createPriceLogByDTO(newPriceLog);

                    priceLogService.createPriceLogByDTO(priceCreateDTOAfterPromotion);
                } else {
                    priceLogService.createPriceLogByDTO(newPriceLog);
                }

            } catch (IllegalArgumentException iAE) {
                /* Revert update */
                course = updateCourse(oldCourse);
            }
        }

        return wrapDTO(course, null);
    }


    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteCourse(Long id) throws Exception {
        Course course = getById(id, List.of(DELETED), false);

        if (course == null) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Khóa Học. Không tìm thấy Khóa Học nào với id: " + id);
        }

        if (!course.getStatus().equals(Status.DESIGNING)) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Khóa Học. Chỉ có những Khóa Học với trạng thái 'Đang thiết kế' mới được phép xóa.");
        }

        Status oldStatus = course.getStatus();

        /* Delete */
        course.setStatus(DELETED);

        /* Save to DB */
        courseRepository.saveAndFlush(course);

        try {
            priceLogService.deleteAllByCourseId(id);
        } catch (IllegalArgumentException iAE) {
            /* Revert delete */
            course.setStatus(oldStatus);

            courseRepository.saveAndFlush(course);

            throw new IllegalArgumentException(
                    "Lỗi xóa Khóa Học. Bắt nguồn từ: \n" + iAE.getMessage());
        }

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CourseReadDTO wrapDTO(
            Course course, 
            Collection<DtoOption> options) throws Exception {
        if (course == null) {
            return null;
        }

        CourseReadDTO dto = mapper.map(course, CourseReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.MATERIAL_LIST)) {
                List<MaterialReadDTO> materialList =
                        materialService.getAllDTOByCourseId(course.getId(), options);
                dto.setMaterialList(materialList);
            }

            if (options.contains(DtoOption.TEST_LIST)) {
//                dto.setTestList();
            }

            if (options.contains(DtoOption.CLAZZ_LIST_OPENED)) {
                List<ClazzReadDTO> clazzDTOList =
                        clazzService.getAllDTOByCourseId(
                                course.getId(), List.of(OPENED), true, options);

                dto.setClazzList(clazzDTOList);
            }

            if (options.contains(DtoOption.CLAZZ_LIST_CURRENT)) {
                List<ClazzReadDTO> clazzDTOList =
                        clazzService.getAllDTOByCourseId(
                                course.getId(), List.of(ONGOING, OPENED), true, options);

                dto.setClazzList(clazzDTOList);
            }

            if (options.contains(DtoOption.CLAZZ_LIST_ALL)) {
                List<ClazzReadDTO> clazzDTOList =
                        clazzService.getAllDTOByCourseId(
                                course.getId(), List.of(DELETED), false, options);

                dto.setClazzList(clazzDTOList);
            }

            if (options.contains(DtoOption.CURRENT_PRICE)) {
                PriceLogReadDTO priceLogDTO = priceLogService.getCurrentDTOByCourseId(course.getId());

                dto.setCurrentPrice(priceLogDTO);
            }
        }

        return dto;
    }
    @Override
    public List<CourseReadDTO> wrapListDTO(
            Collection<Course> courseCollection, 
            Collection<DtoOption> options) throws Exception {
        if (ObjectUtils.isEmpty(courseCollection)) {
            return null;
        }

        List<CourseReadDTO> dtoList = new ArrayList<>();

        CourseReadDTO dto;

//        Map<Long, List<ClazzReadDTO>> courseIdClazzDTOListMap = new HashMap<>();
        Map<Long, List<MaterialReadDTO>> courseIdMaterialDTOListMap = new HashMap<>();
        Map<Long, List<TestReadDTO>> courseIdTestDTOListMap = new HashMap<>();
        Map<Long, PriceLogReadDTO> courseIdLatestPriceLogMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> courseIdSet = new HashSet<>();

            for (Course course : courseCollection) {
                courseIdSet.add(course.getId());
            }

            if (options.contains(DtoOption.MATERIAL_LIST)) {
                courseIdMaterialDTOListMap =
                        materialService.mapCourseIdListDTOByCourseIdIn(courseIdSet, options);
            }

            if (options.contains(DtoOption.TEST_LIST)) {
                courseIdTestDTOListMap =
                        testService.mapCourseIdListDTOByCourseIdIn(courseIdSet, options);
            }

            if (options.contains(DtoOption.CURRENT_PRICE)) {
                courseIdLatestPriceLogMap =
                        priceLogService.mapCourseIdCurrentPriceDTOByCourseIdIn(courseIdSet);
            }
        }

        for (Course course : courseCollection) {
            dto = mapper.map(course, CourseReadDTO.class);

            /* Add Dependency */
//            dto.setClazzList();
            dto.setMaterialList(courseIdMaterialDTOListMap.get(course.getId()));

            dto.setTestList(courseIdTestDTOListMap.get(course.getId()));

            dto.setCurrentPrice(courseIdLatestPriceLogMap.get(course.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<CourseReadDTO> wrapPageDTO(
            Page<Course> coursePage, 
            Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(coursePage.getContent(), options),
                coursePage.getPageable(),
                coursePage.getTotalPages());
    }
}