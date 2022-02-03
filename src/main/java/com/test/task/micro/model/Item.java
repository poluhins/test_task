package com.test.task.micro.model;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serializable;

@Value
@NonFinal
public class Item implements Serializable {
    int id;
    int count;
}
