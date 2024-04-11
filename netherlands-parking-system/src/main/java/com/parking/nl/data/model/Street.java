package com.parking.nl.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "street")
@Entity
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "street_id")
    private Long streetId;

    @Column(name = "street_name", unique = true)
    private String name;

    @Column(name = "price_per_min")
    private int pricePerMinute;


}

