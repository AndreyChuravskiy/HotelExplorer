package org.example.hotelexplorer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HotelCreateRequestDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @NotBlank
    @Size(max = 100)
    private String brand;

    @NotNull
    @Valid
    private AddressDto address;

    @NotNull
    @Valid
    private ContactsDto contacts;

    @NotNull
    @Valid
    private ArrivalTimeDto arrivalTime;
}