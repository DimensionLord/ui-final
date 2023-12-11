package ru.karine.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * Общий предок для всех страниц, отображающих формы для заполнения
 */
public abstract class FormPage<T extends BasePage<T>> extends BasePage<T> {
    private final static SelenideElement saveButton = Selenide.$x("//button[@type='submit']").as("Сохранить");

    private final static SelenideElement errorMessage = Selenide
            .$x("//span[contains(@class, 'error-message')]")
            .as("Сообщение об ошибке");

    @SneakyThrows
    @Step("Сохранение введенных данных")
    public void submitForm() {
        saveButton.click();
        waitForSpinner();
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * Проверяет текст возникшего сообщения о неверном заполнении поля
     * @param text ожидаемый текст сообщения
     */
    protected void validateErrorMessage(String text) {
        errorMessage.shouldBe(Condition.text(text));
    }
}
