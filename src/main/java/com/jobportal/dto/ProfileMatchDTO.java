package com.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMatchDTO {
    private double skillMatchPercentage;
    private double educationMatchPercentage;
    private double overallMatchPercentage;
}
