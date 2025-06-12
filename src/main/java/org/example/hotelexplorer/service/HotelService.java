package org.example.hotelexplorer.service;

import org.example.hotelexplorer.dto.HotelCreateRequestDto;
import org.example.hotelexplorer.dto.HotelFullResponseDto;
import org.example.hotelexplorer.dto.HotelShortResponseDto;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelShortResponseDto> getAllHotels();
    HotelFullResponseDto getHotelById(Long id);
    List<HotelShortResponseDto> searchHotels(String name, String brand, String city, String country, List<String> amenities);
    HotelShortResponseDto createHotel(HotelCreateRequestDto request);
    void addAmenities(Long hotelId, List<String> amenities);
    Map<String, Integer> getHistogram(String param);
}