package com.example.thepiratescodingtest.dto.request;

import com.example.thepiratescodingtest.exception.PostValidationGroup;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Valid
public class DeliveryRequestDto {

    @NotBlank(message = "배송 타입은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String type;
    @NotBlank(message = "마감 시간은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String closing;
    private int price;
}
