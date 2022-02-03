package com.test.task.service;

import com.test.task.micro.model.Item;
import com.test.task.micro.service.external.impl.AmountServiceImpl;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(value = SpringJUnit4ClassRunner.class)
public class AmountServiceTest {

    public static final Map<Integer, Double> TEST_CACHE = new ConcurrentHashMap<>() {{
        put(0, 10.3);
        put(1, 45.8);
        put(2, 8.2);
    }};
    public static final Map<Integer, Double> EXTERNAL_DATA = new ConcurrentHashMap<>() {{
        put(2, 8.4);
        put(3, 20.3);
        put(4, 134.0);
        put(5, 35.6);
    }};

    AmountServiceImpl service = new AmountServiceImpl(TEST_CACHE, EXTERNAL_DATA, newSingleThreadExecutor());

    @SneakyThrows
    @Test
    public void updateCacheTest() {
        val item1 = new Item(1, 20);
        val item2 = new Item(2, 5);
        val item3 = new Item(5, 10);
        val item4 = new Item(10, 100);

        assertEquals(45.8 * 20, service.calculate(item1).getAmount());
        assertEquals(8.2 * 5, service.calculate(item2).getAmount());
        sleep(1000);
        assertEquals(8.4 * 5, service.calculate(item2).getAmount());
        assertEquals(35.6 * 10, service.calculate(item3).getAmount());
        assertEquals(null, service.calculate(item4));
    }
}
