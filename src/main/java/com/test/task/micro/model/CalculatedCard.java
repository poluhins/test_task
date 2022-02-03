package com.test.task.micro.model;

import lombok.Value;

import java.io.Serializable;
import java.util.Collection;

@Value
public class CalculatedCard implements Serializable {
    Collection<CalculatedItem> items;
    PayerInfo payerInfo;
    double amount;

    public CalculatedCard(Collection<CalculatedItem> items, PayerInfo payerInfo) {
        this.items = items;
        this.payerInfo = payerInfo;
        this.amount = items.stream()
                .mapToDouble(CalculatedItem::getAmount)
                .sum();
    }
}
