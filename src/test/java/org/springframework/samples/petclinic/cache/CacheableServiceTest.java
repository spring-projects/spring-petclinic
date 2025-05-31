package org.springframework.samples.petclinic.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CacheableServiceTest {

    @Autowired
    private CacheableService cacheableService;
    
    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheHit() {
        // Первый вызов - кэш промах
        long startTime1 = System.currentTimeMillis();
        String result1 = cacheableService.getData("key1");
        long duration1 = System.currentTimeMillis() - startTime1;
        
        // Второй вызов - должен быть кэш хит
        long startTime2 = System.currentTimeMillis();
        String result2 = cacheableService.getData("key1");
        long duration2 = System.currentTimeMillis() - startTime2;
        
        // Проверяем результаты
        assertEquals("value1", result1);
        assertEquals("value1", result2);
        
        // Второй вызов должен быть быстрее из-за кэширования
        assertTrue(duration2 < duration1);
    }
    
    @Test
    public void testCacheMiss() {
        // Проверяем несуществующий ключ
        String result = cacheableService.getData("nonExistentKey");
        assertNull(result);
    }
    
    @Test
    public void testCacheUpdate() {
        // Обновляем значение
        cacheableService.updateData("key1", "newValue");
        
        // Получаем обновленное значение
        String result = cacheableService.getData("key1");
        assertEquals("newValue", result);
    }
} 