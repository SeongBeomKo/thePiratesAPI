package com.example.thepiratescodingtest.dto.request;

import com.example.thepiratescodingtest.exception.PostValidationGroup;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class ProductRequestDto {

    @NotBlank(message = "상품 이름은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String name;
    @NotBlank(message = "상품 설명은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String description;
    @Valid
    private DeliveryRequestDto delivery;
    @Valid
    private List<OptionRequestDto> options;
}
