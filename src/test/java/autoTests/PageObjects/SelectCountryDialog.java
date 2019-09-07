package autoTests.PageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class SelectCountryDialog {
    //region Поля
    private SelenideElement mainView = $x(".//div[@id='selectCountryModalWnd']");
    private SelenideElement btnClose = mainView.$x(".//form//button[@class='close']");
    //endregion

    //region Методы
    @Step("Закрываем диалог выбора страны")
    public void closeDialog() {
        if (mainView.isDisplayed())
            btnClose.shouldBe(Condition.visible.because("Кнопка закрытия диалога должна отображаться")).click();
    }
    //endregion

}
