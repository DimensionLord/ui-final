package ru.karine.page.screens;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import ru.karine.utils.Stash;

public class AuthPage {

    private final static SelenideElement userNameInput = Selenide.$x("//input[@name='username']").as("Логин");
    private final static SelenideElement passwordInput = Selenide.$x("//input[@name='password']").as("Пароль");
    private final static SelenideElement loginButton = Selenide.$x("//button[@type='submit']").as("Войти");
    private final static SelenideElement userIcon = Selenide.$x("//header//img").as("Иконка профиля");



    @Step("Авторизация как {userName}")
    public AuthPage authorise(String userName, @Param(mode = Parameter.Mode.MASKED) String password) {
        userNameInput.sendKeys(userName);
        passwordInput.setValue(SetValueOptions.withText(password).sensitive());
        loginButton.click();
        return this;
    }

    @Step("Проверка успешного входа")
    public void checkAuth() {
        userIcon.shouldBe(Condition.appear);
    }

}
