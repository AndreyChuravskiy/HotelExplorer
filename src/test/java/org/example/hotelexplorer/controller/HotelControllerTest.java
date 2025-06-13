package org.example.hotelexplorer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelexplorer.dto.*;
import org.example.hotelexplorer.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
@Import(HotelControllerTest.Config.class)
class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.context.TestConfiguration
    static class Config {
        @Bean
        HotelService hotelService() {
            return mock(HotelService.class);
        }

    }

    @BeforeEach
    void setUp() {
        hotelService = Mockito.mock(HotelService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new HotelController(hotelService)).build();
    }

    @Test
    @DisplayName("GET /property-view/hotels returns list of hotels")
    void getAllHotels() throws Exception {
        HotelShortResponseDto dto = new HotelShortResponseDto();
        dto.setId(1L);
        dto.setName("Hotel1");
        dto.setDescription("Desc");
        dto.setAddress("Some address");
        dto.setPhone("+1234567890");

        when(hotelService.getAllHotels()).thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hotel1"));
    }

    @Test
    @DisplayName("GET /property-view/hotels/{id} returns hotel by id")
    void getHotelById() throws Exception {
        HotelFullResponseDto dto = new HotelFullResponseDto();
        dto.setId(1L);
        dto.setName("Hotel1");
        dto.setDescription("Desc");
        dto.setBrand("BrandA");
        dto.setAddress(new AddressDto());
        dto.setContacts(new ContactsDto());
        dto.setArrivalTime(new ArrivalTimeDto());
        dto.setAmenities(List.of("wifi"));

        when(hotelService.getHotelById(1L)).thenReturn(dto);

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hotel1"))
                .andExpect(jsonPath("$.amenities[0]").value("wifi"));
    }

    @Test
    @DisplayName("POST /property-view/hotels creates hotel")
    void createHotel() throws Exception {
        HotelCreateRequestDto req = new HotelCreateRequestDto();
        req.setName("HotelX");
        req.setBrand("BrandY");
        AddressDto address = new AddressDto();
        address.setHouseNumber(10);
        address.setStreet("Main St");
        address.setCity("CityX");
        address.setCountry("CountryX");
        address.setPostCode("12345");
        req.setAddress(address);
        ContactsDto contacts = new ContactsDto();
        contacts.setPhone("+123456789");
        contacts.setEmail("mail@test.com");
        req.setContacts(contacts);
        ArrivalTimeDto arrivalTime = new ArrivalTimeDto();
        arrivalTime.setCheckIn(LocalTime.of(14, 0));
        arrivalTime.setCheckOut(LocalTime.of(12, 0));
        req.setArrivalTime(arrivalTime);

        HotelShortResponseDto resp = new HotelShortResponseDto();
        resp.setId(100L);
        resp.setName("HotelX");
        resp.setDescription(null);
        resp.setAddress("Main St, CityX");
        resp.setPhone("+123456789");

        when(hotelService.createHotel(ArgumentMatchers.any())).thenReturn(resp);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("HotelX"))
                .andExpect(jsonPath("$.phone").value("+123456789"));
    }

    @Test
    @DisplayName("GET /property-view/search with params calls searchHotels")
    void searchHotels_withParams() throws Exception {
        HotelShortResponseDto dto = new HotelShortResponseDto();
        dto.setId(2L);
        dto.setName("SearchHotel");
        dto.setDescription("SearchDesc");
        dto.setAddress("SearchAddress");
        dto.setPhone("+987654321");
        when(hotelService.searchHotels(eq("n"), eq("b"), eq("c"), eq("co"), eq(List.of("wifi")))).thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/search")
                        .param("name", "n")
                        .param("brand", "b")
                        .param("city", "c")
                        .param("country", "co")
                        .param("amenities", "wifi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    @DisplayName("POST /property-view/hotels/{id}/amenities calls addAmenities")
    void addAmenities() throws Exception {
        doNothing().when(hotelService).addAmenities(eq(5L), eq(List.of("pool", "spa")));

        mockMvc.perform(post("/property-view/hotels/5/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of("pool", "spa"))))
                .andExpect(status().isOk());

        verify(hotelService, times(1)).addAmenities(eq(5L), eq(List.of("pool", "spa")));
    }

    @Test
    @DisplayName("GET /property-view/histogram/{param} returns histogram")
    void getHistogram() throws Exception {
        when(hotelService.getHistogram("city")).thenReturn(Map.of("CityX", 3));

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CityX").value(3));
    }
}