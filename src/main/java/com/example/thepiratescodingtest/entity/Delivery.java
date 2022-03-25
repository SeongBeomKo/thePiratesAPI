package com.example.thepiratescodingtest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Delivery extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int price;

    private LocalTime closingTime;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery", optional = false)
    private Product product;
}
