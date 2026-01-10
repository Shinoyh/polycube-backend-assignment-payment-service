package com.polycube.assignment.domain.discount.grade;

import com.polycube.assignment.domain.member.Member;
import org.springframework.stereotype.Component;

@Component
public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(Member member, int price) {
        return 0;
    }

    @Override
    public String getPolicyName() {
        return "NoneDiscount";
    }

    @Override
    public String getDescription() {
        return "할인 없음";
    }

    @Override
    public int getDiscountPercent() {
        return 0;
    }
}
