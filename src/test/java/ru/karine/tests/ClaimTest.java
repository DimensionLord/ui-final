package ru.karine.tests;

import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.karine.BaseTest;
import ru.karine.page.claim.AddEventPage;
import ru.karine.page.screens.ClaimPage;
import ru.karine.utils.Stash;

public class ClaimTest extends BaseTest {
    ClaimPage claimPage = new ClaimPage();
    AddEventPage addEventPage = new AddEventPage();

    @DataProvider
    public Object[][] provideEventData() {
        return new Object[][]{
                {Faker.instance().gameOfThrones().character().replace('\'','`'), Faker.instance().gameOfThrones().quote().replace('\'','`'), "Active"},
                {Faker.instance().gameOfThrones().character().replace('\'','`'), Faker.instance().gameOfThrones().quote().replace('\'','`'), "Inactive"}
        };
    }

    @DataProvider
    public Object[][] provideEventStatus() {
        return new Object[][]{
                {"Active", "Inactive"},
                {"Inactive", "Active"}
        };
    }

    @Test(
            description = "Проверка добавления нового события",
            dataProvider = "provideEventData"
    )
    public void addEventTest(String eventName, String eventDescription, String status) {
        claimPage.navigate("Configuration", "Events")
                .addEvent();
        addEventPage.fillForm(eventName, eventDescription, status)
                .submitForm();
        claimPage.waitListToLoad()
                .filterByEventName(eventName)
                .waitListToLoad()
                .checkRecordData(0, "Status", status)
                .checkRecordsCount(1);

    }

    @Test(description = "Провека удаления события")
    public void deleteEventTest() {
        claimPage
                .navigate("Configuration", "Events")
                .waitListToLoad()
                .saveTableValueToStash(0, "Event Name", "eventNameToDelete")
                .deleteRecord(0)
                .filterByEventName(Stash.getInstance().getFromStash("eventNameToDelete"))
                .checkRecordsCount(0);
    }

    @Test(description = "Проверка обработки добавления события без названия")
    public void checkEmptyEventNameTest() {
        claimPage
                .navigate("Configuration", "Events")
                .addEvent();
        addEventPage.fillForm(null, Faker.instance().gameOfThrones().quote(), null)
                .submitForm();
        addEventPage.validateEmptyEventNameError();
    }

    @Test(
            description = "Провека изменения статуса события",
            dataProvider = "provideEventStatus"
    )
    public void changeEventStatusTest(String baseStatus, String newStatus) {
        claimPage
                .navigate("Configuration", "Events")
                .filterByEventStatus(baseStatus)
                .waitListToLoad()
                .saveTableValueToStash(0, "Event Name", "eventToChange")
                .editRecord(0);
        addEventPage.fillForm(null, null, newStatus)
                .submitForm();
        claimPage.waitListToLoad()
                .filterByEventName(Stash.getInstance().getFromStash("eventToChange"))
                .waitListToLoad()
                .checkRecordData(0, "Status", newStatus);
    }
}
