package com.test.task.micro.model;

import lombok.Value;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@Value
public class PayerInfo implements Serializable {

    public enum PayType implements Serializable {
        CASH,
        CARD
    }

    PayType payType;
    @Nullable
    Long addressId;
}
