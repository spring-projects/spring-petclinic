package org.springframework.samples.petclinic.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CacheableService {
    
    private final Map<String, String> dataStore = new HashMap<>();
    
    public CacheableService() {
        // Инициализация тестовых данных
        dataStore.put("key1", "value1");
        dataStore.put("key2", "value2");
        dataStore.put("key3", "value3");
    }
    
    @Cacheable(value = "dataCache", key = "#key")
    public String getData(String key) {
        // Имитация задержки при получении данных
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return dataStore.get(key);
    }
    
    public void updateData(String key, String value) {
        dataStore.put(key, value);
    }
} 