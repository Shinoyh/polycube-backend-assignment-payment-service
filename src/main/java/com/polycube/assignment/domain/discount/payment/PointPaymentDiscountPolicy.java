package com.polycube.assignment.domain.discount.payment;

import org.springframework.stereotype.Component;

@Component
public class PointPaymentDiscountPolicy implements PaymentDiscountPolicy {

    private int discountRate = 5;

    @Override
    public int calculateDiscount(int price) {
        return price * discountRate / 100;
    }

    @Override
    public String getDiscountPolicyName() {
        return "PointPaymentDiscountPolicy";
    }

    @Override
    public String getDescription() {
        return "카드결제 시 "+ discountRate +"% 할인";
    }

    @Override
    public int getDiscountPercent() {
        return discountRate;
    }
}
