package com.example.thepiratescodingtest.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductRequestDto {

    private String name;
    private String description;
    private DeliveryRequestDto delivery;
    private List<OptionRequestDto> options;
}
