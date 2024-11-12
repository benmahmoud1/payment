package com.example.events;

import com.example.enums.PaymentType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreatedEvent extends PaymentEvent{

    private BigDecimal totalAmount;
    private PaymentType paymentType;
}
