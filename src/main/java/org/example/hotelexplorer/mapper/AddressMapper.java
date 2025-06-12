package org.example.hotelexplorer.mapper;

import org.example.hotelexplorer.dto.AddressDto;
import org.example.hotelexplorer.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
    Address toEntity(AddressDto addressDto);
}