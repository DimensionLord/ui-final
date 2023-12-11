package ru.karine.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Вспомогательный класс для описания ожиданий
 */
@UtilityClass
public class WaitUtils {

    /**
     * Дожидается некоторого условия
     * @param supplier воспроизводимое событие, имеющее результат
     * @param tester описание проверки результата события
     * @param duration общая продолжительность ожидания
     * @param tickTime пауза между попытками
     * @return true, если результат события валиден, false, если не удалось дождаться валидного результата в течение заданного времени
     */
    @SneakyThrows
    public <T> boolean waitForOptionalEvent(Supplier<T> supplier, Predicate<T> tester, Duration duration, long tickTime) {
        long millis = System.currentTimeMillis() + duration.toMillis();
        while (System.currentTimeMillis() < millis) {
            if (tester.test(supplier.get())) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(tickTime);
        }
        return false;
    }
}
