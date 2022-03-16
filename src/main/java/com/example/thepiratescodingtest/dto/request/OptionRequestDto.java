package com.example.thepiratescodingtest.dto.request;

import com.example.thepiratescodingtest.exception.PostValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OptionRequestDto {

    @NotBlank(message = "옵션 이름은 공백일 수 없습니다.", groups = PostValidationGroup.class)
    private String name;
    private int price;
    private int stock;
}
