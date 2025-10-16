package com.workflex.demonic.service;

import com.workflex.demonic.exception.WorkationNotFoundException;
import com.workflex.demonic.model.Risk;
import com.workflex.demonic.model.Workation;
import com.workflex.demonic.repository.WorkationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class WorkationService {

    private final WorkationRepository repository;

    public WorkationService(WorkationRepository repository) {
        this.repository = repository;
    }

    public Page<Workation> getAllWorkations(
            Pageable pageable,
            String employee,
            String country,
            String countryDest,
            Risk risk
    ) {
        Specification<Workation> spec = Specification.where(null);

        if (employee != null && !employee.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("employee")), "%" + employee.toLowerCase() + "%"));
        }

        if (country != null && !country.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%"));
        }

        if (countryDest != null && !countryDest.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("country_dest")), "%" + countryDest.toLowerCase() + "%"));
        }

        if (risk != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("risk"), risk));
        }

        return repository.findAll(spec, pageable);
    }

    public Workation getWorkationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new WorkationNotFoundException(id));
    }

    public Workation createWorkation(Workation workation) {
        return repository.save(workation);
    }

    public Workation updateWorkation(Long id, Workation updatedWorkation) {
        Workation workation = repository.findById(id)
                .orElseThrow(() -> new WorkationNotFoundException(id));
        
        workation.setEmployee(updatedWorkation.getEmployee());
        workation.setCountry(updatedWorkation.getCountry());
        workation.setDays(updatedWorkation.getDays());
        
        return repository.save(workation);
    }

    public void deleteWorkation(Long id) {
        if (!repository.existsById(id)) {
            throw new WorkationNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
