package ru.karine.page.screens;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;
import ru.karine.page.ScreenPage;

public class AdminPage extends ScreenPage<AdminPage> {
    private final static SelenideElement addButton = Selenide.$x("//button[contains(.,'Add')]").as("Добавить должность");

    @Step("Инициализация создания новой должности")
    public void addJob() {
        addButton.click();
    }

    @Step("Проверка должности в списке")
    public void checkJob(String title, String description) {
        int index = getTable().getRowIndexByValue("Job Titles", title);
        getTable().getCell(index, "Job Description").should(Condition.text(description));
    }

    @Step("Проверка, что должность отсутствует в списке")
    public void checkJobDoesNotExist(String header, String value) {
        Assert.assertThrows("Запись не была удалена", AssertionError.class, () -> getTable().getRowIndexByValue(header, value));
    }
}
