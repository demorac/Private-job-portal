package com.jobportal.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certification {
    private String name;
    private String issuer;
    private LocalDate  issueDate;
    private String certificateId;


}
