package com.test.task.micro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServiceConfig {

    @Bean("cacheUpdater")
    public ExecutorService cacheUpdater() {
        return Executors.newFixedThreadPool(4);
    }

    // Redis / NoSQL хранилище
    @Bean("amountCache")
    public Map<Integer, Double> amountCache() {
        return new ConcurrentHashMap<>();
    }

    // Http сервис для запросов к стороннему сервису
    @Bean("externalService")
    public Map<Integer, Double> externalService() {
        return new ConcurrentHashMap<>();
    }

}
