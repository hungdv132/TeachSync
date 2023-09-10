package com.teachsync.services.clazz;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzCreateDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazz.ClazzUpdateDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.courseSemester.CourseSemesterReadDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.CourseSemester;
import com.teachsync.repositories.ClazzRepository;
import com.teachsync.repositories.CourseSemesterRepository;
import com.teachsync.services.clazzMember.ClazzMemberService;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private ClazzMemberService clazzMemberService;
    @Autowired
    private ClazzScheduleService clazzScheduleService;
    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CourseSemesterRepository courseSemesterRepository;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;

    private Logger logger = LoggerFactory.getLogger(ClazzServiceImpl.class);



    /* =================================================== CREATE =================================================== */
    @Override
    public Clazz createClazz(Clazz clazz) throws Exception {
        /* Validate input */
        /* TODO: */

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

        /* Create */
        clazz = clazzRepository.saveAndFlush(clazz);

        return clazz;
    }
    @Override
    public ClazzReadDTO createClazzByDTO(ClazzCreateDTO createDTO) throws Exception {
        Clazz clazz = mapper.map(createDTO, Clazz.class);

        CourseSemester courseSemester =
                courseSemesterService.getByCourseIdAndSemesterIdAndCenterId(
                        createDTO.getCourseId(), createDTO.getSemesterId(), createDTO.getCenterId());

        if (courseSemester == null) {
            courseSemester = new CourseSemester(
                    createDTO.getCourseId(), createDTO.getSemesterId(), createDTO.getCenterId());
            courseSemester.setStatus(Status.CREATED);

            courseSemester = courseSemesterService.createCourseSemester(courseSemester);
        }

        clazz.setCourseSemesterId(courseSemester.getId());

        clazz = createClazz(clazz);

        /* TODO: create clazzSchedule */

        return wrapDTO(clazz, null);
    }

    /* =================================================== READ ===================================================== */
    @Override
    public Page<Clazz> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging();
        }
        
        Page<Clazz> clazzPage =
                clazzRepository.findAllByStatusNot(Status.DELETED, paging);
        
        if (clazzPage.isEmpty()) {
            return null;
        }
        
        return clazzPage;
    }
    @Override
    public Page<ClazzReadDTO> getPageDTOAll(Pageable paging) throws Exception {
        Page<Clazz> clazzPage = getPageAll(paging);

        if (clazzPage == null) {
            return null;
        }
        
        return wrapPageDTO(clazzPage, null);
    }
    @Override
    public Page<ClazzReadDTO> getPageDTOAll(Pageable paging, Collection<DtoOption> options) throws Exception {
        Page<Clazz> clazzPage = getPageAll(paging);

        if (clazzPage == null) {
            return null;
        }

        return wrapPageDTO(clazzPage, options);
    }

    /* id */
    @Override
    public Clazz getById(Long id) throws Exception {
        return clazzRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzReadDTO getDTOById(Long id) throws Exception {
        Clazz clazz = getById(id);

        if (clazz == null) {
            return null;
        }

        return wrapDTO(clazz, null);
    }
    @Override
    public ClazzReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Clazz clazz = getById(id);

        if (clazz == null) {
            return null;
        }

        return wrapDTO(clazz, options);
    }

    @Override
    public List<Clazz> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Clazz> clazzList =
                clazzRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (clazzList.isEmpty()) {
            return null;
        }

        return clazzList;
    }
    @Override
    public Map<Long, String> mapClazzIdClazzNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<Clazz> roomList = getAllByIdIn(idCollection);

        if (roomList.isEmpty()) {
            return null; }

        return roomList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Clazz::getClazzName));
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Clazz> clazzList = getAllByIdIn(idCollection);

        if (clazzList == null) {
            return null;
        }

        return wrapListDTO(clazzList, options);
    }
    @Override
    public Map<Long, ClazzReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzReadDTO> clazzDTOList = getAllDTOByIdIn(idCollection, options);

        if (clazzDTOList == null) {
            return new HashMap<>();
        }

        return clazzDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* courseSemesterId */
    @Override
    public List<Clazz> getAllByCourseSemesterId(Long courseSemesterId) throws Exception {
        List<Clazz> clazzList =
                clazzRepository.findAllByCourseSemesterIdAndStatusNot(courseSemesterId, Status.DELETED);

        if (clazzList.isEmpty()) {
            return null;
        }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseSemesterId(
            Long courseSemesterId, Collection<DtoOption> options) throws Exception {
        List<Clazz> clazzList = getAllByCourseSemesterId(courseSemesterId);

        if (clazzList == null) {
            return null;
        }

        return wrapListDTO(clazzList, options);
    }

    @Override
    public List<Clazz> getAllByCourseSemesterIdIn(
            Collection<Long> courseSemesterIdCollection) throws Exception {
        List<Clazz> clazzList =
                clazzRepository.findAllByCourseSemesterIdInAndStatusNot(courseSemesterIdCollection, Status.DELETED);

        if (clazzList.isEmpty()) {
            return null;
        }

        return clazzList;
    }
    @Override
    public List<ClazzReadDTO> getAllDTOByCourseSemesterIdIn(
            Collection<Long> courseSemesterIdCollection, Collection<DtoOption> options) throws Exception {
        List<Clazz> clazzList = getAllByCourseSemesterIdIn(courseSemesterIdCollection);

        if (clazzList == null) {
            return null;
        }

        return wrapListDTO(clazzList, options);
    }
    @Override
    public Map<Long, List<ClazzReadDTO>> mapCourseSemesterIdListDTOByCourseSemesterIdIn(
            Collection<Long> courseSemesterIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzReadDTO> clazzDTOList = getAllDTOByCourseSemesterIdIn(courseSemesterIdCollection, options);

        if (clazzDTOList == null) {
            return new HashMap<>(); }

        Map<Long, List<ClazzReadDTO>> courseSemesterIdClazzDTOListMap = new HashMap<>();
        long courseSemesterId;
        List<ClazzReadDTO> tmpList;
        for (ClazzReadDTO dto : clazzDTOList) {
            courseSemesterId = dto.getCourseSemesterId();

            tmpList = courseSemesterIdClazzDTOListMap.get(courseSemesterId);
            if (tmpList == null) {
                courseSemesterIdClazzDTOListMap.put(courseSemesterId, new ArrayList<>(List.of(dto)));
            } else {
                tmpList.add(dto);
                courseSemesterIdClazzDTOListMap.put(courseSemesterId, tmpList);
            }
        }

        return courseSemesterIdClazzDTOListMap;
    }

    /* =================================================== UPDATE =================================================== */
    @Override
    public Clazz updateClazz(Clazz clazz) throws Exception {
        /* Check exist */
        Clazz oldClazz = getById(clazz.getId());
        if (oldClazz == null) {
            throw new IllegalArgumentException("Update error. No Clazz found with id: " + clazz.getId());
        }
        clazz.setCreatedBy(oldClazz.getCreatedBy());
        clazz.setCreatedAt(oldClazz.getCreatedAt());

        /* Validate input */
        /* TODO: */

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

        /* Create */
        clazz = clazzRepository.saveAndFlush(clazz);

        return clazz;
    }
    @Override
    public ClazzReadDTO updateClazzByDTO(ClazzUpdateDTO updateDTO) throws Exception {
        Clazz oldClazz = getById(updateDTO.getId());
        if (oldClazz == null) {
            throw new IllegalArgumentException("No Clazz Found with Id: " + updateDTO.getId());
        }

        Clazz clazz = mapper.map(updateDTO, Clazz.class);

        CourseSemester courseSemester =
                courseSemesterService.getByCourseIdAndSemesterIdAndCenterId(
                        updateDTO.getCourseId(), updateDTO.getSemesterId(), updateDTO.getCenterId());

        if (courseSemester == null) {
            courseSemester = new CourseSemester(
                    updateDTO.getCourseId(), updateDTO.getSemesterId(), updateDTO.getCenterId());
            courseSemester.setStatus(Status.CREATED);

            courseSemester = courseSemesterService.createCourseSemester(courseSemester);
        }

        clazz.setCourseSemesterId(courseSemester.getId());

        clazz = updateClazz(clazz);

        /* TODO: update clazzSchedule */

        return wrapDTO(clazz, null);
    }

    /* =================================================== DELETE =================================================== */
    @Override
    public String deleteClazz(Long Id) {
        try{
            Clazz clazz = clazzRepository.findById(Id).orElseThrow(() -> new Exception("Không tìm thấy lớp học"));
            clazz.setStatus(Status.DELETED);
            clazzRepository.saveAndFlush(clazz);
            return "success";
        }catch (Exception e){
            logger.error("Error when deleteClazz  : " + e.getMessage());
            return "error";
        }
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzReadDTO wrapDTO(Clazz clazz, Collection<DtoOption> options) throws Exception {
        ClazzReadDTO dto = mapper.map(clazz, ClazzReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.COURSE_SEMESTER)) {
                dto.setCourseSemester(
                        courseSemesterService.getDTOById(clazz.getCourseSemesterId(), options));
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

            if (options.contains(DtoOption.STAFF)) {
                dto.setStaff(
                        staffService.getDTOById(clazz.getStaffId(), options));
            }

            if (options.contains(DtoOption.HOMEWORK_LIST)) {
//            TODO: dto.setHomeworkList();
            }

            if (options.contains(DtoOption.TEST_LIST)) {
//            TODO: dto.setTestList();
            }
        }

        return dto;
    }
    @Override
    public List<ClazzReadDTO> wrapListDTO(Collection<Clazz> clazzCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzReadDTO> dtoList = new ArrayList<>();

        ClazzReadDTO dto;

        Map<Long, CourseSemesterReadDTO> scheduleIdCourseSemesterMap = new HashMap<>();
        Map<Long, ClazzScheduleReadDTO> clazzIdClazzScheduleMap = new HashMap<>();
//      TODO: Map<Long, List<SessionReadDTO>> clazzIdSessionListMap = new HashMap<>();
        Map<Long, List<ClazzMemberReadDTO>> clazzIdClazzMemberListMap = new HashMap<>();
        Map<Long, StaffReadDTO> staffIdStaffMap = new HashMap<>();
//      TODO: Map<Long, List<HomeworkReadDTO>> clazzIdHomeworkListMap = new HashMap<>();
//      TODO: Map<Long, List<ClazzTestReadDTO>> clazzIdClazzTestListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> courseSemesterIdSet = new HashSet<>();
            Set<Long> staffIdSet = new HashSet<>();

            for (Clazz clazz : clazzCollection) {
                clazzIdSet.add(clazz.getId());
                courseSemesterIdSet.add(clazz.getCourseSemesterId());
                staffIdSet.add(clazz.getStaffId());
            }
            if (options.contains(DtoOption.COURSE_SEMESTER)) {
                scheduleIdCourseSemesterMap =
                        courseSemesterService.mapIdDTOByIdIn(courseSemesterIdSet, options);
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

            if (options.contains(DtoOption.STAFF)) {
                staffIdStaffMap =
                        staffService.mapIdDTOByIdIn(staffIdSet, options);
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
            dto.setCourseSemester(
                    scheduleIdCourseSemesterMap.get(clazz.getCourseSemesterId()));

            dto.setClazzSchedule(
                    clazzIdClazzScheduleMap.get(clazz.getId()));

//            TODO: dto.setSessionList();

            dto.setMemberList(
                    clazzIdClazzMemberListMap.get(clazz.getId()));

            dto.setStaff(
                    staffIdStaffMap.get(clazz.getStaffId()));

//            TODO: dto.setHomeworkList();

//            TODO: dto.setTestList();

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<ClazzReadDTO> wrapPageDTO(Page<Clazz> clazzPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(clazzPage.getContent(), options),
                clazzPage.getPageable(),
                clazzPage.getTotalPages());
    }
}
