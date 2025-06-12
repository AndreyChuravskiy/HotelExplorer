package org.example.hotelexplorer.dto;

import lombok.Data;

@Data
public class HotelCreateRequestDto {
    private String name;
    private String description;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
}