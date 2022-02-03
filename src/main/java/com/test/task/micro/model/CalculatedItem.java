package com.test.task.micro.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

@Value
@EqualsAndHashCode(callSuper = true)
public class CalculatedItem extends Item implements Serializable {

    double amount;

    public CalculatedItem(@NonNull Item item, double amount) {
        super(item.getId(), item.getCount());
        this.amount = amount * item.getCount();
    }
}

