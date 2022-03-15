package com.example.thepiratescodingtest.repository;

import com.example.thepiratescodingtest.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
