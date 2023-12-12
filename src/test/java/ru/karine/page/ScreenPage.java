package ru.karine.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.AccessLevel;
import lombok.Getter;
import ru.karine.page.utils.Table;
import ru.karine.utils.Stash;

import java.time.Duration;

/**
 * Общий предок для всех экранов
 */

public abstract class ScreenPage<T extends BasePage<T>> extends BasePage<T> {
    private final static SelenideElement submitDeleteButton = Selenide
            .$x("//div[@role='document']//button[./i[contains(@class, 'trash')]]")
            .as("Подтверждение удаления сотрудника");

    @Getter(AccessLevel.PROTECTED)
    private final Table table = new Table();

    @Step("Сохранение {header} в Stash")
    public T saveTableValueToStash(int rowNum, String header, String stashKey) {
        String cellValue = getTable().getCell(rowNum, header).getText();
        Stash.getInstance().putInStash(stashKey, cellValue);
        return (T) this;
    }

    @Step("Удаление записи из списка")
    public T deleteRecord(int rowNum) {
        table.getRows().get(rowNum).$x(".//button[./i[contains(@class, 'trash')]]").click();
        submitDeleteButton.click();
        waitForSpinner();
        return waitListToLoad();
    }

    @Step("Ожидание загрузки экрана")
    public T waitListToLoad() {
        table.getRows().shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(10));
        return (T) this;
    }

    @Step("Редактирование записи")
    public void editRecord(int rowNum) {
        table.getRows().get(rowNum).$x(".//button[./i[contains(@class, 'pencil')]]").click();
    }

    @Step("Проверка {header} записи в списке")
    public T checkRecordData(int rowNum, String header, String data) {
        getTable().getCell(rowNum, header).shouldBe(Condition.text(data));
        return (T) this;
    }

    @Step("Проверка количества записей")
    public void checkRecordsCount(int count) {
        getTable().getRows().should(CollectionCondition.size(count));
    }

}
