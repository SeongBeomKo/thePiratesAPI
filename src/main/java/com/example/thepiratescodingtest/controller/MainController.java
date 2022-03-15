package com.example.thepiratescodingtest.controller;

import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.dto.request.ProductRequestDto;
import com.example.thepiratescodingtest.dto.response.ProductInfoResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductResponseDto;
import com.example.thepiratescodingtest.exception.InputValidator;
import com.example.thepiratescodingtest.exception.PostValidationGroup;
import com.example.thepiratescodingtest.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 상품 목록 조회
    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts() {
        return mainService.getAllProducts();
    }

    // 새상품 추가
    @PostMapping("/newProduct")
    public void uploadNewProduct(@RequestBody @Validated(PostValidationGroup.class) ProductRequestDto productReqeustDto,
                                 BindingResult bindingResult) {
        // 빈값이 들어왔는지 유효성 검사
        InputValidator.BadRequestHandler(bindingResult);
        mainService.uploadNewProduct(productReqeustDto);
    }

    // 상품 삭제
    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        mainService.deleteProduct(id);
    }

    // 상품 상세 조회
    @GetMapping("/product/{id}")
    public ProductInfoResponseDto getProductInfo(@PathVariable Long id) {
        return mainService.getProductInfo(id);
    }

    // 상품 수령 가능일 조회
    @GetMapping("/product/{id}/deliveryDates")
    public List<DateResponseDto> getDeliveryDates(@PathVariable Long id) {
        return mainService.getDeliveryDates(id, LocalDateTime.now());
    }
}
