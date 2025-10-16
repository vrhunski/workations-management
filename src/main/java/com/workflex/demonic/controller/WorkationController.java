package com.workflex.demonic.controller;

import com.workflex.demonic.dto.WorkationMapper;
import com.workflex.demonic.dto.WorkationRequestDto;
import com.workflex.demonic.dto.WorkationResponseDto;
import com.workflex.demonic.model.Risk;
import com.workflex.demonic.service.WorkationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workations")
@CrossOrigin(origins = "*")
public class WorkationController {

    private final WorkationService service;
    private final WorkationMapper mapper;

    public WorkationController(WorkationService service, WorkationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<WorkationResponseDto> getAllWorkations(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection,
            @RequestParam(name = "employee", required = false) String employee,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "countryDest", required = false) String countryDest,
            @RequestParam(name = "risk", required = false) Risk risk
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return service.getAllWorkations(pageable, employee, country, countryDest, risk)
                .map(mapper::toResponseDto);
    }

    @GetMapping("/{id}")
    public WorkationResponseDto getWorkationById(@PathVariable Long id) {
        return mapper.toResponseDto(service.getWorkationById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkationResponseDto createWorkation(@Valid @RequestBody WorkationRequestDto requestDto) {
        return mapper.toResponseDto(service.createWorkation(mapper.toEntity(requestDto)));
    }

    @PutMapping("/{id}")
    public WorkationResponseDto updateWorkation(@PathVariable Long id, @Valid @RequestBody WorkationRequestDto requestDto) {
        return mapper.toResponseDto(service.updateWorkation(id, mapper.toEntity(requestDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkation(@PathVariable Long id) {
        service.deleteWorkation(id);
    }
}
