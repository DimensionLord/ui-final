package ru.karine.page.screens;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import ru.karine.page.BasePage;
import ru.karine.utils.Stash;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyInfoPage extends BasePage<MyInfoPage> {
    private final static SelenideElement firstNameInput = Selenide.$x("//input[@name='firstName']").as("Имя");
    private final static SelenideElement middleNameInput = Selenide.$x("//input[@name='middleName']").as("Второе имя");
    private final static SelenideElement lastNameInput = Selenide.$x("//input[@name='lastName']").as("Фамилия");

    @Step("Сохранение имени пользователя в стеш")
    public MyInfoPage saveUserNameToStash() {
        String fullUserName = Stream.of(firstNameInput, middleNameInput, lastNameInput).map(this::getValue).collect(Collectors.joining(" "));
        Stash.getInstance().putInStash("fullUserName", fullUserName);
        return this;
    }
}
