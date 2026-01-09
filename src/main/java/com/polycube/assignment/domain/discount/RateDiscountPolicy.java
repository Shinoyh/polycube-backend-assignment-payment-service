package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {

        if (member.getGrade() == Grade.VVIP) {
            return price * discountPercent / 100;
        }
        return 0;
    }

    @Override
    public String getPolicyName() {
        return "RateDiscountPolicy";
    }

    @Override
    public String getDescription() {
        return "주문 금액의 " + discountPercent +"% 할인";
    }

    @Override
    public int getDiscountPercent() {
        return discountPercent;
    }
}
