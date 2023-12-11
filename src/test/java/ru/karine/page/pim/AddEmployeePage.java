package ru.karine.page.pim;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import ru.karine.page.BasePage;
import ru.karine.page.FormPage;

import java.util.concurrent.TimeUnit;

public class AddEmployeePage extends FormPage<AddEmployeePage> {
    private final static SelenideElement firstNameInput = Selenide.$x("//input[@name='firstName']").as("Имя");
    private final static SelenideElement middleNameInput = Selenide.$x("//input[@name='middleName']").as("Второе имя");
    private final static SelenideElement lastNameInput = Selenide.$x("//input[@name='lastName']").as("Фамилия");
    private final static SelenideElement employeeIdInput = Selenide
            .$x("//div[contains(@class,'oxd-input-group')][.//label[text()='Employee Id']]//input")
            .as("Id");


    @SneakyThrows
    @Step("Заполнение данных сотрудника '{firstName} {middleName} {lastName}'")
    public AddEmployeePage fillForm(String firstName, String middleName, String lastName, String employeeId) {
        TimeUnit.SECONDS.sleep(5);
        fillFields(
                Pair.of(firstNameInput, firstName),
                Pair.of(middleNameInput, middleName),
                Pair.of(lastNameInput, lastName),
                Pair.of(employeeIdInput, employeeId)
        );
        return this;
    }

    public AddEmployeePage fillForm(String firstName, String middleName, String lastName) {
        return fillForm(firstName, middleName, lastName, null);
    }



    @Step("Провекрка сообщения о превышении максимальной длины идентификатора сотрудника")
    public AddEmployeePage checkLongIdMessage() {
        validateErrorMessage("Should not exceed 10 characters");
        return this;
    }
}
