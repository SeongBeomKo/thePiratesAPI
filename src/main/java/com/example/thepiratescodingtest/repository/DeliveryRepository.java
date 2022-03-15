package com.example.thepiratescodingtest.repository;

import com.example.thepiratescodingtest.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
