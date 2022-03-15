package com.example.thepiratescodingtest.controller;

import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.dto.request.ProductRequestDto;
import com.example.thepiratescodingtest.dto.response.ProductInfoResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductResponseDto;
import com.example.thepiratescodingtest.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts() {
        return mainService.getAllProducts();
    }

    @PostMapping("/newProduct")
    public void uploadNewProduct(@RequestBody ProductRequestDto productReqeustDto) {
        mainService.uploadNewProduct(productReqeustDto);
    }

    @DeleteMapping("/product/{id}")
    public void deleteProduct(@PathVariable Long id) {
        mainService.deleteProduct(id);
    }

    @GetMapping("/product/{id}")
    public ProductInfoResponseDto getProductInfo(@PathVariable Long id) {
        return mainService.getProductInfo(id);
    }

    @GetMapping("/product/{id}/deliveryDates")
    public List<DateResponseDto> getDeliveryDates(@PathVariable Long id) {
        return mainService.getDeliveryDates(id);
    }
}
