package com.workflex.demonic.dto;

import com.workflex.demonic.model.Risk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkationResponseDto {

    private Long id;
    private String employee;
    private String country;
    private String countryDest;
    private Date startDate;
    private Date endDate;
    private int days;
    private Risk risk;
}
