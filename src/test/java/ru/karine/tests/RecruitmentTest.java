package ru.karine.tests;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.karine.BaseTest;
import ru.karine.page.admin.AddJobTitlePage;
import ru.karine.page.recruitment.AddRecruitmentCandidatePage;
import ru.karine.page.recruitment.AddRecruitmentVacancyPage;
import ru.karine.page.screens.AdminPage;
import ru.karine.page.screens.AuthPage;
import ru.karine.page.screens.RecruitmentCandidatePage;
import ru.karine.page.screens.RecruitmentVacancyPage;
import ru.karine.utils.Stash;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecruitmentTest extends BaseTest {

    RecruitmentCandidatePage recruitmentCandidatePage = new RecruitmentCandidatePage();
    RecruitmentVacancyPage recruitmentVacancyPage = new RecruitmentVacancyPage();
    AdminPage adminPage = new AdminPage();
    AuthPage authPage = new AuthPage();
    AddRecruitmentCandidatePage addRecruitmentPage = new AddRecruitmentCandidatePage();
    AddRecruitmentVacancyPage addRecruitmentVacancyPage = new AddRecruitmentVacancyPage();
    AddJobTitlePage addJobTitlePage = new AddJobTitlePage();

    @DataProvider
    public Object[][] provideRecruitmentCandidateData() {
        return new Object[][]{
                {Faker.instance().elderScrolls().creature(),
                        Faker.instance().book().title().replace('\'','`'),
                        Faker.instance().leagueOfLegends().champion().replace('\'','`'),
                        Faker.instance().name().firstName().replace('\'','`'),
                        Faker.instance().name().lastName().replace('\'','`'),
                        Faker.instance().internet().emailAddress(),
                        Faker.instance().ancient().god().replace('\'','`'),
                        Faker.instance().hobbit().quote().replace('\'','`')}
        };
    }

    @DataProvider
    public Object[][] provideRecruitmentVacancyData() {
        return new Object[][]{
                {Faker.instance().elderScrolls().creature().replace('\'','`'), Faker.instance().book().title().replace('\'','`')}
        };
    }

    @Test(
            description = "Проверка создания нового подбора",
            dataProvider = "provideRecruitmentVacancyData"
    )
    public void addRecruitmentVacancyTest(String jobTitle, String vacancy) {
        authPage.stashUserName();
        recruitmentCandidatePage.navigateToScreen("Admin");
        adminPage.navigate("Job", "Job Titles")
                .addJob();
        addJobTitlePage.fillForm(jobTitle, null, null)
                .submitForm();
        adminPage.navigateToScreen("Recruitment");
        recruitmentVacancyPage.navigate("Vacancies")
                .addVacancy();
        addRecruitmentVacancyPage.fillForm(vacancy, jobTitle, Stash.getInstance().getFromStash("userName"))
                .submitForm();
        recruitmentVacancyPage.navigate("Vacancies")
                .waitListToLoad()
                .filterByVacancy(vacancy)
                .checkRecordData(0, "Job Title", jobTitle)
                .checkRecordData(0, "Vacancy", vacancy)
                .checkRecordData(0, "Hiring Manager", Stash.getInstance().getFromStash("userName"))
                .checkRecordData(0, "Status", "Active");
    }

    @Test(description = "Проверка добавления нового подбора", dataProvider = "provideRecruitmentCandidateData")
    public void addRecruitmentCandidateTest(String jobTitle, String vacancy, String firstName, String middleName, String lastName, String email, String keywords, String note) {
        authPage.stashUserName();
        recruitmentCandidatePage.navigateToScreen("Admin");
        adminPage.navigate("Job", "Job Titles")
                .addJob();
        addJobTitlePage.fillForm(jobTitle, null, null)
                .submitForm();
        adminPage.navigateToScreen("Recruitment");
        recruitmentVacancyPage.navigate("Vacancies")
                .addVacancy();
        addRecruitmentVacancyPage.fillForm(vacancy, jobTitle, Stash.getInstance().getFromStash("userName"))
                .submitForm();
        recruitmentCandidatePage.navigate("Candidates")
                .addRecruitment();
        addRecruitmentPage.fillForm(firstName, middleName, lastName, email, keywords, note, vacancy)
                .submitForm();
        recruitmentCandidatePage.navigate("Candidates")
                .filterByFirstName(firstName)
                .checkRecordData(0, "Vacancy", vacancy)
                .checkRecordData(0, "Hiring Manager", Stash.getInstance().getFromStash("userName"))
                .checkRecordData(0, "Status", "Application Initiated")
                .checkRecordData(0, "Date of Application", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }


    @Test(description = "Провека удаления вакансии")
    public void deleteVacancyTest() {
        recruitmentVacancyPage
                .navigate("Vacancies")
                .waitListToLoad()
                .saveTableValueToStash(0, "Vacancy", "vacancyToDelete")
                .deleteRecord(0)
                .filterByVacancy(Stash.getInstance().getFromStash("vacancyToDelete"))
                .checkRecordsCount(0);
    }


    @Test(description = "Провека удаления кандидата")
    public void deleteCandidateTest() {
        recruitmentCandidatePage
                .waitListToLoad()
                .saveTableValueToStash(0, "Candidate", "candidateToDelete")
                .deleteRecord(0)
                .checkCandidateDoesNotExist("Candidate", Stash.getInstance().getFromStash("candidateToDelete"));
    }
}
