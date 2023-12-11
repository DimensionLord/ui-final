package ru.karine.page.screens;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.karine.page.ScreenPage;

public class RecruitmentVacancyPage extends ScreenPage<RecruitmentVacancyPage> {
    private final static SelenideElement addButton = Selenide.$x("//button[contains(.,'Add')]").as("Добавить подбор");

    private final static SelenideElement vacancyCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Vacancy']]//div[@class='oxd-select-text-input']")
            .as("Вакансия");
    private final static SelenideElement searchButton = Selenide.$x("//button [@type='submit']").as("Поиск");

    @Step("Инициализация создания новой вакансии")
    public void addVacancy() {
        addButton.click();
    }


    @Step("Поиск вакансии '{vacancy}'")
    public RecruitmentVacancyPage filterByVacancy(String vacancy) {
        selectOption(vacancyCombobox, vacancy);
        searchButton.click();
        waitForSpinner();
        return this;
    }

}
