package com.travel.demo.dto;

import lombok.Data;

@Data
public class OverviewReportDTO {
    BaseOverViewReportDTO revenue;
    BaseOverViewReportDTO orderTotal;
    BaseOverViewReportDTO customerTotal;
    BaseOverViewReportDTO tourCancelRate;
}