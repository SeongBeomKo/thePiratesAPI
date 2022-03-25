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
import com.example.thepiratescodingtest.utility.Holidays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final OptionRepository optionRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;

    // 상품 목록 조회
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>(products.size());
        for (Product product : products) {
            productResponseDtos.add(
                    ProductResponseDto.builder()
                            .name(product.getProductName())
                            .description(product.getProductDesc())
                            .price(String.format("%,d", product.getMin_price()) + " ~")
                            .build()
            );
        }
        return productResponseDtos;
    }

    //새 상품 등록
    @Transactional
    public void uploadNewProduct(ProductRequestDto productRequestDto) {

        Type type = productRequestDto.getDelivery().getType().equals("fast") ?
                Type.FAST :
                Type.REGULAR;
        //배송 테이블 등록
        Delivery delivery = deliveryRepository.save(Delivery.builder()
                .price(productRequestDto.getDelivery().getPrice())
                .closingTime(LocalTime.parse(productRequestDto.getDelivery().getClosing(),
                        DateTimeFormatter.ofPattern("H:mm")))
                .type(type)
                .build()
        );

        // 상품 테이블 등록
        Product product = productRepository.save(Product.builder()
                .productName(productRequestDto.getName())
                .productDesc(productRequestDto.getDescription())
                .min_price(productRequestDto.getOptions()
                        .stream()
                        .map(OptionRequestDto::getPrice)
                        .min(Integer::compare).get())
                .delivery(delivery)
                .build());
        // 옵션 테이블 등록
        for (OptionRequestDto optionRequestDto : productRequestDto.getOptions()) {
            optionRepository.save(Option.builder()
                    .price(optionRequestDto.getPrice())
                    .name(optionRequestDto.getName())
                    .stock(optionRequestDto.getStock())
                    .product(product)
                    .build()
            );
        }
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // 상품 상세 조회
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

    // 수령가능 날짜 조회
    public List<DateResponseDto> getDeliveryDates(Long id, LocalDateTime now) {

        Product product = productRepository.getById(id);

        //당일 마감 시간
        LocalDateTime closingTime = LocalDateTime
                .of(now.getYear(),
                        now.getMonth(),
                        now.getDayOfMonth(),
                        product.getDelivery().getClosingTime().getHour(),
                        product.getDelivery().getClosingTime().getMinute());

        //하루 전 마감시간
        LocalDateTime oneDayBefore = LocalDateTime.from(closingTime).minusDays(1);
        // 요청 시 주말 혹은 공휴일이면 발송 가능일 까지 ++ 후 마감시간 전으로 표시(true)
        // 요청시 발송 가능일이라면 마감시간 전에 주문했는지 확인 후 표시
        boolean beforeClosing =
                Holidays.weekendOrHoliday(now.toLocalDate()).equals(now.toLocalDate()) ?
                now.isBefore(closingTime) && now.isAfter(oneDayBefore) : true;


        //가장 이른 수령 가능날짜 확인
        LocalDate startDate = product.getDelivery().getType().getDeliveryDate(beforeClosing, now);

        List<DateResponseDto> dateResponseDtos = new ArrayList<>();
        Set<String> holidays = Holidays.holidayArray(String.valueOf(LocalDate.now().getYear()));
        int days = 0;
        // 수령 가능날짜 5개 선별
        while (dateResponseDtos.size() < 5) {
                dateResponseDtos.add(new DateResponseDto(LocalDate
                        .from(startDate)
                        .plusDays(days)
                        .format(DateTimeFormatter.ofPattern("MM월 dd일 E요일"))));
                days++;
        }
        return dateResponseDtos;
    }
}
