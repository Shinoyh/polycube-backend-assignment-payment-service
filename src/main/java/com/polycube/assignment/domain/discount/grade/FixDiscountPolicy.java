package com.polycube.assignment.domain.discount.grade;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        return discountAmount;
    }

    @Override
    public String getPolicyName() {
        return "FixDiscountPolicy";
    }

    @Override
    public String getDescription() {
        return "VIP 고객은 " + discountAmount + "원 고정 금액 할인";
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

}
