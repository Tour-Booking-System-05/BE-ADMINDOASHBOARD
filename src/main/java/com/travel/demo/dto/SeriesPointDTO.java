package com.travel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SeriesPointDTO {
    private String label; // T1..T12
    private long value;
}
