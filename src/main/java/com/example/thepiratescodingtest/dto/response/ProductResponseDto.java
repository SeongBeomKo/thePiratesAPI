package com.example.thepiratescodingtest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductResponseDto {

    private String name;
    private String description;
    private String price;
}
