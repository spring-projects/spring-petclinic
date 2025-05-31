package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringJUnitConfig
public class LoggingAspectTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Test
    public void testLogMethodExecution() throws Throwable {
        // Создаем тестовый метод для проверки
        TestService testService = new TestService();
        
        // Вызываем метод, который должен быть перехвачен аспектом
        testService.testMethod();
        
        // Проверяем, что логирование было вызвано
        verify(logger, atLeastOnce()).info(contains("Starting method:"));
        verify(logger, atLeastOnce()).info(contains("Method TestService.testMethod completed in"));
    }

    // Вспомогательный класс для тестирования
    private static class TestService {
        public void testMethod() {
            // Простой метод для тестирования
            try {
                Thread.sleep(100); // Имитируем некоторую работу
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
} 