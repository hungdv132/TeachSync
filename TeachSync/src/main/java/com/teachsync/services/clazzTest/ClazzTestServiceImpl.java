package com.teachsync.services.clazzTest;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazzTest.ClazzTestReadDTO;
import com.teachsync.dtos.test.TestReadDTO;
import com.teachsync.entities.ClazzTest;
import com.teachsync.repositories.ClazzTestRepository;
import com.teachsync.services.test.TestService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClazzTestServiceImpl implements ClazzTestService {
    @Autowired
    private ClazzTestRepository clazzTestRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public ClazzTest getById(Long id) throws Exception {
        return clazzTestRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzTestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        ClazzTest clazzTest = getById(id);

        if (clazzTest == null) {
            return null;
        }

        return wrapDTO(clazzTest, options);
    }

    @Override
    public List<ClazzTest> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<ClazzTest> clazzTestList = clazzTestRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (clazzTestList.isEmpty()) {
            return null;
        }

        return clazzTestList;
    }
    @Override
    public List<ClazzTestReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzTest> clazzTestList = getAllByIdIn(idCollection);

        if (clazzTestList == null) {
            return null;
        }

        return wrapListDTO(clazzTestList, options);
    }
    @Override
    public Map<Long, ClazzTestReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzTestReadDTO> clazzTestDTOList = getAllDTOByIdIn(idCollection, options);

        if (clazzTestDTOList == null) {
            return new HashMap<>();
        }

        return clazzTestDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* clazzId */
    @Override
    public List<ClazzTest> getAllByClazzId(Long clazzId) throws Exception {
        List<ClazzTest> clazzTestList =
                clazzTestRepository.findAllByClazzIdAndStatusNot(clazzId, Status.DELETED);

        if (clazzTestList.isEmpty()) {
            return null;
        }

        return clazzTestList;
    }
    @Override
    public List<ClazzTestReadDTO> getAllDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception {
        List<ClazzTest> clazzTestList = getAllByClazzId(clazzId);

        if (clazzTestList == null) {
            return null;
        }

        return wrapListDTO(clazzTestList, options);
    }

    /* clazzId & testId */
    @Override
    public ClazzTest getByClazzIdAndTestId(Long clazzId, Long testId) throws Exception {
        return clazzTestRepository
                .findByClazzIdAndTestIdAndStatusNot(clazzId, testId, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzTestReadDTO getDTOByClazzIdAndTestId(Long clazzId, Long testId, Collection<DtoOption> options) throws Exception {
        ClazzTest clazzTest = getByClazzIdAndTestId(clazzId, testId);

        if (clazzTest == null) {
            return null;
        }

        return wrapDTO(clazzTest, options);
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzTestReadDTO wrapDTO(ClazzTest clazzTest, Collection<DtoOption> options) throws Exception {
        ClazzTestReadDTO dto = mapper.map(clazzTest, ClazzTestReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.TEST)) {
                TestReadDTO testDTO = testService.getDTOById(clazzTest.getTestId(), options);
                dto.setTest(testDTO);
            }
        }

        return dto;
    }

    @Override
    public List<ClazzTestReadDTO> wrapListDTO(
            Collection<ClazzTest> clazzTestCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzTestReadDTO> dtoList = new ArrayList<>();
        ClazzTestReadDTO dto;

        Map<Long, TestReadDTO> testIdTestDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> testIdSet = new HashSet<>();

            for (ClazzTest clazzTest : clazzTestCollection) {
                testIdSet.add(clazzTest.getTestId());
            }

            if (options.contains(DtoOption.TEST)) {
                testIdTestDTOMap = testService.mapIdDTOByIdIn(testIdSet, options);
            }
        }

        for (ClazzTest clazzTest : clazzTestCollection) {
            dto = mapper.map(clazzTest, ClazzTestReadDTO.class);

            /* Add dependency */
            dto.setTest(testIdTestDTOMap.get(clazzTest.getTestId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ClazzTestReadDTO> wrapPageDTO(Page<ClazzTest> clazzTestPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(clazzTestPage.getContent(), options),
                clazzTestPage.getPageable(),
                clazzTestPage.getTotalPages());
    }
}
