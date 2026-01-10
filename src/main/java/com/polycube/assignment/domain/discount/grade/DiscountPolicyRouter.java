package com.polycube.assignment.domain.discount.grade;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountPolicyRouter {

    private final FixDiscountPolicy fixDiscountPolicy;
    private final RateDiscountPolicy rateDiscountPolicy;
    private final NoneDiscountPolicy noneDiscountPolicy;

    public DiscountPolicy getDiscountPolicy(Member member) {

        if (member.getGrade() == Grade.VIP) {
            return fixDiscountPolicy;
        }
        else if (member.getGrade() == Grade.VVIP) {
            return rateDiscountPolicy;
        }

        return noneDiscountPolicy;
    }
}
