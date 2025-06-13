package org.example.hotelexplorer.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.hotelexplorer.dto.HotelCreateRequestDto;
import org.example.hotelexplorer.dto.HotelFullResponseDto;
import org.example.hotelexplorer.dto.HotelShortResponseDto;
import org.example.hotelexplorer.entity.Amenity;
import org.example.hotelexplorer.entity.Hotel;
import org.example.hotelexplorer.mapper.HotelMapper;
import org.example.hotelexplorer.repository.AmenityRepository;
import org.example.hotelexplorer.repository.HotelRepository;
import org.example.hotelexplorer.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelShortResponseDto> getAllHotels() {
        return hotelMapper.toShortResponseDtoList(hotelRepository.findAll());
    }

    @Override
    public HotelFullResponseDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found"));
        return hotelMapper.toFullResponseDto(hotel);
    }

    @Override
    public List<HotelShortResponseDto> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        Set<Hotel> hotels = null;

        if (name != null && !name.isBlank()) {
            hotels = new HashSet<>(hotelRepository.findByNameContainingIgnoreCase(name));
        }
        if (brand != null && !brand.isBlank()) {
            List<Hotel> byBrand = hotelRepository.findByBrandContainingIgnoreCase(brand);
            hotels = hotels == null ? new HashSet<>(byBrand) : intersection(hotels, byBrand);
        }
        if (city != null && !city.isBlank()) {
            List<Hotel> byCity = hotelRepository.findByCity(city);
            hotels = hotels == null ? new HashSet<>(byCity) : intersection(hotels, byCity);
        }
        if (country != null && !country.isBlank()) {
            List<Hotel> byCountry = hotelRepository.findByCountry(country);
            hotels = hotels == null ? new HashSet<>(byCountry) : intersection(hotels, byCountry);
        }
        if (amenities != null && !amenities.isEmpty()) {
            List<String> lowerCaseAmenities = amenities.stream()
                    .map(String::toLowerCase)
                    .toList();
            List<Hotel> byAmenities = hotelRepository.findByAmenities(lowerCaseAmenities);
            hotels = hotels == null ? new HashSet<>(byAmenities) : intersection(hotels, byAmenities);
        }

        if (hotels == null) {
            hotels = new HashSet<>(hotelRepository.findAll());
        }

        return hotelMapper.toShortResponseDtoList(new ArrayList<>(hotels));
    }

    @Override
    public Map<String, Integer> getHistogram(String param) {
        List<Object[]> results;
        switch (param.toLowerCase()) {
            case "brand":
                results = hotelRepository.getBrandHistogram();
                break;
            case "city":
                results = hotelRepository.getCityHistogram();
                break;
            case "country":
                results = hotelRepository.getCountryHistogram();
                break;
            case "amenities":
                results = hotelRepository.getAmenitiesHistogram();
                break;
            default:
                throw new IllegalArgumentException("Unsupported histogram param: " + param);
        }
        Map<String, Integer> histogram = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = row[0] != null ? row[0].toString() : "Unknown";
            Integer value = row[1] != null ? ((Number)row[1]).intValue() : 0;
            histogram.put(key, value);
        }
        return histogram;
    }

    private Set<Hotel> intersection(Set<Hotel> hotels, List<Hotel> filterResult) {
        hotels.retainAll(filterResult);
        return hotels;
    }

    @Override
    public HotelShortResponseDto createHotel(HotelCreateRequestDto request) {
        Hotel hotel = hotelMapper.toEntity(request);
        hotel = hotelRepository.save(hotel);
        return hotelMapper.toShortResponseDto(hotel);
    }

    @Override
    public void addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NoSuchElementException("Hotel not found"));
        Set<Amenity> amenityEntities = amenities.stream()
                .map(name -> amenityRepository.findByName(name)
                        .orElseGet(() -> amenityRepository.save(new Amenity(null, name))))
                .collect(Collectors.toSet());
        hotel.getAmenities().addAll(amenityEntities);
        hotelRepository.save(hotel);
    }
}
