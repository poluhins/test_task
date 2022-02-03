package com.test.task.micro.model;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;
import java.util.Collection;

@Value
@NonFinal
public class Card implements Serializable {
    Collection<Item> items;
    PayerInfo payerInfo;
}
