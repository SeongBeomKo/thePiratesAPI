package com.example.thepiratescodingtest.dto.request;

import com.example.thepiratescodingtest.exception.PostValidationGroup;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Valid
public class OptionRequestDto {

    @NotBlank(message = "옵션 이름은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String name;
    private int price;
    private int stock;
}
