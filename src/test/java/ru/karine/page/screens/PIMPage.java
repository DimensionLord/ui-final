package ru.karine.page.screens;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import ru.karine.page.ScreenPage;
import ru.karine.page.utils.Table;

import java.time.Duration;

public class PIMPage extends ScreenPage<PIMPage> {
    private final static SelenideElement addButton = Selenide.$x("//button[contains(.,'Add')]").as("Добавить сотрудника");
    private final static SelenideElement employeeIdFilterInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Employee Id']]//input")
            .as("Фильтр по Id");
    private final static SelenideElement searchButton = Selenide.$x("//button [@type='submit']").as("Поиск");



    @Step("Инициализация создания нового сотрудника")
    public void addEmployee() {
        addButton.click();
    }


    @SneakyThrows
    @Step("Поиск сотрудника в таблице по Id '{employeeId}'")
    public PIMPage filterById(String employeeId) {
        employeeIdFilterInput.sendKeys(employeeId);
        searchButton.click();
        waitForSpinner();
        return this;
    }
}
