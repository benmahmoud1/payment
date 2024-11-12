package com.example.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    NEW("New"),
    AUTHORIZED("Authorized"),
    CAPTURED("Captured"),
    CANCELED("Caneled");

    private final String name;

    PaymentStatus(String name){
        this.name = name;
    }


}
