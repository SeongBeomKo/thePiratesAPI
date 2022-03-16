package com.example.thepiratescodingtest;

import com.example.thepiratescodingtest.repository.DeliveryRepository;
import com.example.thepiratescodingtest.repository.OptionRepository;
import com.example.thepiratescodingtest.repository.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ProductRepository productRepository;
}
