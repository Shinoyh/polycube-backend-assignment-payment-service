package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountAmount = 1000;

    @Override
    public int discount(Member member, int price) {

        if (member.getGrade() == Grade.VIP) {
            return discountAmount;
        }
        return 0;
    }

    @Override
    public String getPolicyName() {
        return "FixDiscountPolicy";
    }

    @Override
    public String getDescription() {
        return discountAmount + "원 고정 금액 할인";
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }

}
