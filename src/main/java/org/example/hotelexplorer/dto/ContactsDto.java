package org.example.hotelexplorer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactsDto {

    @NotBlank
    @Size(max = 50)
    private String phone;

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;
}