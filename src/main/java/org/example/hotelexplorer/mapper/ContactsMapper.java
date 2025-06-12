package org.example.hotelexplorer.mapper;

import org.example.hotelexplorer.dto.ContactsDto;
import org.example.hotelexplorer.entity.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    ContactsDto toDto(Contacts contacts);
    Contacts toEntity(ContactsDto contactsDto);
}