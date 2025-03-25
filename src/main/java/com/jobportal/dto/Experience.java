package com.jobportal.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience { // ✅ Fix: Renamed from "Experirnce"
    private String title;
    private String company;
    private String location;
    private LocalDate  startDate;
    private LocalDate  endDate;
    private Boolean working;
    private String description;
}
