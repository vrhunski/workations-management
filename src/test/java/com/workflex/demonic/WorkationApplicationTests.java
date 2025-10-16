package com.workflex.demonic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workflex.demonic.dto.WorkationRequestDto;
import com.workflex.demonic.model.Risk;
import com.workflex.demonic.model.Workation;
import com.workflex.demonic.repository.WorkationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkationApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WorkationRepository workationRepository;

    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        workationRepository.deleteAll();
    }

    // ==================== GET ALL WORKATIONS ====================

    @Test
    void testGetAllWorkations_Success() throws Exception {
        // Given
        createTestWorkation("John Doe", "United States", "Portugal", 75);
        createTestWorkation("Jane Smith", "Germany", "Spain", 85);

        // When & Then
        mockMvc.perform(get("/api/v1/workations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].employee").exists())
                .andExpect(jsonPath("$.content[0].country").exists());
    }

    @Test
    void testGetAllWorkations_WithPagination() throws Exception {
        // Given
        for (int i = 1; i <= 15; i++) {
            createTestWorkation("Employee " + i, "Country " + i, "Destination " + i, 50 + i);
        }

        // When & Then - First page
        mockMvc.perform(get("/api/v1/workations")
                        .param("page", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));

        // Second page
        mockMvc.perform(get("/api/v1/workations")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.first").value(false))
                .andExpect(jsonPath("$.last").value(false));
    }

    @Test
    void testGetAllWorkations_WithSorting() throws Exception {
        // Given
        createTestWorkation("Zack", "USA", "Canada", 30);
        createTestWorkation("Alice", "UK", "France", 40);
        createTestWorkation("Bob", "Germany", "Italy", 50);

        // When & Then - Sort by employee ASC
        mockMvc.perform(get("/api/v1/workations")
                        .param("sortBy", "employee")
                        .param("sortDirection", "ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employee").value("Alice"))
                .andExpect(jsonPath("$.content[1].employee").value("Bob"))
                .andExpect(jsonPath("$.content[2].employee").value("Zack"));

        // Sort by employee DESC
        mockMvc.perform(get("/api/v1/workations")
                        .param("sortBy", "employee")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employee").value("Zack"))
                .andExpect(jsonPath("$.content[1].employee").value("Bob"))
                .andExpect(jsonPath("$.content[2].employee").value("Alice"));
    }


    @Test
    void testGetAllWorkations_WithRiskFilter() throws Exception {
        // Given
        createTestWorkation("Low Risk Person", "USA", "Canada", 60); // LOW_RISK
        createTestWorkation("High Risk Person", "UK", "Australia", 120); // HIGH_RISK
        createTestWorkation("No Risk Person", "Germany", "France", 30); // NO_RISK

        // Filter by risk level
        mockMvc.perform(get("/api/v1/workations")
                        .param("risk", "HIGH_RISK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].risk").value("HIGH_RISK"));
    }

    @Test
    void testGetAllWorkations_EmptyResult() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/workations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.empty").value(true));
    }

    // ==================== GET WORKATION BY ID ====================

    @Test
    void testGetWorkationById_Success() throws Exception {
        // Given
        Workation saved = createTestWorkation("John Doe", "USA", "Portugal", 75);

        // When & Then
        mockMvc.perform(get("/api/v1/workations/{id}", saved.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.employee").value("John Doe"))
                .andExpect(jsonPath("$.country").value("USA"))
                .andExpect(jsonPath("$.countryDest").value("Portugal"))
                .andExpect(jsonPath("$.days").value(75));
    }

    @Test
    void testGetWorkationById_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/workations/{id}", 99999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ==================== CREATE WORKATION ====================

    @Test
    void testCreateWorkation_Success() throws Exception {
        // Given
        WorkationRequestDto request = createWorkationRequest(
                "New Employee", "France", "Italy",
                "2025-06-01", "2025-08-15", 75, Risk.LOW_RISK);

        // When & Then
        MvcResult result = mockMvc.perform(post("/api/v1/workations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.employee").value("New Employee"))
                .andExpect(jsonPath("$.country").value("France"))
                .andExpect(jsonPath("$.countryDest").value("Italy"))
                .andExpect(jsonPath("$.days").value(75))
                .andExpect(jsonPath("$.risk").value("LOW_RISK"))
                .andReturn();

        // Verify in database
        long count = workationRepository.count();
        assertEquals(1, count);
    }

    @Test
    void testCreateWorkation_ValidationError_EmptyEmployee() throws Exception {
        // Given
        WorkationRequestDto request = createWorkationRequest(
                "", "France", "Italy",
                "2025-06-01", "2025-08-15", 75, Risk.LOW_RISK);

        // When & Then
        mockMvc.perform(post("/api/v1/workations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateWorkation_ValidationError_InvalidDays() throws Exception {
        // Given - Days exceeds maximum (365)
        WorkationRequestDto request = createWorkationRequest(
                "John Doe", "USA", "Canada",
                "2025-01-01", "2026-02-01", 400, Risk.HIGH_RISK);

        // When & Then
        mockMvc.perform(post("/api/v1/workations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateWorkation_ValidationError_ShortEmployeeName() throws Exception {
        // Given - Employee name too short
        WorkationRequestDto request = createWorkationRequest(
                "A", "France", "Italy",
                "2025-06-01", "2025-08-15", 75, Risk.LOW_RISK);

        // When & Then
        mockMvc.perform(post("/api/v1/workations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ==================== UPDATE WORKATION ====================

    @Test
    void testUpdateWorkation_NotFound() throws Exception {
        // Given
        WorkationRequestDto updateRequest = createWorkationRequest(
                "Non Existent", "USA", "Canada",
                "2025-06-01", "2025-08-15", 75, Risk.LOW_RISK);

        // When & Then
        mockMvc.perform(put("/api/v1/workations/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWorkation_ValidationError() throws Exception {
        // Given
        Workation existing = createTestWorkation("Old Name", "USA", "Canada", 50);

        WorkationRequestDto updateRequest = createWorkationRequest(
                "", "USA", "Mexico",
                "2025-07-01", "2025-09-15", 76, Risk.LOW_RISK);

        // When & Then
        mockMvc.perform(put("/api/v1/workations/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ==================== DELETE WORKATION ====================

    @Test
    void testDeleteWorkation_Success() throws Exception {
        // Given
        Workation workation = createTestWorkation("To Delete", "USA", "Canada", 50);
        Long id = workation.getId();

        // When & Then
        mockMvc.perform(delete("/api/v1/workations/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify deleted from database
        assertFalse(workationRepository.existsById(id));
    }

    @Test
    void testDeleteWorkation_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/workations/{id}", 99999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ==================== COMPLEX SCENARIOS ====================

    @Test
    void testComplexQuery_PaginationSortingAndFiltering() throws Exception {
        // Given
        createTestWorkation("Alice Johnson", "United States", "France", 45);
        createTestWorkation("Bob Smith", "United States", "Spain", 60);
        createTestWorkation("Charlie Brown", "United States", "Italy", 110);
        createTestWorkation("David Wilson", "Canada", "Mexico", 30);

        // When & Then - Filter by country, sort by days DESC, paginate
        mockMvc.perform(get("/api/v1/workations")
                        .param("country", "United States")
                        .param("sortBy", "days")
                        .param("sortDirection", "DESC")
                        .param("page", "0")
                        .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].days").value(110))
                .andExpect(jsonPath("$.content[1].days").value(60))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void testCRUDLifecycle() throws Exception {
        // 1. Create
        WorkationRequestDto createRequest = createWorkationRequest(
                "Lifecycle Test", "USA", "UK",
                "2025-05-01", "2025-07-15", 75, Risk.LOW_RISK);

        MvcResult createResult = mockMvc.perform(post("/api/v1/workations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        Long createdId = objectMapper.readTree(createResponse).get("id").asLong();

        // 2. Read
        mockMvc.perform(get("/api/v1/workations/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee").value("Lifecycle Test"));

        // 3. Update
        WorkationRequestDto updateRequest = createWorkationRequest(
                "Lifecycle Test Updated", "USA", "France",
                "2025-05-01", "2025-08-01", 92, Risk.LOW_RISK);

        mockMvc.perform(put("/api/v1/workations/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee").value("Lifecycle Test Updated"));

        // 4. Delete
        mockMvc.perform(delete("/api/v1/workations/{id}", createdId))
                .andExpect(status().isNoContent());

        // 5. Verify deletion
        mockMvc.perform(get("/api/v1/workations/{id}", createdId))
                .andExpect(status().isNotFound());
    }

    // ==================== HELPER METHODS ====================

    private Workation createTestWorkation(String employee, String country, String countryDest, int days) {
        try {
            Workation workation = new Workation();
            workation.setEmployee(employee);
            workation.setCountry(country);
            workation.setCountry_dest(countryDest);
            workation.setStart_date(dateFormat.parse("2025-03-01"));
            workation.setEnd_date(dateFormat.parse("2025-05-15"));
            workation.setDays(days);

            // Set risk based on days
            if (days <= 50) {
                workation.setRisk(Risk.NO_RISK);
            } else if (days <= 100) {
                workation.setRisk(Risk.LOW_RISK);
            } else {
                workation.setRisk(Risk.HIGH_RISK);
            }

            return workationRepository.save(workation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test workation", e);
        }
    }

    private WorkationRequestDto createWorkationRequest(
            String employee, String country, String countryDest,
            String startDate, String endDate, int days, Risk risk) {

        WorkationRequestDto dto = new WorkationRequestDto();
        dto.setEmployee(employee);
        dto.setCountry(country);
        dto.setCountryDest(countryDest);

        try {
            dto.setStartDate(dateFormat.parse(startDate));
            dto.setEndDate(dateFormat.parse(endDate));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse dates", e);
        }

        dto.setDays(days);
        dto.setRisk(risk);

        return dto;
    }
}