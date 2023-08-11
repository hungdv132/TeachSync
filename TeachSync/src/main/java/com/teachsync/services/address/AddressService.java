package com.teachsync.services.address;

import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.address.AddressUpdateDTO;
import com.teachsync.entities.Address;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AddressService {
    /* =================================================== CREATE =================================================== */
    Address createAddress(Address address) throws Exception;
    AddressReadDTO createAddressByDTO(AddressCreateDTO addressCreateDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    /* id */
    Address getById(Long id) throws Exception;
    AddressReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Address> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<AddressReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, AddressReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Address updateAddress(Address address) throws Exception;

    AddressReadDTO updateAddressByDTO(AddressUpdateDTO addressUpdateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    AddressReadDTO wrapDTO(Address address, Collection<DtoOption> options) throws Exception;
    List<AddressReadDTO> wrapListDTO(Collection<Address> addressCollection, Collection<DtoOption> options) throws Exception;
    Page<AddressReadDTO> wrapPageDTO(Page<Address> addressPage, Collection<DtoOption> options) throws Exception;
}
