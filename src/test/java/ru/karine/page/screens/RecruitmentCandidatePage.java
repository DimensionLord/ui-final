package ru.karine.page.screens;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import ru.karine.page.ScreenPage;

import java.util.concurrent.TimeUnit;

public class RecruitmentCandidatePage extends ScreenPage<RecruitmentCandidatePage> {
    private final static SelenideElement addButton = Selenide.$x("//button[contains(.,'Add')]").as("Добавить подбор");
    private final static SelenideElement firstNameCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Candidate Name']]//input")
            .as("Имя кандидата");
    private final static SelenideElement searchButton = Selenide.$x("//button [@type='submit']").as("Поиск");

    @Step("Инициализация создания нового подбора")
    public void addRecruitment() {
        addButton.click();
    }


    @Step("Поиск кандидата по имени {firstName}")
    public RecruitmentCandidatePage filterByFirstName(String firstName) {
        selectOptionByFilter(firstNameCombobox, firstName);
        searchButton.click();
        waitForSpinner();
        return this;
    }
}
