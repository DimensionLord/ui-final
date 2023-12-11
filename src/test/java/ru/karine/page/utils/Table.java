package ru.karine.page.utils;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.Iterator;

/**
 * Вспомогательный класс для работы с таблицами
 */
public class Table {
    private final static SelenideElement headerRow = Selenide.$x("//div[@class='oxd-table-header']");
    private final static ElementsCollection rows = Selenide
            .$$x("//div[@class='oxd-table-card']/div [@role='row']")
            .as("Список записей");

    /**
     * Получение коллекции записей в таблице
     */
    public ElementsCollection getRows() {
        return rows;
    }

    /**
     * Получение индекса заголовка
     * @param header заголовок таблицы
     * @return индекс заголовка (начиная с 0)
     */
    public int getHeaderIndex(String header) {
        return headerRow.$$x(".//div[@role='columnheader']").texts().indexOf(header);
    }


    /**
     * Получение ячейки по номеру ряда и заголовку
     * @param rowNum номер ряда (начиная с 0)
     * @param header заголовок
     * @return ячейка как элемент
     */
    public SelenideElement getCell(int rowNum, String header) {
        int index = getHeaderIndex(header);
        return getRows().get(rowNum).$x("./div[@role='cell']", index);
    }

    /**
     * Получение индекса ряда по значению ячейки в определенной колонке
     * @param header заголовок колонки
     * @param value искомое значение ячейки
     * @return индекс ряда (начиная с 0)
     * @throws AssertionError если такого ряда не существует
     */
    public int getRowIndexByValue(String header, String value) {
        int index = getHeaderIndex(header);
        Iterator<SelenideElement> iterator = getRows().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            SelenideElement row = iterator.next();
            if (row.$x("./div[@role='cell']", index).getText().equals(value)) {
                return i;
            }
            i++;
        }
        throw new AssertionError("Запись отсутствует в таблице");
    }
}
