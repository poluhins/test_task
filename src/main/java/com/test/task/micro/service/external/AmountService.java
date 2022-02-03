package com.test.task.micro.service.external;

import com.test.task.micro.model.CalculatedItem;
import com.test.task.micro.model.Item;

public interface AmountService {

    CalculatedItem calculate(Item item);

}
