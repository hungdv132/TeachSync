package com.teachsync.services.clazz;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Center;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.Course;
import com.teachsync.repositories.ClazzRepository;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.clazzTest.ClazzTestService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzRepository clazzRepository;

//    @Autowired
//    private CourseSemesterService courseSemesterService;
    @Lazy
    @Autowired
    private CourseService courseService;
    @Autowired
    private CenterService centerService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ClazzScheduleService clazzScheduleService;
    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private ClazzTestService clazzTestService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;

    private Logger logger = LoggerFactory.getLogger(ClazzServiceImpl.class);



    /* =================================================== CREATE =================================================== */
    @Override
    public Clazz createClazz(
            Clazz clazz) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        /* Validate input */
        /* alias */
        errorMsg.append(
                miscUtil.validateString(
                        "Mã lóp học", clazz.getClazzAlias(), 1, 10,
                        List.of("required", "minLength", "maxLength",
                                "onlyBlank", "startBlank", "endBlank", "specialChar")));
        /* name */
        errorMsg.append(
                miscUtil.validateString(
                        "Tên lóp học", clazz.getClazzName(), 1, 45,
                        List.of("required", "minLength", "maxLength",
                                "onlyBlank", "startBlank", "endBlank", "specialChar")));
        /* clazzDesc */
        errorMsg.append(
                miscUtil.validateString(
                        "Miêu tả lóp học", clazz.getClazzDesc(), 1, 9999,
                        List.of("nullOrMinLength", "maxLength", "onlyBlank",
                                "startBlank", "endBlank", "specialChar")));
        /* minCapacity */
        errorMsg.append(
                miscUtil.validateNumber(
                        "Số học sinh tối thiểu", clazz.getMinCapacity().doubleValue(),
                        0.0, clazz.getMaxCapacity().doubleValue(), 1.0,
                        List.of("min", "max", "step")));
        /* maxCapacity */
        errorMsg.append(
                miscUtil.validateNumber(
                        "Số học sinh tối đa", clazz.getMaxCapacity().doubleValue(),
                        0.0, null, 1.0,
                        List.of("min", "step")));

        /* Check FK */
        /* courseId */
        if (!courseService.existsById(clazz.getCourseId())) {
            errorMsg.append("Không tìm thấy Khóa Học nào với id: ").append(clazz.getCourseId());
        }
        /* centerId */
        if (!centerService.existsById(clazz.getCenterId())) {
            errorMsg.append("Không tìm thấy Cơ Sở nào với id: ").append(clazz.getCenterId());
        }
        /* staffId */
        if (!staffService.existsById(clazz.getStaffId())) {
            errorMsg.append("Không tìm thấy Nhân Viên nào với id: ").append(clazz.getStaffId());
        }

        /* Check duplicate */
        if (clazzRepository
                .existsByClazzAliasAndStatusNotIn(
                        clazz.getClazzAlias(),
                        List.of(DELETED))) {
            errorMsg.append("Đã tồn tại Lớp Học khác với Mã: ").append(clazz.getClazzAlias());
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi tạo Lớp Học\n" + errorMsg.toString());
        }

        /* Create */
        return clazzRepository.saveAndFlush(clazz);
    }
    @Override
    public ClazzReadDTO createClazzByDTO(
            ClazzCreateDTO createDTO) throws Exception {
        Clazz clazz = mapper.map(createDTO, Clazz.class);

        clazz = createClazz(clazz);

        return wrapDTO(clazz, null);
    }

    /* =================================================== READ ===================================================== */
    @Override
    public List<Clazz> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository
                    .findAllByStatusIn(statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByStatusNotIn(
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }

    @Override
    public List<ClazzReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAll( statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    
    @Override
    public Page<Clazz> getPageAll(
            Pageable paging, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByStatusIn(
                            statuses, 
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByStatusNotIn(
                            statuses, 
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTO(
            Pageable paging, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAll(paging, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* id */
    @Override
    public Boolean existsById(
            Long id) throws Exception {

        return clazzRepository
                .existsByIdAndStatusNotIn(id, List.of(DELETED));
    }
    @Override
    public Boolean existsAllByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        return clazzRepository
                .existsAllByIdInAndStatusNotIn(ids, List.of(DELETED));
    }

    @Override
    public Clazz getById(
            Long id, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            return clazzRepository
                    .findByIdAndStatusIn(id, statuses)
                    .orElse(null);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            return clazzRepository
                    .findByIdAndStatusNotIn(id, statuses)
                    .orElse(null);
        }
    }
    @Override
    public ClazzReadDTO getDTOById(
            Long id, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Clazz clazz = getById(id, statuses, isStatusIn);

        return wrapDTO(clazz, options);
    }

    @Override
    public List<Clazz> getAllByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;
        
        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByIdInAndStatusIn(
                            ids, 
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByIdInAndStatusNotIn(
                            ids, 
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public Map<Long, Clazz> mapIdClazzByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        List<Clazz> clazzList = getAllByIdIn(ids, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(clazzList)) { return new HashMap<>(); }

        return clazzList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }
    @Override
    public Map<Long, String> mapIdClazzAliasByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        List<Clazz> clazzList = getAllByIdIn(ids, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(clazzList)) { return new HashMap<>(); }

        return clazzList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Clazz::getClazzAlias));
    }
    @Override
    public Map<Long, String> mapIdClazzNameByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        List<Clazz> clazzList = getAllByIdIn(ids, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(clazzList)) { return new HashMap<>(); }

        return clazzList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Clazz::getClazzName));
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        List<Clazz> clazzList = getAllByIdIn(ids, statuses, isStatusIn);
        
        return wrapListDTO(clazzList, options);
    }
    @Override
    public Map<Long, ClazzReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        List<ClazzReadDTO> clazzDTOList = getAllDTOByIdIn(ids, statuses, isStatusIn, options);

        if (ObjectUtils.isEmpty(clazzDTOList)) { return new HashMap<>(); }

        return clazzDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    @Override
    public Page<Clazz> getPageAllByIdIn(
            Pageable paging, Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByIdInAndStatusIn(
                            ids, 
                            statuses, 
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByIdInAndStatusNotIn(
                            ids, 
                            statuses, 
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }
        
        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByIdIn(
            Pageable paging, Collection<Long> ids, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        Page<Clazz> clazzPage = getPageAllByIdIn(paging, ids, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* courseId */
    @Override
    public List<Clazz> getAllByCourseId(
            Long courseId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByCourseIdAndStatusIn(
                            courseId, 
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByCourseIdAndStatusNotIn(
                            courseId, 
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public Map<Long, Clazz> mapIdClazzByCourseId(
            Long courseId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList = getAllByCourseId(courseId, statuses, isStatusIn);

        if (ObjectUtils.isEmpty(clazzList)) { return new HashMap<>(); }

        return clazzList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseId(
            Long courseId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCourseId(courseId, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    @Override
    public Map<Long, ClazzReadDTO> mapIdDTOByCourseId(
            Long courseId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<ClazzReadDTO> clazzDTOList = getAllDTOByCourseId(courseId, statuses, isStatusIn, options);

        if (ObjectUtils.isEmpty(clazzDTOList)) { return new HashMap<>(); }

        return clazzDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    @Override
    public Page<Clazz> getPageAllByCourseId(
            Pageable paging, Long courseId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByCourseIdAndStatusIn(
                            courseId, 
                            statuses, 
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByCourseIdAndStatusNotIn(
                            courseId, 
                            statuses, 
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseId(
            Pageable paging, Long courseId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        Page<Clazz> clazzPage = getPageAllByCourseId(paging, courseId, statuses, isStatusIn);
        
        return wrapPageDTO(clazzPage, options);
    }
    
    @Override
    public List<Clazz> getAllByCourseIdIn(
            Collection<Long> courseIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByCourseIdInAndStatusIn(
                            courseIds, 
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByCourseIdInAndStatusNotIn(
                            courseIds, 
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseIdIn(
            Collection<Long> courseIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        List<Clazz> clazzList = getAllByCourseIdIn(courseIds, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    
    @Override
    public Page<Clazz> getPageAllByCourseIdIn(
            Pageable paging, Collection<Long> courseIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        if (paging == null) {  paging = miscUtil.defaultPaging(); }
        
        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByCourseIdInAndStatusIn(
                            courseIds, 
                            statuses, 
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByCourseIdInAndStatusNotIn(
                            courseIds, 
                            statuses, 
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseIdIn(
            Pageable paging, Collection<Long> courseIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCourseIdIn(paging, courseIds, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* centerId */
    @Override
    public List<Clazz> getAllByCenterId(
            Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByCenterIdAndStatusIn(
                            centerId, 
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByCenterIdAndStatusNotIn(
                            centerId, 
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCenterId(
            Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCenterId(centerId, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByCenterId(
            Pageable paging, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByCenterIdAndStatusIn(
                            centerId,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByCenterIdAndStatusNotIn(
                            centerId,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCenterId(
            Pageable paging, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCenterId(paging, centerId, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    @Override
    public List<Clazz> getAllByCenterIdIn(
            Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByCenterIdInAndStatusIn(
                            centerIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByCenterIdInAndStatusNotIn(
                            centerIds,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCenterIdIn(
            Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCenterIdIn(centerIds, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    
    @Override
    public Page<Clazz> getPageAllByCenterIdIn(
            Pageable paging, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) {  paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByCenterIdInAndStatusIn(
                            centerIds,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByCenterIdInAndStatusNotIn(
                            centerIds,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCenterIdIn(
            Pageable paging, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCenterIdIn(paging, centerIds, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* courseId && centerId */
    @Override
    public List<Clazz> getAllByCourseIdAndCenterId(
            Long courseId, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByCourseIdAndCenterIdAndStatusIn(
                            courseId,
                            centerId,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByCourseIdAndCenterIdAndStatusNotIn(
                            courseId,
                            centerId,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseIdAndCenterId(
            Long courseId, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCourseIdAndCenterId(courseId, centerId, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    
    @Override
    public Page<Clazz> getPageAllByCourseIdAndCenterId(
            Pageable paging, Long courseId, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByCourseIdAndCenterIdAndStatusIn(
                            courseId,
                            centerId,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByCourseIdAndCenterIdAndStatusNotIn(
                            courseId,
                            centerId,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) {
            return null;
        }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseIdAndCenterId(
            Pageable paging, Long courseId, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCourseIdAndCenterId(paging, courseId, centerId, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    @Override
    public List<Clazz> getAllByCourseIdInAndCenterId(
            Collection<Long> courseIds, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList =
                    clazzRepository.findAllByCourseIdInAndCenterIdAndStatusIn(
                            courseIds,
                            centerId,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList =
                    clazzRepository.findAllByCourseIdInAndCenterIdAndStatusNotIn(
                            courseIds,
                            centerId,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseIdInAndCenterId(
            Collection<Long> courseIds, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCourseIdInAndCenterId(courseIds, centerId, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByCourseIdInAndCenterId(
            Pageable paging, Collection<Long> courseIds, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage =
                    clazzRepository.findAllByCourseIdInAndCenterIdAndStatusIn(
                            courseIds,
                            centerId,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage =
                    clazzRepository.findAllByCourseIdInAndCenterIdAndStatusNotIn(
                            courseIds,
                            centerId,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) {
            return null;
        }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseIdInAndCenterId(
            Pageable paging, Collection<Long> courseIds, Long centerId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCourseIdInAndCenterId(paging, courseIds, centerId, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    @Override
    public List<Clazz> getAllByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList =
                    clazzRepository.findAllByCourseIdAndCenterIdInAndStatusIn(
                            courseId,
                            centerIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList =
                    clazzRepository.findAllByCourseIdAndCenterIdInAndStatusNotIn(
                            courseId,
                            centerIds,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCourseIdAndCenterIdIn(courseId, centerIds, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    @Override
    public Map<Long, List<ClazzReadDTO>> mapCenterIdListDTOByCourseIdAndCenterIdIn(
            Long courseId, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<ClazzReadDTO> clazzDTOList =
                getAllDTOByCourseIdAndCenterIdIn(courseId, centerIds, statuses, isStatusIn, options);

        if (ObjectUtils.isEmpty(clazzDTOList)) { return new HashMap<>(); }

        Map<Long, List<ClazzReadDTO>> centerIdDTOListMap = new HashMap<>();
        Long tmpCenterId;
        List<ClazzReadDTO> tmpClazzDTOList;

        for (ClazzReadDTO clazzDTO : clazzDTOList) {
            tmpCenterId = clazzDTO.getCenterId();

            tmpClazzDTOList = centerIdDTOListMap.get(tmpCenterId);

            if (ObjectUtils.isEmpty(tmpClazzDTOList)) {
                centerIdDTOListMap.put(tmpCenterId, new ArrayList<>(List.of(clazzDTO)));

            } else {
                tmpClazzDTOList.add(clazzDTO);

                centerIdDTOListMap.put(tmpCenterId, tmpClazzDTOList);
            }
        }

        return centerIdDTOListMap;
    }

    @Override
    public Page<Clazz> getPageAllByCourseIdAndCenterIdIn(
            Pageable paging, Long courseId, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage =
                    clazzRepository.findAllByCourseIdAndCenterIdInAndStatusIn(
                            courseId,
                            centerIds,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage =
                    clazzRepository.findAllByCourseIdAndCenterIdInAndStatusNotIn(
                            courseId,
                            centerIds,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) {
            return null;
        }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseIdAndCenterIdIn(
            Pageable paging, Long courseId, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCourseIdAndCenterIdIn(paging, courseId, centerIds, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    @Override
    public List<Clazz> getAllByCourseIdInAndCenterIdIn(
            Collection<Long> courseIds, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList =
                    clazzRepository.findAllByCourseIdInAndCenterIdInAndStatusIn(
                            courseIds,
                            centerIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList =
                    clazzRepository.findAllByCourseIdInAndCenterIdInAndStatusNotIn(
                            courseIds,
                            centerIds,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseIdInAndCenterIdIn(
            Collection<Long> courseIds, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByCourseIdInAndCenterIdIn(courseIds, centerIds, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByCourseIdInAndCenterIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage =
                    clazzRepository.findAllByCourseIdInAndCenterIdInAndStatusIn(
                            courseIds,
                            centerIds,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage =
                    clazzRepository.findAllByCourseIdInAndCenterIdInAndStatusNotIn(
                            courseIds,
                            centerIds,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) {
            return null;
        }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByCourseIdInAndCenterIdIn(
            Pageable paging, Collection<Long> courseIds, Collection<Long> centerIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByCourseIdInAndCenterIdIn(paging, courseIds, centerIds, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* staffId */
    @Override
    public List<Clazz> getAllByStaffId(
            Long staffId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByStaffIdAndStatusIn(
                            staffId,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByStaffIdAndStatusNotIn(
                            staffId,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByStaffId(
            Long staffId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByStaffId(staffId, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByStaffId(
            Pageable paging, Long staffId, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByStaffIdAndStatusIn(
                            staffId,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByStaffIdAndStatusNotIn(
                            staffId,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByStaffId(
            Pageable paging, Long staffId, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByStaffId(paging, staffId, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    @Override
    public List<Clazz> getAllByStaffIdIn(
            Collection<Long> staffIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByStaffIdInAndStatusIn(
                            staffIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByStaffIdInAndStatusNotIn(
                            staffIds,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByStaffIdIn(
            Collection<Long> staffIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Clazz> clazzList = getAllByStaffIdIn(staffIds, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }
    
    @Override
    public Page<Clazz> getPageAllByStaffIdIn(
            Pageable paging, Collection<Long> staffIds, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) {  paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByStaffIdInAndStatusIn(
                            staffIds,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByStaffIdInAndStatusNotIn(
                            staffIds,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByStaffIdIn(
            Pageable paging, Collection<Long> staffIds, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByStaffIdIn(paging, staffIds, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }
    
    /* clazzAlias */
    @Override
    public List<Clazz> getAllByAliasContains(
            String clazzAlias, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByClazzAliasContainsAndStatusIn(
                            clazzAlias,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByClazzAliasContainsAndStatusNotIn(
                            clazzAlias,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByAliasContains(
            String clazzAlias, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        List<Clazz> clazzList = getAllByAliasContains(clazzAlias, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByAliasContains(
            Pageable paging, String clazzAlias, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByClazzAliasContainsAndStatusIn(
                            clazzAlias,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByClazzAliasContainsAndStatusNotIn(
                            clazzAlias,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByAliasContains(
            Pageable paging, String clazzAlias, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByAliasContains(paging, clazzAlias, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }
    
    /* clazzName */
    @Override
    public List<Clazz> getAllByNameContains(
            String clazzName, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Clazz> clazzList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzList = 
                    clazzRepository.findAllByClazzNameContainsAndStatusIn(
                            clazzName,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzList = 
                    clazzRepository.findAllByClazzNameContainsAndStatusNotIn(
                            clazzName,
                            statuses);
        }

        if (ObjectUtils.isEmpty(clazzList)) { return null; }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByNameContains(
            String clazzName, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        List<Clazz> clazzList = getAllByNameContains(clazzName, statuses, isStatusIn);

        return wrapListDTO(clazzList, options);
    }

    @Override
    public Page<Clazz> getPageAllByNameContains(
            Pageable paging, String clazzName, 
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (paging == null) { paging = miscUtil.defaultPaging(); }

        Page<Clazz> clazzPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            clazzPage = 
                    clazzRepository.findAllByClazzNameContainsAndStatusIn(
                            clazzName,
                            statuses,
                            paging);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            clazzPage = 
                    clazzRepository.findAllByClazzNameContainsAndStatusNotIn(
                            clazzName,
                            statuses,
                            paging);
        }

        if (clazzPage.isEmpty()) { return null; }

        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageAllDTOByNameContains(
            Pageable paging, String clazzName, 
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Clazz> clazzPage = getPageAllByNameContains(paging, clazzName, statuses, isStatusIn);

        return wrapPageDTO(clazzPage, options);
    }

    /* =================================================== UPDATE =================================================== */
    @Override
    public Clazz updateClazz(
            Clazz clazz) throws Exception {
        /* Check exist */
        Clazz oldClazz = getById(clazz.getId(), List.of(DELETED), false);
        if (oldClazz == null) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Lớp Học. Không tìm thấy Lớp Học nào với id: " + clazz.getId());
        }
        clazz.setCreatedBy(oldClazz.getCreatedBy());
        clazz.setCreatedAt(oldClazz.getCreatedAt());

        StringBuilder errorMsg = new StringBuilder();

        /* Validate input */
        /* status */
        Status oldStatus = oldClazz.getStatus();
        Status newStatus = clazz.getStatus();
        if (!oldStatus.equals(newStatus)) {
            /* Change status => no change other attribute */
            switch (oldStatus) {
                case DESIGNING -> {
                    if (!AWAIT_REVIEW.equals(newStatus)) {
                        errorMsg.append("Lớp Học với trạng thái 'Đang thiết kế' chỉ được phép chuyển trạng thái thành 'Đang chờ xét duyệt'.");
                    }
                }
                case AWAIT_REVIEW -> {
                    if (!List.of(DESIGNING, OPENED).contains(newStatus)) {
                        errorMsg.append("Lớp Học với trạng thái 'Đang thiết kế' chỉ được phép chuyển trạng thái thành 'Đang thiết kế' hoặc 'Đang mở'.");
                    }
                }
                case OPENED -> {
                    if (!CLOSED.equals(newStatus)) {
                        errorMsg.append("Lớp Học với trạng thái 'Đang mở' chỉ được phép chuyển trạng thái thành 'Đã đóng'.");
                    }
                }
                case ONGOING -> {
                    if (!CLOSED.equals(newStatus)) {
                        errorMsg.append("Lớp Học với trạng thái 'Đang tiến hành' chỉ được phép chuyển trạng thái thành 'Đã đóng'.");
                    }
                }
                case CLOSED -> {
                    if (!OPENED.equals(newStatus)) {
                        errorMsg.append("Lớp Học với trạng thái 'Đã đóng' chỉ được phép chuyển trạng thái thành 'Đang mở' hoặc 'Đang tiến hành'.");
                    }
                }
            }
            /* Override old data */
            oldClazz.setStatus(newStatus);
            oldClazz.setUpdatedBy(clazz.getUpdatedBy());
            oldClazz.setUpdatedAt(clazz.getUpdatedAt());

            /* Copy from old data */
            clazz = oldClazz;

            /* No change of attribute => no need validate input, no need check FK */
        } else {
            /* No change status => change other attribute */
            switch (oldStatus) {
                case DESIGNING -> {
                    /* Validate input */

                    /* alias */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Mã lóp học", clazz.getClazzAlias(), 1, 10,
                                    List.of("required", "minLength", "maxLength",
                                            "onlyBlank", "startBlank", "endBlank", "specialChar")));
                    /* name */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Tên lóp học", clazz.getClazzName(), 1, 45,
                                    List.of("required", "minLength", "maxLength",
                                            "onlyBlank", "startBlank", "endBlank", "specialChar")));
                    /* clazzDesc */
                    errorMsg.append(
                            miscUtil.validateString(
                                    "Miêu tả lóp học", clazz.getClazzDesc(), 1, 9999,
                                    List.of("nullOrMinLength", "maxLength", "onlyBlank",
                                            "startBlank", "endBlank", "specialChar")));
                    /* minCapacity */
                    errorMsg.append(
                            miscUtil.validateNumber(
                                    "Số học sinh tối thiểu", clazz.getMinCapacity().doubleValue(),
                                    0.0, clazz.getMaxCapacity().doubleValue(), 1.0,
                                    List.of("min", "max", "step")));
                    /* maxCapacity */
                    errorMsg.append(
                            miscUtil.validateNumber(
                                    "Số học sinh tối đa", clazz.getMaxCapacity().doubleValue(),
                                    0.0, null, 1.0,
                                    List.of("min", "step")));
                    
                    /* Check FK */
                    /* courseId */
                    if (!courseService.existsById(clazz.getCourseId())) {
                        errorMsg.append("Không tìm thấy Khóa Học nào với id: ").append(clazz.getCourseId());
                    }
                    /* centerId */
                    if (!centerService.existsById(clazz.getCenterId())) {
                        errorMsg.append("Không tìm thấy Cơ Sở nào với id: ").append(clazz.getCenterId());
                    }
                    /* staffId */
                    if (!staffService.existsById(clazz.getStaffId())) {
                        errorMsg.append("Không tìm thấy Nhân Viên nào với id: ").append(clazz.getStaffId());
                    }

                    /* Check duplicate */
                    if (clazzRepository
                            .existsByIdNotAndClazzAliasAndStatusNotIn(
                                    clazz.getId(),
                                    clazz.getClazzAlias(),
                                    List.of(DELETED))) {
                        errorMsg.append("Đã tồn tại Lớp Học khác với Mã: ").append(clazz.getClazzAlias());
                    }
                }

                case AWAIT_REVIEW -> {
                    /* Await review don't allow change of attribute => Cancel update. */
                    return oldClazz;
                }
                case OPENED, ONGOING, SUSPENDED, CLOSED -> {
                    /* Override old data */
                    oldClazz.setUpdatedBy(clazz.getUpdatedBy());
                    oldClazz.setUpdatedAt(clazz.getUpdatedAt());

                    /* Copy from old data */
                    clazz = oldClazz;

                    /* Validate input */
                    /* No change attribute => no validate */
                }
            }
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Lớp Học: \n" + errorMsg.toString());
        }

        /* Update */
        clazz = clazzRepository.saveAndFlush(clazz);

        return clazz;
    }
    @Override
    public ClazzReadDTO updateClazzByDTO(
            ClazzUpdateDTO updateDTO) throws Exception {
        Clazz clazz = mapper.map(updateDTO, Clazz.class);

        clazz = updateClazz(clazz);

        return wrapDTO(clazz, null);
    }

    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteClazz(
            Long id) throws Exception {
        Clazz clazz = getById(id, List.of(DELETED), false);

        if (clazz == null) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Lớp Học. Không tìm thấy Lớp Học nào với id: " + id);
        }

        if (!clazz.getStatus().equals(Status.DESIGNING)) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Lớp Học. Chỉ có những Lớp Học với trạng thái 'Đang thiết kế' mới được phép xóa.");
        }

        Status oldStatus = clazz.getStatus();

        /* Delete */
        clazz.setStatus(DELETED);

        /* Save to DB */
        clazzRepository.saveAndFlush(clazz);

        try {
            clazzScheduleService.deleteByClazzId(id);
        } catch (IllegalArgumentException iAE) {
            /* Revert delete */
            clazz.setStatus(oldStatus);

            clazzRepository.saveAndFlush(clazz);

            throw new IllegalArgumentException(
                    "Lỗi xóa Lớp Học. Bắt nguồn từ: \n" + iAE.getMessage());
        }

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzReadDTO wrapDTO(
            Clazz clazz, Collection<DtoOption> options) throws Exception {
        if (clazz == null) { return null; }
        
        ClazzReadDTO dto = mapper.map(clazz, ClazzReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
//            if (options.contains(DtoOption.COURSE_SEMESTER)) {
//                dto.setCourseSemester(
//                        courseSemesterService.getDTOById(clazz.getCourseSemesterId(), options));
//            }

            if (options.contains(DtoOption.COURSE)) {
                dto.setCourse(
                        courseService.getDTOById(
                                clazz.getCourseId(),
                                List.of(DELETED),
                                false,
                                options));
            }
            if (options.contains(DtoOption.COURSE_NAME)) {
                Course course =
                        courseService.getById(
                            clazz.getCourseId(),
                            List.of(DELETED),
                            false);
                dto.setCourseName(course.getCourseName());
            }
            if (options.contains(DtoOption.COURSE_ALIAS)) {
                Course course =
                        courseService.getById(
                                clazz.getCourseId(),
                                List.of(DELETED),
                                false);
                dto.setCourseAlias(course.getCourseAlias());
            }

            if (options.contains(DtoOption.CENTER)) {
                dto.setCenter(
                        centerService.getDTOById(clazz.getCenterId(), options));
            }
            if (options.contains(DtoOption.CENTER_NAME)) {
                Center center = centerService.getById(clazz.getCenterId());
                dto.setCenterName(center.getCenterName());
            }

            if (options.contains(DtoOption.STAFF)) {
                dto.setStaff(
                        staffService.getDTOById(clazz.getStaffId(), options));
            }

            if (options.contains(DtoOption.CLAZZ_SCHEDULE)) {
                dto.setClazzSchedule(
                        clazzScheduleService.getDTOByClazzId(clazz.getId(), options));
            }

            if (options.contains(DtoOption.SESSION_LIST)) {
//            TODO: dto.setSessionList();
            }

            if (options.contains(DtoOption.MEMBER_LIST)) {
                dto.setMemberList(
                        clazzMemberService.getAllDTOByClazzId(clazz.getId(), options));
            }

            if (options.contains(DtoOption.HOMEWORK_LIST)) {
//            TODO: dto.setHomeworkList();
            }

            if (options.contains(DtoOption.TEST_LIST)) {
                dto.setTestList(clazzTestService.getAllDTOByClazzId(clazz.getId(), options));
            }
        }

        return dto;
    }
    @Override
    public List<ClazzReadDTO> wrapListDTO(
            Collection<Clazz> clazzCollection, Collection<DtoOption> options) throws Exception {
        if (ObjectUtils.isEmpty(clazzCollection)) { return null; }
        
        List<ClazzReadDTO> dtoList = new ArrayList<>();

        ClazzReadDTO dto;

        Map<Long, CourseReadDTO> courseIdCourseMap = new HashMap<>();
        Map<Long, String> courseIdCourseNameMap = new HashMap<>();
        Map<Long, String> courseIdCourseAliasMap = new HashMap<>();
        Map<Long, CenterReadDTO> centerIdCenterMap = new HashMap<>();
        Map<Long, String> centerIdCenterNameMap = new HashMap<>();
        Map<Long, StaffReadDTO> staffIdStaffMap = new HashMap<>();
        Map<Long, ClazzScheduleReadDTO> clazzIdClazzScheduleMap = new HashMap<>();

//      TODO: Map<Long, List<SessionReadDTO>> clazzIdSessionListMap = new HashMap<>();
        Map<Long, List<ClazzMemberReadDTO>> clazzIdClazzMemberListMap = new HashMap<>();
//      TODO: Map<Long, List<HomeworkReadDTO>> clazzIdHomeworkListMap = new HashMap<>();
//      TODO: Map<Long, List<ClazzTestReadDTO>> clazzIdClazzTestListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> courseIdSet = new HashSet<>();
            Set<Long> centerIdSet = new HashSet<>();
            Set<Long> staffIdSet = new HashSet<>();
//            Set<Long> courseSemesterIdSet = new HashSet<>();

            for (Clazz clazz : clazzCollection) {
                clazzIdSet.add(clazz.getId());
                courseIdSet.add(clazz.getCourseId());
                centerIdSet.add(clazz.getCenterId());
                staffIdSet.add(clazz.getStaffId());
            }


            if (options.contains(DtoOption.COURSE)) {
                courseIdCourseMap =
                        courseService.mapIdDTOByIdIn(
                                courseIdSet,
                                List.of(DELETED),
                                false,
                                options);
            }
            if (options.contains(DtoOption.COURSE_NAME)) {
                courseIdCourseNameMap =
                        courseService.mapIdCourseNameByIdIn(
                                courseIdSet,
                                List.of(DELETED),
                                false);
            }
            if (options.contains(DtoOption.COURSE_ALIAS)) {
                courseIdCourseAliasMap =
                        courseService.mapIdCourseAliasByIdIn(
                                courseIdSet,
                                List.of(DELETED),
                                false);
            }

            if (options.contains(DtoOption.CENTER)) {
                centerIdCenterMap =
                        centerService.mapIdDTOByIdIn(centerIdSet, options);
            }
            if (options.contains(DtoOption.CENTER_NAME)) {
                centerIdCenterNameMap =
                        centerService.mapIdCenterNameByIdIn(centerIdSet);
            }

            if (options.contains(DtoOption.STAFF)) {
                staffIdStaffMap =
                        staffService.mapIdDTOByIdIn(staffIdSet, options);
            }

            if (options.contains(DtoOption.CLAZZ_SCHEDULE)) {
                clazzIdClazzScheduleMap =
                        clazzScheduleService.mapClazzIdDTOByClazzIdIn(clazzIdSet, options);
            }

            if (options.contains(DtoOption.SESSION_LIST)) {
                //TODO:
            }

            if (options.contains(DtoOption.MEMBER_LIST)) {
                clazzIdClazzMemberListMap =
                        clazzMemberService.mapClazzIdListDTOByClazzIdIn(clazzIdSet, options);
            }

            if (options.contains(DtoOption.HOMEWORK_LIST)) {
                //TODO:
            }

            if (options.contains(DtoOption.TEST_LIST)) {
                //TODO:
            }
        }

        for (Clazz clazz : clazzCollection) {
            dto = mapper.map(clazz, ClazzReadDTO.class);

            /* Add Dependency */
            dto.setCourse(
                    courseIdCourseMap.get(clazz.getCourseId()));
            dto.setCourseName(
                    courseIdCourseNameMap.get(clazz.getCourseId()));
            dto.setCourseAlias(
                    courseIdCourseAliasMap.get(clazz.getCourseId()));

            dto.setCenter(
                    centerIdCenterMap.get(clazz.getCenterId()));
            dto.setCenterName(
                    centerIdCenterNameMap.get(clazz.getCenterId()));

            dto.setStaff(
                    staffIdStaffMap.get(clazz.getStaffId()));

            dto.setClazzSchedule(
                    clazzIdClazzScheduleMap.get(clazz.getId()));

//            TODO: dto.setSessionList();

            dto.setMemberList(
                    clazzIdClazzMemberListMap.get(clazz.getId()));

//            TODO: dto.setHomeworkList();

//            TODO: dto.setTestList();

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<ClazzReadDTO> wrapPageDTO(
            Page<Clazz> clazzPage, Collection<DtoOption> options) throws Exception {

        if (clazzPage == null) { return null; }

        return new PageImpl<>(
                wrapListDTO(clazzPage.getContent(), options),
                clazzPage.getPageable(),
                clazzPage.getTotalPages());
    }
}