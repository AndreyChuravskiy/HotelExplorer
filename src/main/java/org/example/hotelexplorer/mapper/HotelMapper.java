package org.example.hotelexplorer.mapper;

import org.example.hotelexplorer.dto.HotelCreateRequestDto;
import org.example.hotelexplorer.dto.HotelFullResponseDto;
import org.example.hotelexplorer.dto.HotelShortResponseDto;
import org.example.hotelexplorer.entity.Address;
import org.example.hotelexplorer.entity.Amenity;
import org.example.hotelexplorer.entity.Contacts;
import org.example.hotelexplorer.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring",
        uses = {AddressMapper.class, ContactsMapper.class, ArrivalTimeMapper.class}
)
public interface HotelMapper {

    @Mapping(target = "amenities", source = "amenities", qualifiedByName = "amenitiesToNames")
    HotelFullResponseDto toFullResponseDto(Hotel hotel);
    List<HotelFullResponseDto> toFullResponseDtoList(List<Hotel> hotels);

    @Mapping(source = "address", target = "address", qualifiedByName = "addressToString")
    @Mapping(source = "contacts", target = "phone", qualifiedByName = "phoneFromContacts")
    HotelShortResponseDto toShortResponseDto(Hotel hotel);
    List<HotelShortResponseDto> toShortResponseDtoList(List<Hotel> hotels);

    Hotel toEntity(HotelCreateRequestDto dto);

    @Named("amenitiesToNames")
    default List<String> amenitiesToNames(Set<Amenity> amenities) {
        if (amenities == null) return null;
        return amenities.stream().map(Amenity::getName).toList();
    }

    @Named("addressToString")
    default String addressToString(Address address) {
        if (address == null) return null;
        return address.getHouseNumber() + " " + address.getStreet() + ", " + address.getCity() + ", " + address.getPostCode() + ", " + address.getCountry();
    }

    @Named("phoneFromContacts")
    default String phoneFromContacts(Contacts contacts) {
        if (contacts == null) return null;
        return contacts.getPhone();
    }
}