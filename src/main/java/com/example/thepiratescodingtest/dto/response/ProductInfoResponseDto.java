package com.example.thepiratescodingtest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductInfoResponseDto {

    private String name;
    private String description;
    private String delivery;
    List<OptionResponseDto> options;
}
