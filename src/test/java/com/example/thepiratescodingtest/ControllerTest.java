package com.example.thepiratescodingtest;

import com.example.thepiratescodingtest.dto.request.DeliveryRequestDto;
import com.example.thepiratescodingtest.dto.request.OptionRequestDto;
import com.example.thepiratescodingtest.dto.request.ProductRequestDto;
import com.example.thepiratescodingtest.dto.response.DateResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductInfoResponseDto;
import com.example.thepiratescodingtest.dto.response.ProductResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;
    private final ObjectMapper mapper = new ObjectMapper();

    private OptionRequestDto option1;
    private OptionRequestDto option2;
    private List<OptionRequestDto> options1;
    private DeliveryRequestDto deliveryRequestDto1;
    private ProductRequestDto productRequestDto1;

    private OptionRequestDto option3;
    private OptionRequestDto option4;
    private OptionRequestDto option5;
    private List<OptionRequestDto> options2;
    private DeliveryRequestDto deliveryRequestDto2;
    private ProductRequestDto productRequestDto2;

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        option1 = OptionRequestDto
                .builder()
                .name("생연어 몸통살 300g")
                .price(10000)
                .stock(99)
                .build();

        option2 = OptionRequestDto
                .builder()
                .name("생연어 몸통살 500g")
                .price(17000)
                .stock(99)
                .build();

        options1 = new ArrayList<>();
        options1.add(option1);
        options1.add(option2);

        deliveryRequestDto1 = DeliveryRequestDto
                .builder()
                .type("fast")
                .closing("12:00")
                .price(10000)
                .build();

        productRequestDto1 = ProductRequestDto
                .builder()
                .name("노르웨이산 연어")
                .description("노르웨이산 연어 300g, 500g, 반마리 필렛")
                .delivery(deliveryRequestDto1)
                .options(options1)
                .build();

        option3 = OptionRequestDto
                .builder()
                .name("대 7~8미")
                .price(50000)
                .stock(99)
                .build();

        option4 = OptionRequestDto
                .builder()
                .name("중 14~15미")
                .price(34000)
                .stock(99)
                .build();

        option5 = OptionRequestDto
                .builder()
                .name("소 50~60미")
                .price(20000)
                .stock(99)
                .build();

        options2 = new ArrayList<>();
        options2.add(option3);
        options2.add(option4);
        options2.add(option5);

        deliveryRequestDto2 = DeliveryRequestDto
                .builder()
                .type("regular")
                .closing("18:00")
                .price(5000)
                .build();

        productRequestDto2 = ProductRequestDto
                .builder()
                .name("완도전복")
                .description("산지직송 완도 전복 1kg (7미~60미)")
                .delivery(deliveryRequestDto2)
                .options(options2)
                .build();
    }


    @Test
    @Order(1)
    @DisplayName("상품 추가1 - 노르웨이산 연어")
    void test1() throws JsonProcessingException {
        String requestBody = mapper.writeValueAsString(productRequestDto1);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("상품 추가2 - 완도전복")
    void test2() throws JsonProcessingException {
        String requestBody = mapper.writeValueAsString(productRequestDto2);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("상품 목록 조회")
    void test3() throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                "/products",
                HttpMethod.GET,
                request,
                Object.class);

        List<ProductResponseDto> res = (List<ProductResponseDto>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, res.size());
    }

    @Test
    @Order(4)
    @DisplayName("상품 상세 조회1 - 노르웨이산 연어")
    void test4() throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ProductInfoResponseDto> response = testRestTemplate.exchange(
                "/product/1",
                HttpMethod.GET,
                request,
                ProductInfoResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getName(), "노르웨이산 연어");
        assertEquals(response.getBody().getDelivery(), "fast");
        assertEquals(response.getBody().getOptions().get(0).getName(), "생연어 몸통살 300g");

    }

    @Test
    @Order(5)
    @DisplayName("상품 상세 조회2 - 완도전복")
    void test5() throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ProductInfoResponseDto> response = testRestTemplate.exchange(
                "/product/2",
                HttpMethod.GET,
                request,
                ProductInfoResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getName(), "완도전복");
        assertEquals(response.getBody().getDelivery(), "regular");
        assertEquals(response.getBody().getOptions().get(1).getName(),
                "중 14~15미");

    }

    @Test
    @Order(6)
    @DisplayName("수령일 선택 목록 조회")
    void test6() throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Object> response = testRestTemplate.exchange(
                "/product/1/deliveryDates",
                HttpMethod.GET,
                request,
                Object.class);

        List<DateResponseDto> res = (List<DateResponseDto>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, res.size());
    }

    @Test
    @Order(7)
    @DisplayName("상품 삭제")
    void test7() throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        "/product/1",
                        HttpMethod.DELETE,
                        request,
                        Object.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<Object> response1 =
                testRestTemplate.exchange(
                        "/product/2",
                        HttpMethod.DELETE,
                        request,
                        Object.class);

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        ResponseEntity<Object> response2 = testRestTemplate.exchange(
                "/products",
                HttpMethod.GET,
                request,
                Object.class);

        List<ProductResponseDto> res = (List<ProductResponseDto>) response2.getBody();

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(0, res.size());
    }

    @Test
    @Order(8)
    @DisplayName("상품 추가 시 null값 요청 400 에러 - 상품명")
    void test8() throws JsonProcessingException {
        productRequestDto1 = ProductRequestDto
                .builder()
                .description("노르웨이산 연어 300g, 500g, 반마리 필렛")
                .delivery(deliveryRequestDto1)
                .options(options1)
                .build();

        String requestBody = mapper.writeValueAsString(productRequestDto1);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("상품 추가 시 null값 요청 400 에러 - 상품 설명")
    void test9() throws JsonProcessingException {
        productRequestDto1 = ProductRequestDto
                .builder()
                .name("노르웨이산 연어")
                .delivery(deliveryRequestDto1)
                .options(options1)
                .build();

        String requestBody = mapper.writeValueAsString(productRequestDto1);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(10)
    @DisplayName("상품 추가 시 null값 요청 400 에러 - 옵션 명")
    void test10() throws JsonProcessingException {
        option1 = OptionRequestDto
                .builder()
                .price(10000)
                .stock(99)
                .build();

        option2 = OptionRequestDto
                .builder()
                .name("생연어 몸통살 500g")
                .price(17000)
                .stock(99)
                .build();

        options1 = new ArrayList<>();
        options1.add(option1);
        options1.add(option2);

        productRequestDto1 = ProductRequestDto
                .builder()
                .name("노르웨이산 연어")
                .description("노르웨이산 연어 300g, 500g, 반마리 필렛")
                .delivery(deliveryRequestDto1)
                .options(options1)
                .build();

        String requestBody = mapper.writeValueAsString(productRequestDto1);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("상품 추가 시 null값 요청 400 에러 - 마감 시간")
    void test11() throws JsonProcessingException {

        deliveryRequestDto1 = DeliveryRequestDto
                .builder()
                .type("fast")
                .price(10000)
                .build();

        productRequestDto1 = ProductRequestDto
                .builder()
                .name("노르웨이산 연어")
                .description("노르웨이산 연어 300g, 500g, 반마리 필렛")
                .delivery(deliveryRequestDto1)
                .options(options1)
                .build();

        String requestBody = mapper.writeValueAsString(productRequestDto1);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = testRestTemplate.postForEntity(
                "/newProduct",
                request, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
