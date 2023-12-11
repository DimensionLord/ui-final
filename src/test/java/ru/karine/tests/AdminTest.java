package ru.karine.tests;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.karine.BaseTest;
import ru.karine.page.admin.AddJobTitlePage;
import ru.karine.page.screens.AdminPage;
import ru.karine.utils.Stash;

public class AdminTest extends BaseTest {
    AdminPage adminPage = new AdminPage();
    AddJobTitlePage addJobTitlePage = new AddJobTitlePage();

    @DataProvider
    public Object[][] provideJobData() {
        return new Object[][]{
                {Faker.instance().yoda().quote(), Faker.instance().ancient().titan(), Faker.instance().crypto().sha512()}
        };
    }

    @Test(description = "Проверка добавления новой должности", dataProvider = "provideJobData")
    public void addJobTitleTest(String jobTitle, String jobDescription, String jobNote) {
        adminPage.navigate("Job", "Job Titles")
                .addJob();
        addJobTitlePage.fillForm(jobTitle, jobDescription, jobNote)
                .submitForm();
        adminPage.checkJob(jobTitle, jobDescription);
    }

    @Test(description = "Проверка обработки добавления должности без наименования", dataProvider = "provideJobData")
    public void checkEmptyJobTitleTest(String ignored, String jobDescription, String jobNote) {
        adminPage.navigate("Job", "Job Titles")
                .addJob();
        addJobTitlePage.fillForm(null, jobDescription, jobNote)
                .submitForm();
        addJobTitlePage.validateEmptyJobTitleError();
    }


    @Test(description = "Провека удаления должности")
    public void deleteJob() {
        adminPage.navigate("Job", "Job Titles")
                .waitListToLoad()
                .saveTableValueToStash(0,"Job Titles", "jobTitleToDelete")
                .deleteRecord(0)
                .waitListToLoad()
                .checkJobDoesNotExist("Job Titles", Stash.getInstance().getFromStash("jobTitleToDelete"));
    }


    @Test(description = "Провека редактирования должности", dataProvider = "provideJobData")
    public void editEmployeeTest(String ignored, String jobDescription, String alsoIgnored) {
        adminPage.navigate("Job", "Job Titles")
                .waitListToLoad()
                .saveTableValueToStash(0, "Job Titles", "jobTitleToEdit")
                .editRecord(0);

        addJobTitlePage.fillForm(null, jobDescription, null)
                .submitForm();
        adminPage.checkJob(Stash.getInstance().getFromStash("jobTitleToEdit"), jobDescription);

    }
}
