package com.teachsync.services.material;

import com.teachsync.dtos.material.MaterialCreateDTO;
import com.teachsync.dtos.material.MaterialReadDTO;
import com.teachsync.dtos.material.MaterialUpdateDTO;
import com.teachsync.entities.Material;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface MaterialService {
    /* =================================================== CREATE =================================================== */
    Material createMaterial(Material material) throws Exception;
    MaterialReadDTO createMaterialByDTO(MaterialCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    Page<Material> getPageAll(Pageable pageable) throws Exception;
    Page<MaterialReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception;

    /* id */
    Material getById(Long id) throws Exception;
    MaterialReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Material> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<MaterialReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* isFree */
    Page<Material> getPageAllByIsFree(Boolean isFree, Pageable pageable) throws Exception;
    Page<MaterialReadDTO> getPageAllDTOByIsFree(
            Boolean isFree, Pageable pageable, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Material updateMaterial(Material material) throws Exception;
    MaterialReadDTO updateMaterialByDTO(MaterialUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */
    Boolean deleteMaterial(Long id) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    MaterialReadDTO wrapDTO(Material material, Collection<DtoOption> options) throws Exception;
    List<MaterialReadDTO> wrapListDTO(
            Collection<Material> materialCollection, Collection<DtoOption> options) throws Exception;
    Page<MaterialReadDTO> wrapPageDTO(Page<Material> materialPage, Collection<DtoOption> options) throws Exception;
}
