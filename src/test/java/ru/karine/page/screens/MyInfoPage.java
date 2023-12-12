package ru.karine.page.screens;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import ru.karine.page.BasePage;
import ru.karine.utils.Stash;

public class MyInfoPage extends BasePage<MyInfoPage> {
    private final static SelenideElement firstNameInput = Selenide.$x("//input[@name='firstName']").as("Имя");
    private final static SelenideElement middleNameInput = Selenide.$x("//input[@name='middleName']").as("Второе имя");
    private final static SelenideElement lastNameInput = Selenide.$x("//input[@name='lastName']").as("Фамилия");

    @Step("Сохранение имени пользователя в стеш")
    public MyInfoPage saveUserNameToStash() {
        String firstName = getValue(firstNameInput);
        String middleName = getValue(middleNameInput);
        String lastName = getValue(lastNameInput);
        String userName = String.join(" ", firstName, lastName);
        Stash.getInstance().putInStash("shortUserName", userName);
        Stash.getInstance().putInStash(
                "fullUserName",
                StringUtils.isBlank(middleName)
                        ? userName
                        : String.join(" ", firstName, middleName, lastName)
        );
        return this;
    }
}
