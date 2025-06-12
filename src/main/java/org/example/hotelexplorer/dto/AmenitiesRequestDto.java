package org.example.hotelexplorer.dto;

import lombok.Data;

import java.util.List;

@Data
public class AmenitiesRequestDto {
    private List<String> amenities;
}