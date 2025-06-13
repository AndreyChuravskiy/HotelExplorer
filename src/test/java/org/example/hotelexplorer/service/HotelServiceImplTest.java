package org.example.hotelexplorer.service;

import org.example.hotelexplorer.dto.*;
import org.example.hotelexplorer.entity.Amenity;
import org.example.hotelexplorer.entity.Hotel;
import org.example.hotelexplorer.mapper.HotelMapper;
import org.example.hotelexplorer.repository.AmenityRepository;
import org.example.hotelexplorer.repository.HotelRepository;
import org.example.hotelexplorer.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private AmenityRepository amenityRepository;
    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllHotels_returnsShortResponseDtoList() {
        List<Hotel> hotels = List.of(new Hotel(), new Hotel());
        List<HotelShortResponseDto> dtos = List.of(new HotelShortResponseDto(), new HotelShortResponseDto());

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toShortResponseDtoList(hotels)).thenReturn(dtos);

        assertThat(hotelService.getAllHotels()).isEqualTo(dtos);
        verify(hotelRepository).findAll();
        verify(hotelMapper).toShortResponseDtoList(hotels);
    }

    @Test
    void getHotelById_found_returnsFullDto() {
        Hotel hotel = new Hotel();
        HotelFullResponseDto dto = new HotelFullResponseDto();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toFullResponseDto(hotel)).thenReturn(dto);

        assertThat(hotelService.getHotelById(1L)).isEqualTo(dto);
        verify(hotelRepository).findById(1L);
        verify(hotelMapper).toFullResponseDto(hotel);
    }

    @Test
    void getHotelById_notFound_throwsException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.getHotelById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Hotel not found");
    }

    @Test
    void searchHotels_byName_andBrand_andCity_andCountry_andAmenities() {
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        List<Hotel> foundByName = List.of(hotel1, hotel2);
        List<Hotel> foundByBrand = List.of(hotel1);
        List<Hotel> foundByCity = List.of(hotel1);
        List<Hotel> foundByCountry = List.of(hotel1);
        List<Hotel> foundByAmenities = List.of(hotel1);

        when(hotelRepository.findByNameContainingIgnoreCase("test")).thenReturn(foundByName);
        when(hotelRepository.findByBrandContainingIgnoreCase("brand")).thenReturn(foundByBrand);
        when(hotelRepository.findByCity("city")).thenReturn(foundByCity);
        when(hotelRepository.findByCountry("country")).thenReturn(foundByCountry);
        when(hotelRepository.findByAmenities(List.of("wifi"))).thenReturn(foundByAmenities);

        List<HotelShortResponseDto> dtos = List.of(new HotelShortResponseDto());
        when(hotelMapper.toShortResponseDtoList(anyList())).thenReturn(dtos);

        List<HotelShortResponseDto> result = hotelService.searchHotels("test", "brand", "city", "country", List.of("wifi"));
        assertThat(result).isEqualTo(dtos);
    }

    @Test
    void searchHotels_noFilters_returnsAll() {
        List<Hotel> hotels = List.of(new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<HotelShortResponseDto> dtos = List.of(new HotelShortResponseDto());
        when(hotelMapper.toShortResponseDtoList(hotels)).thenReturn(dtos);

        assertThat(hotelService.searchHotels(null, null, null, null, null)).isEqualTo(dtos);
    }

    @Test
    void getHistogram_brand() {
        when(hotelRepository.getBrandHistogram()).thenReturn(List.of(new Object[]{"BrandA", 2}, new Object[]{null, 1}));
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("BrandA", 2);
        expected.put("Unknown", 1);

        assertThat(hotelService.getHistogram("brand")).isEqualTo(expected);
    }

    @Test
    void getHistogram_city() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"CityX", 3});
        when(hotelRepository.getCityHistogram()).thenReturn(list);
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("CityX", 3);

        assertThat(hotelService.getHistogram("city")).isEqualTo(expected);
    }

    @Test
    void getHistogram_country() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"CountryY", 4});
        when(hotelRepository.getCountryHistogram()).thenReturn(list);
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("CountryY", 4);

        assertThat(hotelService.getHistogram("country")).isEqualTo(expected);
    }

    @Test
    void getHistogram_amenities() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"wifi", 5});
        when(hotelRepository.getAmenitiesHistogram()).thenReturn(list);
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("wifi", 5);

        assertThat(hotelService.getHistogram("amenities")).isEqualTo(expected);
    }

    @Test
    void getHistogram_unsupportedParam_throws() {
        assertThatThrownBy(() -> hotelService.getHistogram("unsupported"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported histogram param");
    }

    @Test
    void createHotel_savesAndReturnsDto() {
        HotelCreateRequestDto request = new HotelCreateRequestDto();
        Hotel hotel = new Hotel();
        Hotel savedHotel = new Hotel();
        HotelShortResponseDto dto = new HotelShortResponseDto();

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toShortResponseDto(savedHotel)).thenReturn(dto);

        assertThat(hotelService.createHotel(request)).isEqualTo(dto);
    }

    @Test
    void addAmenities_shouldAddAndSave() {
        Long hotelId = 1L;
        Hotel hotel = new Hotel();
        hotel.setAmenities(new HashSet<>());
        Amenity amenity = new Amenity(2L, "wifi");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName("wifi")).thenReturn(Optional.of(amenity));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        hotelService.addAmenities(hotelId, List.of("wifi"));

        assertThat(hotel.getAmenities()).contains(amenity);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_createsNewAmenityIfNotExist() {
        Long hotelId = 1L;
        Hotel hotel = new Hotel();
        hotel.setAmenities(new HashSet<>());
        String amenityName = "pool";
        Amenity newAmenity = new Amenity(3L, amenityName);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName(amenityName)).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(newAmenity);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        hotelService.addAmenities(hotelId, List.of(amenityName));

        assertThat(hotel.getAmenities()).extracting(Amenity::getName).contains(amenityName);
        verify(amenityRepository).save(any(Amenity.class));
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenities_hotelNotFound_throws() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.addAmenities(1L, List.of("wifi")))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Hotel not found");
    }
}