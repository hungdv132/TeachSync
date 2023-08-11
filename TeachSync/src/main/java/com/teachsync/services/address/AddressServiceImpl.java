package com.teachsync.services.address;


import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.address.AddressUpdateDTO;
import com.teachsync.entities.Address;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.LocationUnit;
import com.teachsync.repositories.AddressRepository;
import com.teachsync.repositories.LocationUnitRepository;
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
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private LocationUnitRepository locationUnitRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public Address createAddress(Address address) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..
        return addressRepository.saveAndFlush(address);
    }
    @Override
    public AddressReadDTO createAddressByDTO(AddressCreateDTO addressCreateDTO) throws Exception {
        Address address = mapper.map(addressCreateDTO, Address.class);
        ArrayList<LocationUnit> unitList = new ArrayList<>();
        LocationUnit locationUnit = locationUnitRepository.findByIdAndStatusNot(address.getUnitId(), Status.DELETED).orElse(null);
        while (locationUnit.getParentId() != null){
            unitList.add(locationUnit);
            locationUnit = locationUnitRepository.findByIdAndStatusNot(locationUnit.getParentId(), Status.DELETED).orElse(null);
        }
        StringBuilder addressString = new StringBuilder(address.getAddressNo() + " " + address.getStreet());

        for (LocationUnit unit: unitList) {
            addressString.append(", ").append(unit.getUnitAlias());
        }

        address.setAddressString(addressString.toString());
        address = createAddress(address);
        return mapper.map(address, AddressReadDTO.class);
    }


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Address getById(Long id) throws Exception {
        return addressRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public AddressReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Address address = getById(id);

        if (address == null) {
            return null;
        }

        return wrapDTO(address, options);
    }

    @Override
    public List<Address> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Address> addressesList = addressRepository.findAllByIdInAndStatusNot(idCollection,Status.DELETED);
        if(addressesList.isEmpty()){
            return null;
        }
        return addressesList;
    }
    @Override
    public List<AddressReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Address> addressList = getAllByIdIn(idCollection);

        if (addressList == null) {
            return null;
        }

        return wrapListDTO(addressList, options);
    }
    @Override
    public Map<Long, AddressReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<AddressReadDTO> addressDTOList = getAllDTOByIdIn(idCollection, options);

        if (addressDTOList == null) {
            return new HashMap<>();
        }

        return addressDTOList.stream().collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Address updateAddress(Address address) throws Exception {
        return null;
    }
    @Override
    public AddressReadDTO updateAddressByDTO(AddressUpdateDTO addressUpdateDTO) throws Exception {
        return null;
    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public AddressReadDTO wrapDTO(Address address, Collection<DtoOption> options) throws Exception {
        AddressReadDTO dto = mapper.map(address, AddressReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(address.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(address.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<AddressReadDTO> wrapListDTO(Collection<Address> addressCollection, Collection<DtoOption> options) throws Exception {
        List<AddressReadDTO> dtoList = new ArrayList<>();
        AddressReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Address address : addressCollection) {
                fkIdSet.add(address.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Address address : addressCollection) {
            dto = mapper.map(address, AddressReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(address.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(address.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<AddressReadDTO> wrapPageDTO(Page<Address> addressPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(addressPage.getContent(), options),
                addressPage.getPageable(),
                addressPage.getTotalPages());
    }
}
