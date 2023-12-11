package ru.karine.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import ru.karine.page.claim.AddEventPage;
import ru.karine.page.screens.RecruitmentCandidatePage;
import ru.karine.utils.WaitUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *  Общий предок всех страниц
 * @param <T> собственный тип страницы. Используется для fluent style
 */
public class BasePage<T extends BasePage<T>> {
    private final static SelenideElement mainMenu = Selenide.$x("//ul[@class='oxd-main-menu']").as("Главное меню");
    private final static SelenideElement spinner = Selenide.$x("//div[@class='oxd-loading-spinner']").as("Спиннер");
    private final static SelenideElement screenMenu = Selenide.$x("//nav/ul").as("Меню страницы");
    private final static SelenideElement subMenu = Selenide.$x("//ul[@class='oxd-dropdown-menu']").as("Подменю");
    private final static SelenideElement comboList = Selenide.$x("//div[@role='listbox']");

    /**
     * Переключает переключатель в заданное состояние
     * @param checkBox переключатель
     * @param expected ожидаемое состояние
     */
    protected void setCheckBox(SelenideElement checkBox, boolean expected) {
        boolean isSelected=!checkBox.getCssValue("background-color").equals("rgba(232, 234, 239, 1)");
        if (isSelected!= expected){
            checkBox.click();
        }
    }

    /**
     * Выбирает элемент из выпадающего списка
     * @param comboBox выпадающий список
     * @param target текст опции
     */
    protected void selectOption(SelenideElement comboBox, String target){
        comboBox.click();
        comboList.$x(".//div[@role='option'][.='"+target+"']").click();
    }

    /**
     * Выбирает элемент из выпадающего списка с фильтром
     * @param comboBox выпадающий список с фильтром
     * @param target текст опции
     */
    protected void selectOptionByFilter(SelenideElement comboBox, String target){
        comboBox.sendKeys(target);;
        comboList.$x(".//div[@role='option']").shouldNotBe(Condition.text("Searching....")).click();
    }

    @Step("Переход к экрану '{target}'")
    public void navigateToScreen(String target) {
        mainMenu.$x(".//a[.//span[text()='" + target + "']]").click();
    }

    /**
     * Ожидает исчезновения всех спиннеров
     * Логика работы следующая: ожидает появление спиннера, если дожидается, ждёт исчезновения спиннера, затем повторяет цикл до 4 раз
     * Если спиннер не появляется продолжает выполнение
     */
    protected void waitForSpinner() {
        for (int i = 0; i < 5; i++) {
            boolean spinnerExists = WaitUtils.waitForOptionalEvent(spinner::exists, b -> b, Duration.ofSeconds(2), 100);
            if (spinnerExists) {
                spinner.shouldBe(Condition.disappear, Duration.ofSeconds(15));
                continue;
            }
            return;
        }
    }

    /**
     * Заполняет поля, предварительно очищая их
     * Поскольку метод WebElement#clear очищает лишь визуальную состовляющую поля (при заполнении "очищенный текст" возвращается)
     * @param element поле
     * @param text текст
     */
    protected void replaceFieldText(SelenideElement element, String text) {
        new Actions(Selenide.webdriver().object())
                .click(element.getWrappedElement())
                .keyDown(Keys.LEFT_CONTROL)
                .keyDown("A")
                .keyUp("A")
                .keyUp(Keys.LEFT_CONTROL)
                .sendKeys(text)
                .build()
                .perform();
    }

    /**
     * Метод упрощающий работу с множеством полей
     * Логика работы следующая: для каждой пары элемент->текст заполняет элемент этим текстом
     * Если значение null ввод выполнен не будет, и значение поля сохранится
     * Если значение пустая строка, поле будет очищено
     * @param pairs пары элемент->текст
     */
    @SafeVarargs
    protected final void fillFields(Pair<SelenideElement, String>... pairs) {
        Arrays.stream(pairs).filter(entry -> entry.getValue() != null)
                .forEach(entry -> replaceFieldText(entry.getKey(), entry.getValue()));
    }

    @Step("Переход к {target}")
    public T navigate(String target) {
        screenMenu.$x(".//li[normalize-space(.)='" + target + "']").click();
        waitForSpinner();
        return (T) this;
    }

    @Step("Переход к {target}->{subTarget}")
    public T navigate(String target, String subTarget) {
        navigate(target);
        subMenu.$x(".//li[normalize-space(.)='" + subTarget + "']").click();
        return (T) this;
    }
}
