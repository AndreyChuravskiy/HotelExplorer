package org.example.hotelexplorer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDto {

    @NotNull
    private Integer houseNumber;

    @NotBlank
    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    private String country;

    @NotBlank
    @Size(max = 20)
    private String postCode;
}