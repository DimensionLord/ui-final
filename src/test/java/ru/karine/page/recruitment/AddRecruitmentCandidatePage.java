package ru.karine.page.recruitment;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import ru.karine.page.FormPage;

import java.util.concurrent.TimeUnit;

public class AddRecruitmentCandidatePage extends FormPage<AddRecruitmentCandidatePage> {
    private final static SelenideElement firstNameInput = Selenide.$x("//input[@name='firstName']").as("Имя");
    private final static SelenideElement middleNameInput = Selenide.$x("//input[@name='middleName']").as("Второе имя");
    private final static SelenideElement lastNameInput = Selenide.$x("//input[@name='lastName']").as("Фамилия");
    private final static SelenideElement emailInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Email']]//input")
            .as("Email");
    private final static SelenideElement vacancyCombobox = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Vacancy']]//div[@class='oxd-select-text-input']")
            .as("Вакансии");

    private final static SelenideElement keywordsInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Keywords']]//input")
            .as("Ключевые слова");
    private static final SelenideElement recruitmentNoteInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Notes']]//textarea")
            .as("Заметки");


    @SneakyThrows
    @Step("Заполнение данных по подбору '{firstName} {middleName} {lastName} {email} {vacancy}'")
    public AddRecruitmentCandidatePage fillForm(String firstName, String middleName, String lastName, String email, String keywords, String note, String vacancy) {
        TimeUnit.SECONDS.sleep(5);
        fillFields(
                Pair.of(firstNameInput, firstName),
                Pair.of(middleNameInput, middleName),
                Pair.of(lastNameInput, lastName),
                Pair.of(emailInput, email),
                Pair.of(keywordsInput, keywords),
                Pair.of(recruitmentNoteInput, note)
        );
        selectOption(vacancyCombobox,vacancy);

        return this;
    }


}
