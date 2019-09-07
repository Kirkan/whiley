package autoTests.PageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;

public class EducationPage {

    //region Поля
    private SelenideElement mainContainer = $x(".//div[contains(@class, 'main-page-container')]");
    private SelenideElement pageHeader = mainContainer.$x(".//div[@class='hero-banner']//div[@class='wiley-slogan']/h1/*[normalize-space()]");
    private SelenideElement sidePanel = mainContainer.$x(".//div[@class='side-panel']");

    //endregion

    public EducationPage() {
        mainContainer.shouldBe(Condition.visible.because("Страница должна отображаться"));
        //Check “Education” header is displayed
        pageHeader.shouldHave(Condition.text("Education").because("Заголовок отображается неверно"));
    }

    //region Методы
    @Step("Получение списка заголовков боковой панели")
    public List<String> getSidePanelTitles() {
        return sidePanel.$$x("./ul/li/a").stream().map(x -> x.innerText().trim()).collect(Collectors.toList());
    }
    //endregion

}
