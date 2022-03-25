package com.example.thepiratescodingtest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String productDesc;

    private int minPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Option> optionList;

    // 연관관계 주인이 아닌쪽에서 호출해도 lazy로 로딩
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

}
