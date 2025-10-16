package com.workflex.demonic.repository;


import com.workflex.demonic.model.Workation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkationRepository extends JpaRepository<Workation, Long>, JpaSpecificationExecutor<Workation> {
}