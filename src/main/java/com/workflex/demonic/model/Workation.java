package com.workflex.demonic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workation")
public class Workation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employee;
    private String country;
    private String country_dest;
    private Date start_date;
    private Date end_date;
    private int days;
    @Enumerated(EnumType.STRING)
    @Column(name = "risk")
    private Risk risk;
}