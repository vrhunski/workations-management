package com.workflex.demonic.service;

import com.workflex.demonic.model.Risk;
import com.workflex.demonic.model.Workation;
import com.workflex.demonic.repository.WorkationRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class DataInitializer {

    private final WorkationRepository workationRepository;

    public DataInitializer(WorkationRepository workationRepository) {
        this.workationRepository = workationRepository;
    }

    @PostConstruct
    public void init() {
        if (workationRepository.count() > 0) {
            log.info("Database already contains {} workations. Skipping CSV import.", workationRepository.count());
            return;
        }

        log.info("Initializing database with CSV data...");
        loadCsvData();
    }

    private void loadCsvData() {
        try {
            ClassPathResource resource = new ClassPathResource("workations.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String line;
            int lineNumber = 0;
            int importedCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header
                if (lineNumber == 1) {
                    continue;
                }

                String[] fields = line.split(",");

                if (fields.length < 8) {
                    log.warn("Line {}: Invalid format - skipping", lineNumber);
                    continue;
                }

                try {
                    Workation workation = new Workation();
                    workation.setEmployee(fields[1].trim());
                    workation.setCountry(fields[2].trim());
                    workation.setCountry_dest(fields[3].trim());
                    workation.setStart_date(dateFormat.parse(fields[4].trim()));
                    workation.setEnd_date(dateFormat.parse(fields[5].trim()));
                    workation.setDays(Integer.parseInt(fields[6].trim()));
                    workation.setRisk(Risk.valueOf(fields[7].trim()));

                    workationRepository.save(workation);
                    importedCount++;
                } catch (Exception e) {
                    log.error("Line {}: Failed to parse - {}", lineNumber, e.getMessage());
                }
            }

            reader.close();
            log.info("Successfully imported {} workations from CSV", importedCount);
        } catch (Exception e) {
            log.error("Failed to load CSV data: {}", e.getMessage(), e);
        }
    }
}