package autoTests.PageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class SearchPanel {

    //region Поля
    private SelenideElement mainContainer = $x(".//div[@class='main-navigation-search']//form");
    private SelenideElement edSearch = mainContainer.$x(".//input[@id='js-site-search-input']");
    private SelenideElement btnSubmit = mainContainer.$x(".//button");
    private SelenideElement autocompleteContainer = mainContainer.$x(".//aside[contains(@class, 'ui-autocomplete')]");
    //endregion

    public SearchPanel() {
        mainContainer.shouldBe(Condition.visible.because("Панель поиска должна отображаться"));
    }

    //region Методы
    @Step("Заполняем поле Поиск")
    public SearchPanel setSearchText(String value) {
        edSearch.shouldBe(Condition.visible.because("Поле для ввода текста должно отображаться")).sendKeys(value);
        return this;
    }

    @Step("Нажимаем кнопку Поиск")
    public SearchResults clickSearchButton() {
        btnSubmit.shouldBe(Condition.visible.because("Кнопка Поиск должна отображаться")).click();
        return new SearchResults();
    }

    @Step("Проверка видимости контекстных результатов поиска")
    public boolean isAutocompleteResultsVisible(boolean waitResults) {
        if (waitResults) {
            return autocompleteContainer.shouldBe(Condition.visible).has(Condition.visible);
        } else {
            sleep(500);
            return autocompleteContainer.isDisplayed();
        }
    }

    //endregion

}
