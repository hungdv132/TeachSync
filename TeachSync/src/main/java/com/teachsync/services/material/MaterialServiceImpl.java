package com.teachsync.services.material;

import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.material.MaterialCreateDTO;
import com.teachsync.dtos.material.MaterialReadDTO;
import com.teachsync.dtos.material.MaterialUpdateDTO;
import com.teachsync.entities.Material;
import com.teachsync.repositories.MaterialRepository;
import com.teachsync.services.courseMaterial.CourseMaterialService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CourseMaterialService courseMaterialService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @Override
    public Material createMaterial(Material material) throws Exception {
        /* Validate input */
        /* TODO: valid link, ... */

        /* Check FK */
        /* No FK */

        /* Check duplicate */
        if (materialRepository
                .existsByMaterialNameOrMaterialLinkAndStatusNot(
                        material.getMaterialName(),
                        material.getMaterialLink(),
                        Status.DELETED)) {
            throw new IllegalArgumentException(
                    "Already exists Material with Name: " + material.getMaterialName()
                            + " or with Link: " + material.getMaterialLink());
        }

        /* Save to DB */
        material = materialRepository.saveAndFlush(material);

        return material;
    }
    @Override
    public MaterialReadDTO createMaterialByDTO(MaterialCreateDTO createDTO) throws Exception {
        Material material = mapper.map(createDTO, Material.class);

        material = createMaterial(material);

        /* Create dependency */
        /* No dependency */

        return wrapDTO(material, null);
    }

    /* =================================================== READ ===================================================== */
    @Override
    public Page<Material> getPageAll(Pageable pageable) throws Exception {
        Page<Material> materialPage = materialRepository.findAllByStatusNot(Status.DELETED, pageable);
        
        if (materialPage.isEmpty()) {
            return null;
        }
        
        return materialPage;
    }
    @Override
    public Page<MaterialReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }
        
        Page<Material> materialPage = getPageAll(pageable);
        
        if (materialPage == null) {
            return null;
        }
        
        return wrapPageDTO(materialPage, options);
    }

    /* id */
    @Override
    public Material getById(Long id) throws Exception {
        return materialRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public MaterialReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Material material = getById(id);

        if (material == null) {
            return null;
        }

        return wrapDTO(material, options);
    }

    @Override
    public List<Material> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Material> materialList = materialRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (materialList.isEmpty()) {
            return null;
        }

        return materialList;
    }
    @Override
    public List<MaterialReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Material> materialList = getAllByIdIn(idCollection);

        if (materialList == null) {
            return null;
        }

        return wrapListDTO(materialList, options);
    }

    /* isFree */
    @Override
    public Page<Material> getPageAllByIsFree(Boolean isFree, Pageable pageable) throws Exception {
        Page<Material> materialPage = materialRepository.findAllByStatusNot(Status.DELETED, pageable);

        if (materialPage.isEmpty()) {
            return null;
        }

        return materialPage;
    }
    @Override
    public Page<MaterialReadDTO> getPageAllDTOByIsFree(
            Boolean isFree, Pageable pageable, Collection<DtoOption> options) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<Material> materialPage = getPageAllByIsFree(isFree, pageable);

        if (materialPage == null) {
            return null;
        }

        return wrapPageDTO(materialPage, options);
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Material updateMaterial(Material material) throws Exception {
        Material oldMaterial = getById(material.getId());

        /* Check exist by id */
        if (oldMaterial == null) {
            throw new IllegalArgumentException("Update Error. No Material found with Id: " + material.getId());
        }
        material.setCreatedAt(oldMaterial.getCreatedAt());
        material.setCreatedBy(oldMaterial.getCreatedBy());

        /* Validate input */
        /* TODO: valid link, name max 45 char, ... */

        /* Check FK */
        /* No FK */

        /* Check duplicate */
        if (materialRepository
                .existsByIdNotAndMaterialNameOrMaterialLinkAndStatusNot(
                        material.getId(),
                        material.getMaterialName(),
                        material.getMaterialLink(),
                        Status.DELETED)) {
            throw new IllegalArgumentException(
                    "Already exists Material with Name: " + material.getMaterialName()
                            + " or with Link: " + material.getMaterialLink());
        }

        /* Save to DB */
        material = materialRepository.saveAndFlush(material);

        return material;
    }
    @Override
    public MaterialReadDTO updateMaterialByDTO(MaterialUpdateDTO updateDTO) throws Exception {
        Material material = mapper.map(updateDTO, Material.class);

        material = updateMaterial(material);

        /* Create new dependency */
        /* No dependency */

        /* Update existing dependency */
        /* No dependency */

        return wrapDTO(material, null);
    }

    
    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteMaterial(Long id) throws Exception {
        Material material = getById(id);

        if (material == null) {
            throw new IllegalArgumentException("Delete error. No Material found with Id: " + id);
        }

        /* Set delete */
        material.setStatus(Status.DELETED);

        /* Save to DB */
        materialRepository.saveAndFlush(material);

        /* Cascade delete */
        /* TODO: delete cascade CourseMaterial by materialId */
//        courseMaterialService.deleteAllByMaterialId(id);

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public MaterialReadDTO wrapDTO(Material material, Collection<DtoOption> options) throws Exception {
        MaterialReadDTO dto = mapper.map(material, MaterialReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
//            if (options.contains(DtoOption.FK)) {
//                FkReadDTO fkDTO = fkService.getDTOById(material.getFkId());
//                dto.setFk(fkDTO);
//            }

            if (options.contains(DtoOption.COURSE_LIST)) {
                List<CourseReadDTO> courseDTOList =
                        courseMaterialService.getAllCourseDTOByMaterialId(material.getId());
                dto.setCourseList(courseDTOList);
            }
        }

        return dto;
    }
    @Override
    public List<MaterialReadDTO> wrapListDTO(Collection<Material> materialCollection, Collection<DtoOption> options) throws Exception {
        List<MaterialReadDTO> dtoList = new ArrayList<>();
        MaterialReadDTO dto;

//        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<CourseReadDTO>> materialIdIdCourseDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> materialIdSet = new HashSet<>();

            for (Material material : materialCollection) {
                materialIdSet.add(material.getId());
            }

//            if (options.contains(DtoOption.FK)) {
//                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
//            }

            if (options.contains(DtoOption.COURSE_LIST)) {
                materialIdIdCourseDTOListMap =
                        courseMaterialService.mapMaterialIdListCourseDTOByMaterialIdIn(materialIdSet);
            }
        }

        for (Material material : materialCollection) {
            dto = mapper.map(material, MaterialReadDTO.class);

            /* Add dependency */
//            dto.setFk(fkIdFkDTOMap.get(material.getFkId()));

            dto.setCourseList(materialIdIdCourseDTOListMap.get(material.getId()));

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<MaterialReadDTO> wrapPageDTO(Page<Material> materialPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(materialPage.getContent(), options),
                materialPage.getPageable(),
                materialPage.getTotalPages());
    }
}
