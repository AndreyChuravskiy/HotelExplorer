package org.example.hotelexplorer.mapper;

import org.example.hotelexplorer.dto.ArrivalTimeDto;
import org.example.hotelexplorer.entity.ArrivalTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArrivalTimeMapper {
    ArrivalTimeDto toDto(ArrivalTime arrivalTime);
    ArrivalTime toEntity(ArrivalTimeDto arrivalTimeDto);

}