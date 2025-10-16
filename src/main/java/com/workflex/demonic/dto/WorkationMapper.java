package com.workflex.demonic.dto;

import com.workflex.demonic.model.Workation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "countryDest", target = "country_dest")
    @Mapping(source = "startDate", target = "start_date")
    @Mapping(source = "endDate", target = "end_date")
    Workation toEntity(WorkationRequestDto dto);

    @Mapping(source = "country_dest", target = "countryDest")
    @Mapping(source = "start_date", target = "startDate")
    @Mapping(source = "end_date", target = "endDate")
    WorkationResponseDto toResponseDto(Workation workation);
}
