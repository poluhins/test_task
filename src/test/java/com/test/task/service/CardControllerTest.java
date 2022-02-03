package com.test.task.service;

import com.test.task.micro.controller.CardController;
import com.test.task.micro.model.Card;
import com.test.task.micro.model.Item;
import com.test.task.micro.model.PayerInfo;
import com.test.task.micro.service.external.impl.AmountServiceImpl;
import com.test.task.micro.service.external.UserAddressService;
import lombok.val;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.test.task.micro.model.PayerInfo.PayType.CARD;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CardControllerTest {

    public static final Map<Integer, Double> TEST_CACHE = new ConcurrentHashMap<>() {{
        put(0, 10.3);
        put(1, 45.8);
        put(2, 8.2);
    }};
    AmountServiceImpl service = new AmountServiceImpl(TEST_CACHE, TEST_CACHE, newSingleThreadExecutor());;
    CardController controller = new CardController(service, new UserAddressService());

    @Test
    public void cardCalculateTest() {
        val items = new ArrayList<Item>();
        items.add(new Item(0, 10));
        items.add(new Item(1, 30));
        items.add(new Item(2, 50));
        val card = new Card(items, new PayerInfo(CARD, 10L));

        val cardAmount = 10.3 * 10 + 45.8 * 30 + 50 * 8.2;
        assertEquals(cardAmount, controller.calculateCard(card).getBody().getAmount());

        card.getItems().add(new Item(3, 100));
        val resultCard = controller.calculateCard(card);
        assertEquals(NOT_FOUND, resultCard.getStatusCode());
        assertEquals(cardAmount, resultCard.getBody().getAmount());
    }


}
