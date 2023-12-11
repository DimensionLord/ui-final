package ru.karine.page.claim;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import ru.karine.page.FormPage;

import java.util.concurrent.TimeUnit;

public class AddEventPage extends FormPage<AddEventPage> {
    private final static SelenideElement eventNameInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Event Name']]//input")
            .as("Название события");
    private static final SelenideElement eventDescriptionInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Description']]//textarea")
            .as("Описание события");
    private final static SelenideElement eventStatusCheckbox = Selenide
            .$x("//div[@class='orangehrm-sm-field'][.//p[.='Active']]//span")
            .as("Статус");

    @SneakyThrows
    @Step("Заполнение описания события '{eventName}'")
    public AddEventPage fillForm(String eventName, String eventDescription, String status) {
        TimeUnit.SECONDS.sleep(5);
        fillFields(
                Pair.of(eventNameInput, eventName),
                Pair.of(eventDescriptionInput, eventDescription)
        );
        setStatus(status);
        return this;
    }

    @Step("Проверка сообщения о незаполненном названии события")
    public AddEventPage validateEmptyEventNameError() {
        validateErrorMessage("Required");
        return this;
    }

    @Step("Установка статуса события {status}")
    private void setStatus(String status) {
        if (status == null) {
            return;
        }
        boolean expected = status.equals("Active");
        setCheckBox(eventStatusCheckbox, expected);
    }

}
