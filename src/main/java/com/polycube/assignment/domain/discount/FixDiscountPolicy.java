package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int fixAmount = 1000;

    @Override
    public int discount(Member member, int price) {

        if (member.getGrade() == Grade.VIP) {
            return fixAmount;
        }
        return 0;
    }

}
