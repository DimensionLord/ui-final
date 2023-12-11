package ru.karine.tests;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.SneakyThrows;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.karine.BaseTest;
import ru.karine.page.pim.AddEmployeePage;
import ru.karine.page.screens.PIMPage;
import ru.karine.utils.Stash;

public class PIMTest extends BaseTest {

    PIMPage pimPage = new PIMPage();
    AddEmployeePage addEmployeePage = new AddEmployeePage();

    @DataProvider
    public Object[][] provideEmployeeData() {
        return new Object[][]{
                {Faker.instance().name().firstName().replace('\'','`'), Faker.instance().name().firstName().replace('\'','`'), Faker.instance().name().lastName().replace('\'','`'), Faker.instance().numerify("######")},
                {Faker.instance().name().firstName().replace('\'','`'), "", Faker.instance().name().lastName().replace('\'','`'), Faker.instance().numerify("######")}
        };
    }

    @Test(
            description = "Провека добавления нового сотрудника",
            dataProvider = "provideEmployeeData"
    )
    public void addEmployeeTest(String firstName, String middleName, String lastName, String employeeId) {
        pimPage.addEmployee();
        addEmployeePage.fillForm(firstName, middleName, lastName, employeeId)
                .submitForm();
        pimPage.navigate("Employee List")
                .filterById(employeeId)
                .checkRecordsCount(1);
    }

    @Test(description = "Проверка обработки некорректной длины Id")
    public void longEmployeeIdTest() {
        pimPage.addEmployee();
        Name name = Faker.instance().name();
        String id = Faker.instance().numerify("###########");
        addEmployeePage.fillForm(name.firstName(), name.firstName(), name.lastName(), id)
                .checkLongIdMessage()
                .submitForm();
        pimPage.navigate("Employee List")
                .filterById(id)
                .checkRecordsCount(0);
    }


    @Test(description = "Провека удаления сотрудника")
    public void deleteEmployeeFromTableTest() {
        pimPage
                .waitListToLoad()
                .saveTableValueToStash(0, "Id", "employeeToDelete")
                .deleteRecord(0)
                .filterById(Stash.getInstance().getFromStash("employeeToDelete"))
                .checkRecordsCount(0);
    }

    @Test(description = "Провека редактирования данных сотрудника", dataProvider = "provideEmployeeData")
    public void editEmployeeTest(String firstName, String middleName, String lastName, String ignored) {
        pimPage.waitListToLoad()
                .saveTableValueToStash(0, "Id", "employeeToEdit")
                .editRecord(0);
        addEmployeePage.fillForm(firstName, middleName, lastName)
                .submitForm();
        pimPage.navigate("Employee List")
                .waitListToLoad()
                .filterById(Stash.getInstance().getFromStash("employeeToEdit"))
                .waitListToLoad()
                .checkRecordData(0, "First (& Middle) Name", String.join(" ", firstName, middleName))
                .checkRecordData(0, "Last Name", lastName)
                .checkRecordsCount(1);
    }
}

