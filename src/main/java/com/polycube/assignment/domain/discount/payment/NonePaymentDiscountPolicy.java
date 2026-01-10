package com.polycube.assignment.domain.discount.payment;

import org.springframework.stereotype.Component;

@Component
public class NonePaymentDiscountPolicy implements PaymentDiscountPolicy {
    @Override
    public int calculateDiscount(int price) {
        return 0;
    }

    @Override
    public String getDiscountPolicyName() {
        return "NonePaymentDiscount";
    }

    @Override
    public String getDescription() {
        return "결제 수단 추가 할인 없음";
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }
}
