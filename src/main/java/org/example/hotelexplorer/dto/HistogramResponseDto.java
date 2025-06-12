package org.example.hotelexplorer.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HistogramResponseDto {
    private Map<String, Integer> histogram;
}