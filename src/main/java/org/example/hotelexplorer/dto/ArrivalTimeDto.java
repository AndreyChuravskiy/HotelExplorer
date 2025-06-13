package org.example.hotelexplorer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ArrivalTimeDto {

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOut;
}