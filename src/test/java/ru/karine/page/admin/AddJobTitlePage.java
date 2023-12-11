package ru.karine.page.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import ru.karine.page.FormPage;

import java.util.concurrent.TimeUnit;

public class AddJobTitlePage extends FormPage<AddJobTitlePage> {
    private static final SelenideElement jobTitleInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Job Title']]//input")
            .as("Наименование должности");

    private static final SelenideElement jobDescriptionInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Job Description']]//textarea")
            .as("Описание должности");

    private static final SelenideElement jobNoteInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Note']]//textarea")
            .as("Заметки");


    @SneakyThrows
    @Step("Заполнение описания должности")
    public AddJobTitlePage fillForm(String title, String description, String note) {
        TimeUnit.SECONDS.sleep(5);
        fillFields(
                Pair.of(jobTitleInput, title),
                Pair.of(jobDescriptionInput, description),
                Pair.of(jobNoteInput, note)
        );
        return this;
    }

    @Step("Провекрка сообщения о незаполненном названии должности")
    public AddJobTitlePage validateEmptyJobTitleError() {
        validateErrorMessage("Required");
        return this;
    }
}
