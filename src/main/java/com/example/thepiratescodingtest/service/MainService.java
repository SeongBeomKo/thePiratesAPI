package com.example.thepiratescodingtest.service;

import com.example.thepiratescodingtest.dto.request.OptionRequestDto;
import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.dto.request.ProductRequestDto;
import com.example.thepiratescodingtest.dto.response.OptionResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductInfoResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductResponseDto;
import com.example.thepiratescodingtest.entity.Delivery;
import com.example.thepiratescodingtest.entity.Option;
import com.example.thepiratescodingtest.entity.Product;
import com.example.thepiratescodingtest.entity.Type;
import com.example.thepiratescodingtest.repository.DeliveryRepository;
import com.example.thepiratescodingtest.repository.OptionRepository;
import com.example.thepiratescodingtest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MainService {

    private final OptionRepository optionRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>(products.size());
        for(Product product : products) {
            productResponseDtos.add(
                    ProductResponseDto.builder()
                            .name(product.getProductName())
                            .description(product.getProductDesc())
                            //포멧터로 바꾸기
                            .price(String.format("%,d",product.getMin_price()) + " ~")
                            .build()
            );
        }
        return productResponseDtos;
    }

    @Transactional
    public void uploadNewProduct(ProductRequestDto productRequestDto) {

        Product product = productRepository.save(Product.builder()
                .productName(productRequestDto.getName())
                .productDesc(productRequestDto.getDescription())
                .min_price(productRequestDto.getOptions()
                        .stream()
                        .map(OptionRequestDto::getPrice)
                        .min(Integer::compare).get())
                .build());

        for(OptionRequestDto optionRequestDto : productRequestDto.getOptions()) {
            optionRepository.save(Option.builder()
                    .price(optionRequestDto.getPrice())
                    .name(optionRequestDto.getName())
                    .stock(optionRequestDto.getStock())
                    .product(product)
                    .build()
            );
        }

        Type type = productRequestDto.getDelivery().getType().equals("fast") ?
                Type.FAST :
                Type.REGULAR;

        deliveryRepository.save(Delivery.builder()
                .price(productRequestDto.getDelivery().getPrice())
                .closingTime(LocalTime.parse(productRequestDto.getDelivery().getClosing(),
                        DateTimeFormatter.ofPattern("H:mm")))
                .type(type)
                .product(product)
                .build()
        );
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductInfoResponseDto getProductInfo(Long id) {
        Product product = productRepository.getById(id);

        return ProductInfoResponseDto
                .builder()
                .name(product.getProductName())
                .description(product.getProductDesc())
                .delivery(product.getDelivery().getType().getDelivery_type())
                .options(product.getOptionList()
                        .stream()
                        .map(p -> OptionResponseDto
                                .builder()
                                .name(p.getName())
                                .price(p.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<DateResponseDto> getDeliveryDates(Long id) {

        Product product = productRepository.getById(id);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime closingTime = LocalDateTime
                .of(LocalDate.now().getYear(),
                        LocalDate.now().getMonth(),
                        LocalDate.now().getDayOfMonth(),
                        product.getDelivery().getClosingTime().getHour(),
                        product.getDelivery().getClosingTime().getMinute());

        LocalDateTime oneDayBefore = LocalDateTime.from(closingTime).minusDays(1);

        boolean beforeClosing = now.isBefore(closingTime) && now.isAfter(oneDayBefore);

        LocalDate startDate = product.getDelivery().getType().getDeliveryDate(beforeClosing);

        return IntStream.rangeClosed(0, 4)
                .boxed()
                .map(p -> new DateResponseDto(
                        LocalDate.from(startDate)
                                .plusDays(p)
                                .toString()))
                .collect(Collectors.toList());
    }
}
