package ru.karine.page.recruitment;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import ru.karine.page.FormPage;

import java.util.concurrent.TimeUnit;

public class AddRecruitmentVacancyPage extends FormPage<AddRecruitmentVacancyPage> {
    private final static SelenideElement vacancyNameInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Vacancy Name']]//input")
            .as("Вакансия");
    private final static SelenideElement jobTitleCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Job Title']]//div[@class='oxd-select-text-input']")
            .as("Наименование должности");

    private final static SelenideElement hiringManagerCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Hiring Manager']]//input")
            .as("Менеджер по подбору");

    @SneakyThrows
    @Step("Заполнение данных по вакансии {vacancy}")
    public AddRecruitmentVacancyPage fillForm(String vacancy, String jobTitle, String hiringManager){
        TimeUnit.SECONDS.sleep(5);
        replaceFieldText(vacancyNameInput, vacancy);
        selectOption(jobTitleCombobox, jobTitle);
        selectOptionByFilter(hiringManagerCombobox, hiringManager);
        return this;
    }
}
