package com.test.task.micro.service.external.impl;

import com.test.task.micro.model.CalculatedItem;
import com.test.task.micro.model.Item;
import com.test.task.micro.service.external.AmountService;
import com.test.task.micro.service.external.NotFoundItemCostException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AmountServiceImpl implements AmountService {

    @Qualifier("amountCache")
    Map<Integer, Double> amountCache;
    @Qualifier("externalService")
    Map<Integer, Double> externalService;
    @Qualifier("cacheUpdater")
    ExecutorService cacheExecutor;

    public CalculatedItem calculate(Item item) {
        try {
            val amount = getAmount(item.getId());
            LOGGER.debug("Get amount for {} item {}", item.getId(), amount);
            return new CalculatedItem(item, amount);
        } catch (Exception e) {
            LOGGER.error("Cannot find item cost for item {}", item);
            return null;
        }
    }

    // Если в кеше уже есть цена — отдаем сразу ее, в фоне обновляем кеш
    // если в кеше не данных — ждем цену со стороннего сервиса
    private double getAmount(int itemId) throws NotFoundItemCostException {
        val fromExternalService = cacheExecutor.submit(() -> makeRequestToExternalService(itemId));
        if (amountCache.containsKey(itemId)) {
            cacheExecutor.execute(() -> updateCache(itemId, fromExternalService));
            return amountCache.get(itemId);
        }
        try {
            return fromExternalService.get(10, SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Cannot get amount from external service for {} item", itemId, e);
            throw new NotFoundItemCostException();
        }
    }

    private void updateCache(int itemId, Future<Double> amount) {
        try {
            val result = amount.get(10, SECONDS);
            if (result == null) {
                return;
            }
            LOGGER.info("Update cache for {} item {}", itemId, result);
            amountCache.put(itemId, result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Cannot update cache for {} item", itemId, e);
        }
    }

    private Double makeRequestToExternalService(int itemId) {
        return externalService.get(itemId);
    }

}
