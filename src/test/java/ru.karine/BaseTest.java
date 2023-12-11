package ru.karine;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import ru.karine.page.BasePage;
import ru.karine.page.screens.AuthPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.Function;

/**
 * Тестовый класс, от которого наследуются все тесты экранов
 */
public abstract class BaseTest {
    AuthPage authPage = new AuthPage();
    BasePage<?> mainPage = new BasePage<>();

    /**
     * Добавляет конфигурации браузера для запуска тестов
     */
    private void configBrowser() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of("enableVNC", true));
        Configuration.remote = "http://51.250.123.179:4444/wd/hub";
        Configuration.browserSize = "1920x1080";
        Configuration.browser = "chrome";
        Configuration.browserVersion = "112.0";
        Configuration.browserCapabilities = capabilities;
    }


    /**
     * Метод, позваляющий извлекать значения, как из системных свойств, так и из параметров запуска (приоритет принадлежит параметрам запуска)
     *
     * @param propertyKey ключ значения
     */
    private String getSystemProperty(String propertyKey) {
        if (System.getProperties().containsKey(propertyKey)) {
            return System.getProperty(propertyKey);
        }
        return System.getenv(propertyKey);
    }

    /**
     * Общая конфигурация перед всеми тестами
     */
    @BeforeSuite
    public void initEnv() {
        configBrowser();
        //лисенер, добавляющий в отчет информацию о всех взаимодействиях с элементами
        //включены опции автоматического добавления скринов и исходников страницы при ошибке
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    /**
     * Общий метод для всех тестов, который обеспечивает авторизацию и переход к тестируемому экрану
     *
     * @param url        адрес страницы приложения
     * @param userName   имя пользователя
     * @param password   пароль
     * @param targetPage тестируемый экран
     */
    @Step("Открытие приложения на странице '{targetPage}'")
    protected void moveTo(URL url, String userName, @Param(mode = Parameter.Mode.HIDDEN) String password, String targetPage) {
        Selenide.open(url);
        authPage.authorise(userName, password)
                .checkAuth();
        mainPage.navigateToScreen(targetPage);
    }

    /**
     * Общий метод для всех тестов, который обеспечивает авторизацию и переход к тестируемому экрану
     * автоматически получает имя пользователя, пароль, и url из свойств окружения
     */
    @BeforeMethod
    @Parameters({"targetPage"})
    public void moveTo(String targetPage) {
        String userName = getSystemProperty("UI_LOGIN");
        if (StringUtils.isBlank(userName)) {
            throw new RuntimeException("Необходимо указать имя пользователя через переменную 'UI_LOGIN'");
        }
        String password = getSystemProperty(userName.toUpperCase() + "_PASS");
        if (StringUtils.isBlank(password)) {
            throw new RuntimeException("Необходимо указать пароль пользователя через переменную '" + userName.toUpperCase() + "_PASS'");
        }
        String url = getSystemProperty("HRM_URL");
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("Необходимо указать url через переменную 'HRM_URL'");
        }
        try {
            moveTo(new URL(url), userName, password, targetPage);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL '" + url + "' не является валидной URL");
        }

    }

    /**
     * Принудительно закрывает браузер после каждого теста
     */
    @AfterMethod
    public void killMe() {
        Selenide.closeWebDriver();
    }
}
