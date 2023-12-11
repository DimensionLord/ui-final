package ru.karine.page.screens;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.karine.page.ScreenPage;

public class ClaimPage extends ScreenPage<ClaimPage> {
    private final static SelenideElement addButton = Selenide.$x("//button[contains(.,'Add')]").as("Добавить событие");
    private final static SelenideElement eventNameCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Event Name']]//input")
            .as("Название события");
    private final static SelenideElement searchButton = Selenide.$x("//button [@type='submit']").as("Поиск");
    private final static SelenideElement eventStatusCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Status']]//div[@class='oxd-select-text-input']")
            .as("Статус события");

    @Step("Инициализация создания нового события")
    public void addEvent() {
        addButton.click();
    }

    public ClaimPage filterByEventName(String eventName) {
        selectOptionByFilter(eventNameCombobox, eventName);
        searchButton.click();
        return this;
    }


    public ClaimPage filterByEventStatus(String status) {
        selectOption(eventStatusCombobox, status);
        searchButton.click();
        return this;
    }
}


