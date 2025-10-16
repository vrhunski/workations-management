package com.workflex.demonic.dto;

import com.workflex.demonic.model.Risk;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkationRequestDto {

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 100, message = "Employee name must be between 2 and 100 characters")
    private String employee;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    private String country;

    @NotBlank(message = "Destination country is required")
    @Size(min = 2, max = 100, message = "Destination country name must be between 2 and 100 characters")
    private String countryDest;

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    @NotNull(message = "Trip duration is required")
    @Min(value = 1, message = "Trip duration must be at least 1 day")
    @Max(value = 365, message = "Trip duration cannot exceed 365 days")
    private Integer days;

    @NotNull(message = "Risk level is required")
    private Risk risk;
}