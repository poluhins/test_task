package com.test.task.micro.service.external;

import com.test.task.micro.model.PayerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserAddressService {

    private Map<Long, String> userAddressesCache = new ConcurrentHashMap<>();

    public boolean availableUser(PayerInfo payerInfo) {
        return payerInfo.getAddressId() == null || !userAddressesCache.containsKey(payerInfo.getAddressId());
    }
}
