package com.parking.nl.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "street")
@Entity
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "street_id")
    private Long streetId;

    @Column(name = "street_name", unique = true)
    private String name;

    @Column(name = "price_per_min")
    private BigDecimal pricePerMinute;


}

