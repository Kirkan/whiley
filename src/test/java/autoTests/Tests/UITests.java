package autoTests.Tests;

import autoTests.Config;
import autoTests.JsonDataFileLocation;
import autoTests.PageObjects.*;
import autoTests.ProductType;
import autoTests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

@Epic("UI Tests")
public class UITests extends TestBase {

    @BeforeMethod
    public void beforeMethod() {
        open(Config.getInstance().getBaseUrl());
    }

    class CustomTestData {
        List<String> breadCrumbs;
        List<String> menuValues;
    }


    @JsonDataFileLocation("data/test1.json")
    @Test(dataProvider = "customDataProvider", description = "1. Check items under Who We Serve for sub-header. With DataProvider")
    public void checkMenuItemsForSubHeaderWDP(CustomTestData testData) {
        new SelectCountryDialog().closeDialog();
        List<String> actual = new MainMenu().getMainMenuItemLabels(testData.breadCrumbs.get(0));
        List<String> expected = testData.menuValues;
        Assert.assertEquals(actual.size(), expected.size(), "Количество пунктов не соответствует");
        Assert.assertEquals(actual, expected, "Пункты не соответствуют");
    }

    @Test(enabled = false, description = "1. Check items under Who We Serve for sub-header")
    public void checkMenuItemsForSubHeader() {
        new SelectCountryDialog().closeDialog();
        String[] actual = new MainMenu().getMainMenuItemLabels("WHO WE SERVE").toArray(new String[0]);
        String[] expected = new String[]{"Students", "Instructors", "Book Authors", "Professionals", "Researchers", "Institutions", "Librarians", "Corporations", "Societies", "Journal Editors", "Bookstores", "Government"};
        Assert.assertEquals(actual.length, expected.length, "Количество пунктов не соответствует");
        Assert.assertEquals(actual, expected, "Пункты не соответствуют");
    }

    @Test(description = "2. Check search functionality")
    public void checkContextSearch() {
        new SelectCountryDialog().closeDialog();
        ArrayList<String> request = RestAssured.given().param("term", "Java")
                .when().get(Config.getInstance().getBaseUrlApi()).path("suggestions");
        SearchPanel search = new SearchPanel();
        search.setSearchText("Java");
        Assert.assertTrue(search.isAutocompleteResultsVisible(!request.isEmpty()), "Результаты поиска не отображаются");
    }

    @Test(description = "3. Check search results")
    public void checkSearchResults() {
        new SelectCountryDialog().closeDialog();
        SearchResults searchResults = new SearchPanel().setSearchText("Java").clickSearchButton();
        new SelectCountryDialog().closeDialog();
        List<String> searchResultTitles = searchResults.getProductsTitles();
        Assert.assertEquals(searchResultTitles.size(), 10, "Должно отображаться только 10 результатов на странице");
        Assert.assertEquals(searchResultTitles.size(), searchResultTitles.stream().filter(x -> x.contains("Java")).count(), "Не все заголовки содержат заданный текст");
        searchResults.checkActionButtonForProductType(ProductType.EBOOK, ProductType.OBOOK);
    }

    @JsonDataFileLocation("data/test4.json")
    @Test(dataProvider = "customDataProvider", description = "4. Check Education page")
    public void checkEducationPage(CustomTestData testData) {
        new SelectCountryDialog().closeDialog();
        new MainMenu().openMenu(testData.breadCrumbs.toArray(new String[0]));
        new SelectCountryDialog().closeDialog();
        List<String> actual = new EducationPage().getSidePanelTitles();
        List<String> expected = testData.menuValues;
        Assert.assertEquals(actual.size(), expected.size(), "Количество пунктов не соответствует");
        Assert.assertEquals(actual, expected, "Пункты не соответствуют");
    }
}
