package com.example.thepiratescodingtest;

import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.entity.Delivery;
import com.example.thepiratescodingtest.entity.Product;
import com.example.thepiratescodingtest.entity.Type;
import com.example.thepiratescodingtest.repository.DeliveryRepository;
import com.example.thepiratescodingtest.repository.OptionRepository;
import com.example.thepiratescodingtest.repository.ProductRepository;
import com.example.thepiratescodingtest.service.MainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class holidayTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    OptionRepository optionRepository;
    @Mock
    DeliveryRepository deliveryRepository;

    MainService mainService;

    private Product productFast;
    private Product productRegular;
    private Delivery deliveryFast;
    private Delivery deliveryRegular;

    @BeforeEach
    void setup() {
        deliveryFast = Delivery.builder()
                .type(Type.FAST)
                .closingTime(LocalTime.NOON)
                .price(10000)
                .build();

        deliveryRegular = Delivery.builder()
                .type(Type.REGULAR)
                .closingTime(LocalTime.NOON)
                .price(10000)
                .build();

        productFast = Product.builder()
                .id(1L)
                .productName("고등어")
                .productDesc("갓 잡은 싱싱한 고등어")
                .min_price(10000)
                .delivery(deliveryFast)
                .build();

        productRegular = Product.builder()
                .id(2L)
                .productName("갈치")
                .productDesc("갓 잡은 싱싱한 갈치")
                .min_price(11000)
                .delivery(deliveryRegular)
                .build();


    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/13 일요일)")
    void holiday1(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);


        System.out.println("3/13 일요일 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 13, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 15일 화요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/13 일요일 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 13, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 20일 일요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/12 토요일)")
    void holiday2(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/12 토요일 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 12, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 15일 화요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/12 토요일 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 12, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 20일 일요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/11 금요일, 마감시간 전)")
    void holiday3(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/11 금요일, 마감시간 전 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 11, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 12일 토요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/11 금요일, 마감시간 전 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 11, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 15일 화요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/11 금요일, 마감시간 후)")
    void holiday4(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/11 금요일, 마감시간 후 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 11, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 15일 화요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/11 금요일, 마감시간 후 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 11, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 20일 일요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/10 목요일, 마감시간 전)")
    void holiday5(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/10 목요일, 마감시간 전 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 10, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 11일 금요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/10 목요일, 마감시간 전 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 10, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/10 목요일, 마감시간 후)")
    void holiday6(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/10 목요일, 마감시간 후 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 10, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 12일 토요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/10 목요일, 마감시간 후 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 10, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 15일 화요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 16일 수요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 17일 목요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 18일 금요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 19일 토요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/9 수요일, 마감시간 전)")
    void holiday7(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/9 수요일, 마감시간 전 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 9, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 10일 목요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/9 수요일, 마감시간 전 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 9, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 11일 금요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/9 수요일, 마감시간 후)")
    void holiday8(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/9 수요일, 마감시간 후 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 9, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
//        assertEquals("03월 11일 금요일", dateResponseDtos.get(0).getDate());
//        assertEquals("03월 12일 토요일", dateResponseDtos.get(1).getDate());
//        assertEquals("03월 13일 일요일", dateResponseDtos.get(2).getDate());
//        assertEquals("03월 14일 월요일", dateResponseDtos.get(3).getDate());
//        assertEquals("03월 15일 화요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/9 수요일, 마감시간 후 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 9, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
//        assertEquals("03월 14일 월요일", dateResponseDtos2.get(0).getDate());
//        assertEquals("03월 15일 화요일", dateResponseDtos2.get(1).getDate());
//        assertEquals("03월 16일 수요일", dateResponseDtos2.get(2).getDate());
//        assertEquals("03월 17일 목요일", dateResponseDtos2.get(3).getDate());
//        assertEquals("03월 18일 금요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/8 화요일, 마감시간 전)")
    void holiday9(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/8 화요일, 마감시간 전 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 8, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 09일 수요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 10일 목요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/8 화요일, 마감시간 전 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 8, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 10일 목요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/8 화요일, 마감시간 후)")
    void holiday10(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("배송일: 3/8 화요일, 마감시간 후 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 8, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 10일 목요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("배송일: 3/8 화요일, 마감시간 후 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 8, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 11일 금요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 15일 화요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/7 월요일, 마감시간 전)")
    void holiday11(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/7 월요일, 마감시간 전 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 7, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 08일 화요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 09일 수요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 10일 목요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/7 월요일, 마감시간 전 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 7, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 09일 수요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 10일 목요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 3/7 월요일, 마감시간 후)")
    void holiday12(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/7 월요일, 마감시간 후 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 7, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 09일 수요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 10일 목요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/7 월요일, 마감시간 후 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 7, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 10일 목요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 11일 금요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 12일 토요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 13일 일요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 14일 월요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(배송일: 10/10 개천절 대체공휴일)")
    void holiday13(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("10/10 개천절 대체공휴일 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 10, 10, 11, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("10월 12일 수요일", dateResponseDtos.get(0).getDate());
        assertEquals("10월 13일 목요일", dateResponseDtos.get(1).getDate());
        assertEquals("10월 14일 금요일", dateResponseDtos.get(2).getDate());
        assertEquals("10월 15일 토요일", dateResponseDtos.get(3).getDate());
        assertEquals("10월 16일 일요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("10/10 개천절 대체공휴일 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 10, 10, 11, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("10월 13일 목요일", dateResponseDtos2.get(0).getDate());
        assertEquals("10월 14일 금요일", dateResponseDtos2.get(1).getDate());
        assertEquals("10월 15일 토요일", dateResponseDtos2.get(2).getDate());
        assertEquals("10월 16일 일요일", dateResponseDtos2.get(3).getDate());
        assertEquals("10월 17일 월요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜( 3/1 삼일절 일반공휴일)")
    void holiday14(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("3/1 삼일절 일반공휴일 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 3, 1, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 03일 목요일", dateResponseDtos.get(0).getDate());
        assertEquals("03월 04일 금요일", dateResponseDtos.get(1).getDate());
        assertEquals("03월 05일 토요일", dateResponseDtos.get(2).getDate());
        assertEquals("03월 06일 일요일", dateResponseDtos.get(3).getDate());
        assertEquals("03월 07일 월요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("3/1 삼일절 일반공휴일 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 3, 1, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("03월 04일 금요일", dateResponseDtos2.get(0).getDate());
        assertEquals("03월 05일 토요일", dateResponseDtos2.get(1).getDate());
        assertEquals("03월 06일 일요일", dateResponseDtos2.get(2).getDate());
        assertEquals("03월 07일 월요일", dateResponseDtos2.get(3).getDate());
        assertEquals("03월 08일 화요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }

    @Test
    @DisplayName("일반 날짜(9/9 추석)")
    void holiday15(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println("9/9 추석 당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 9, 9, 14, 0));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }
        assertEquals("09월 14일 수요일", dateResponseDtos.get(0).getDate());
        assertEquals("09월 15일 목요일", dateResponseDtos.get(1).getDate());
        assertEquals("09월 16일 금요일", dateResponseDtos.get(2).getDate());
        assertEquals("09월 17일 토요일", dateResponseDtos.get(3).getDate());
        assertEquals("09월 18일 일요일", dateResponseDtos.get(4).getDate());
        System.out.println();

        System.out.println("9/9 추석 익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 9, 9, 14, 0));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
        assertEquals("09월 15일 목요일", dateResponseDtos2.get(0).getDate());
        assertEquals("09월 16일 금요일", dateResponseDtos2.get(1).getDate());
        assertEquals("09월 17일 토요일", dateResponseDtos2.get(2).getDate());
        assertEquals("09월 18일 일요일", dateResponseDtos2.get(3).getDate());
        assertEquals("09월 19일 월요일", dateResponseDtos2.get(4).getDate());
        System.out.println();
    }
}
