package com.polycube.assignment.domain.payment;

import com.polycube.assignment.domain.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int finalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    @Builder
    public Payment(Order order, int finalPrice, PaymentMethod paymentMethod) {
        this.order = order;
        this.finalPrice = finalPrice;
        this.paymentMethod = paymentMethod;
        this.createdAt = LocalDateTime.now();
    }
}
