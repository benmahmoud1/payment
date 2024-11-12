package com.example.events;

import com.example.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusUpdatedEvent extends PaymentEvent{

    private PaymentStatus newStatus;
}
