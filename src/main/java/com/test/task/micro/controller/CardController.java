package com.test.task.micro.controller;

import com.test.task.micro.model.CalculatedCard;
import com.test.task.micro.model.Card;
import com.test.task.micro.service.external.impl.AmountServiceImpl;
import com.test.task.micro.service.external.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.test.task.micro.model.PayerInfo.PayType.CASH;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CardController {

    AmountServiceImpl amountService;
    // Сервис проверки адреса пользователя
    UserAddressService addressService;

    @PostMapping("/calculate")
    public ResponseEntity<CalculatedCard> calculateCard(@RequestBody Card card) {
        val calculatedItems = card.getItems()
                .stream()
                .map(amountService::calculate)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Пользователь без изестного адреса в книжке не может оплатить заказ наличными
        if (card.getPayerInfo().getPayType() == CASH && !addressService.availableUser(card.getPayerInfo())) {
            return status(UNAUTHORIZED).body(null);
        }
        val calculatedCard = new CalculatedCard(calculatedItems, card.getPayerInfo());
        // Если финальная корзина отличается по размерам от запрашиваемой
        // значит цены для каких-то товаров не найдены
        if (calculatedCard.getItems().size() != card.getItems().size()) {
            return status(NOT_FOUND).body(calculatedCard);
        }
        return ok(calculatedCard);
    }

}
