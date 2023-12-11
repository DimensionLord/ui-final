package ru.karine.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Вспомогательный класс для хранения данных
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Stash {
    /**
     * Стеш должен быть уникальым для каждого потока, поэтому храним его в ThreadLocal
     */
    private final static ThreadLocal<Stash> instance = ThreadLocal.withInitial(Stash::new);

    /**
     * Хранилеще данных
     */
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Получение уникального объекта стеша
     */
    public static Stash getInstance() {
        return instance.get();
    }

    /**
     * Положить значение в стеш (существующее значение будет перезаписано)
     * @param key ключ
     * @param data значение
     */
    public void putInStash(String key, Object data) {
        this.data.put(key, data);
    }

    /**
     * Получение хранимого значения по ключу
     * @param key ключ
     * @throws RuntimeException если ключ не присутствует в стеше
     */
    public <T> T getFromStash(String key) {
        if (!data.containsKey(key)) {
            throw new RuntimeException("Данные не присутствуют в Stash");
        }
        return (T) data.get(key);
    }
}
