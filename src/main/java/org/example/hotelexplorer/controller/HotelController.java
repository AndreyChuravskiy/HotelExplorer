package org.example.hotelexplorer.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotelexplorer.dto.HotelCreateRequestDto;
import org.example.hotelexplorer.dto.HotelFullResponseDto;
import org.example.hotelexplorer.dto.HotelShortResponseDto;
import org.example.hotelexplorer.service.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public List<HotelShortResponseDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public HotelFullResponseDto getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/search")
    public List<HotelShortResponseDto> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities
    ) {
        return hotelService.searchHotels(name, brand, city, country, amenities);
    }

    @PostMapping("/hotels")
    public HotelShortResponseDto createHotel(@RequestBody HotelCreateRequestDto request) {
        return hotelService.createHotel(request);
    }

    @PostMapping("/hotels/{id}/amenities")
    public void addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Integer> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}