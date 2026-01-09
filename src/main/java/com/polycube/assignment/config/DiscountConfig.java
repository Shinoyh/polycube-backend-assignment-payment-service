package com.polycube.assignment.config;

import com.polycube.assignment.domain.discount.DiscountPolicy;
import com.polycube.assignment.domain.discount.FixDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscountConfig {

    @Bean
    public DiscountPolicy discountPolicy() {

        // 1. VIP 1000원 할인
        return new FixDiscountPolicy();
        // 2. VVIP 10% 할인
        // return new RateDiscountPolicy();
    }
}
