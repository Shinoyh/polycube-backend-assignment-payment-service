package com.polycube.assignment.domain.discounthistory;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private Grade memberGrade;

    private String discountPolicyName;

    private int discountPercent;

    private int discountAmount;

    private String description;

    @Builder
    public DiscountHistory(Payment payment, Grade memberGrade, String discountPolicyName, int discountPercent, int discountAmount, String description) {
        this.payment = payment;
        this.memberGrade = memberGrade;
        this.discountPolicyName = discountPolicyName;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.description = description;
    }

}
