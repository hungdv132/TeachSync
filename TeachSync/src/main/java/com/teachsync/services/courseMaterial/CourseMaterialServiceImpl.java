package com.teachsync.services.courseMaterial;

import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseMaterial.CourseMaterialReadDTO;
import com.teachsync.entities.CourseMaterial;
import com.teachsync.repositories.CourseMaterialRepository;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.material.MaterialService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {
    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Lazy
    @Autowired
    private CourseService courseService;
    @Lazy
    @Autowired
    private MaterialService materialService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    @Override
    public List<CourseMaterial> getAllByMaterialId(Long materialId) throws Exception {
        List<CourseMaterial> courseMaterialList =
                courseMaterialRepository.findAllByMaterialIdAndStatusNot(materialId, Status.DELETED);

        if (courseMaterialList.isEmpty()) {
            return null;
        }

        return courseMaterialList;
    }
    @Override
    public List<CourseReadDTO> getAllCourseDTOByMaterialId(Long materialId) throws Exception {
        List<CourseMaterial> courseMaterialList = getAllByMaterialId(materialId);

        if (courseMaterialList == null) {
            return null;
        }

        Set<Long> courseIdSet = courseMaterialList.stream()
                .map(CourseMaterial::getCourseId)
                .collect(Collectors.toSet());

        return courseService.getAllDTOByIdIn(courseIdSet, null);
    }

    @Override
    public List<CourseMaterial> getAllByMaterialIdIn(Collection<Long> materialIdCollection) throws Exception {
        List<CourseMaterial> courseMaterialList =
                courseMaterialRepository.findAllByMaterialIdInAndStatusNot(materialIdCollection, Status.DELETED);

        if (courseMaterialList.isEmpty()) {
            return null;
        }

        return courseMaterialList;
    }
    @Override
    public Map<Long, List<CourseReadDTO>> mapMaterialIdListCourseDTOByMaterialIdIn(
            Collection<Long> materialIdCollection) throws Exception {
        List<CourseMaterial> courseMaterialList = getAllByMaterialIdIn(materialIdCollection);

        if (courseMaterialList == null) {
            return new HashMap<>();
        }

        Set<Long> courseIdSet = courseMaterialList.stream()
                .map(CourseMaterial::getCourseId)
                .collect(Collectors.toSet());

        Map<Long, CourseReadDTO> courseIdCourseDTOMap = courseService.mapIdDTOByIdIn(courseIdSet, null);

        Map<Long, List<CourseReadDTO>> materialIdCourseDTOListMap = new HashMap<>();
        Long materialId;
        List<CourseReadDTO> tmpCourseDTOList;

        for (CourseMaterial courseMaterial : courseMaterialList) {
            materialId = courseMaterial.getMaterialId();

            tmpCourseDTOList = materialIdCourseDTOListMap.get(materialId);

            if (tmpCourseDTOList == null) {
                materialIdCourseDTOListMap.put(
                        materialId,
                        new ArrayList<>(List.of(courseIdCourseDTOMap.get(courseMaterial.getCourseId()))));
            } else {
                tmpCourseDTOList.add(courseIdCourseDTOMap.get(courseMaterial.getCourseId()));

                materialIdCourseDTOListMap.put(materialId, tmpCourseDTOList);
            }
        }

        return materialIdCourseDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CourseMaterialReadDTO wrapDTO(
            CourseMaterial courseMaterial, Collection<DtoOption> options) throws Exception {
        CourseMaterialReadDTO dto = mapper.map(courseMaterial, CourseMaterialReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(courseMaterial.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(courseMaterial.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }
    @Override
    public List<CourseMaterialReadDTO> wrapListDTO(
            Collection<CourseMaterial> courseMaterialCollection, Collection<DtoOption> options) throws Exception {
        List<CourseMaterialReadDTO> dtoList = new ArrayList<>();
        CourseMaterialReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (CourseMaterial courseMaterial : courseMaterialCollection) {
                fkIdSet.add(courseMaterial.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (CourseMaterial courseMaterial : courseMaterialCollection) {
            dto = mapper.map(courseMaterial, CourseMaterialReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(courseMaterial.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(courseMaterial.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<CourseMaterialReadDTO> wrapPageDTO(
            Page<CourseMaterial> courseMaterialPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(courseMaterialPage.getContent(), options),
                courseMaterialPage.getPageable(),
                courseMaterialPage.getTotalPages());
    }
}
