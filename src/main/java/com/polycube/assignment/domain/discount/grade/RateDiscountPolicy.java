package com.polycube.assignment.domain.discount.grade;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        return price * discountPercent / 100;
    }

    @Override
    public String getPolicyName() {
        return "RateDiscountPolicy";
    }

    @Override
    public String getDescription() {
        return "VVIP 고객은 주문 금액의 " + discountPercent +"% 할인";
    }

    @Override
    public int getDiscountPercent() {
        return discountPercent;
    }
}
