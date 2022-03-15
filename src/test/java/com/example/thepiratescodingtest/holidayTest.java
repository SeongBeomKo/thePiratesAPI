package com.example.thepiratescodingtest;

import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.entity.Delivery;
import com.example.thepiratescodingtest.entity.Option;
import com.example.thepiratescodingtest.entity.Product;
import com.example.thepiratescodingtest.entity.Type;
import com.example.thepiratescodingtest.repository.DeliveryRepository;
import com.example.thepiratescodingtest.repository.OptionRepository;
import com.example.thepiratescodingtest.repository.ProductRepository;
import com.example.thepiratescodingtest.service.MainService;
import com.example.thepiratescodingtest.utility.Holidays;
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
    @DisplayName("일반 날짜(주말 x)")
    void holiday1(){
        when(productRepository.getById(1L)).thenReturn(productFast);
        when(productRepository.getById(2L)).thenReturn(productRegular);

        mainService = new MainService(
                optionRepository, deliveryRepository, productRepository);

        System.out.println(Holidays.holidayArray("2022"));

        System.out.println("당일배송일 경우");

        List<DateResponseDto> dateResponseDtos = mainService.getDeliveryDates(1L,
                LocalDateTime.of(2022, 03, 11, 11, 00));

        for(DateResponseDto dto : dateResponseDtos) {
            System.out.println(dto.getDate());
        }

        System.out.println("익일배송일 경우");

        List<DateResponseDto> dateResponseDtos2 = mainService.getDeliveryDates(2L,
                LocalDateTime.of(2022, 03, 11, 11, 00));

        for(DateResponseDto dto : dateResponseDtos2) {
            System.out.println(dto.getDate());
        }
    }
}
