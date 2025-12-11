package com.travel.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseOverViewReportDTO {
    private BigDecimal value;
    private Double percent;
}
