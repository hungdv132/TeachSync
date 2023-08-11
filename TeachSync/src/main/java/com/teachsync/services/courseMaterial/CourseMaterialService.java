package com.teachsync.services.courseMaterial;

import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.courseMaterial.CourseMaterialReadDTO;
import com.teachsync.entities.CourseMaterial;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CourseMaterialService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* materialId */
    List<CourseMaterial> getAllByMaterialId(Long materialId) throws Exception;
    List<CourseReadDTO> getAllCourseDTOByMaterialId(Long materialId) throws Exception;


    List<CourseMaterial> getAllByMaterialIdIn(Collection<Long> materialIdCollection) throws Exception;
    Map<Long, List<CourseReadDTO>> mapMaterialIdListCourseDTOByMaterialIdIn(
            Collection<Long> materialIdCollection) throws Exception;

    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    CourseMaterialReadDTO wrapDTO(CourseMaterial courseMaterial, Collection<DtoOption> options) throws Exception;
    List<CourseMaterialReadDTO> wrapListDTO(
            Collection<CourseMaterial> courseMaterialCollection, Collection<DtoOption> options) throws Exception;
    Page<CourseMaterialReadDTO> wrapPageDTO(
            Page<CourseMaterial> courseMaterialPage, Collection<DtoOption> options) throws Exception;
}
