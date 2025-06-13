package org.example.hotelexplorer.dto;

import lombok.Data;

@Data
public class HotelShortResponseDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}