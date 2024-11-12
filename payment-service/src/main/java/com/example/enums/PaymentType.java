package com.example.enums;

import lombok.Getter;

@Getter
public enum PaymentType {

    CREDIT_CARD("Credit card"),
    GIFT_CARD("Gift card"),
    PAYPAL("Paypal");

    private final String name;

    PaymentType(String name){
        this.name = name;
    }
}
