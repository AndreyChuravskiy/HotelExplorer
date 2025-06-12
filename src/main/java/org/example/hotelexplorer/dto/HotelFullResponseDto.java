package org.example.hotelexplorer.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotelFullResponseDto {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;
}