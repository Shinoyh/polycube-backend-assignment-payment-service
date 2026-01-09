package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private int ratePercent = 10;

    @Override
    public int discount(Member member, int price) {

        if (member.getGrade() == Grade.VVIP) {
            return price * ratePercent / 100;
        }
        return 0;
    }
}
