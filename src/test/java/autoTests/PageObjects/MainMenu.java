package autoTests.PageObjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class MainMenu {
    //region Поля
    private SelenideElement navBar = $x(".//nav[@id='main-header-navbar']");
    private SelenideElement menuItems = navBar.$x(".//ul[contains(@class, 'navigation-menu-items')]");
    private SelenideElement menuItem;
    //endregion

    public MainMenu() {
        navBar.shouldBe(Condition.visible.because("Панель навигации должна отображаться"));
    }

    //region Методы
    @Step("Открываем меню: {breadCrumbs}")
    public void openMenu(String... breadCrumbs) {
        if (breadCrumbs.length >= 1) {
            openDropDownMenu(breadCrumbs[0]).hover();
            sleep(500);
        } else {
            openDropDownMenu(breadCrumbs[0]).click();
        }
        if (breadCrumbs.length <= 2) {
            openDropDownMenuItem(breadCrumbs[1]).click();
        } else {
            openDropDownMenuItem(breadCrumbs[1]).hover();
            sleep(500);
        }
        if (breadCrumbs.length == 3) {
            openDropDownMenuSubItem(breadCrumbs[2]).click();
        }
    }

    @Step("Получаем список пунктов выбранного подменю")
    public List<String> getMainMenuItemLabels(String menuLabel) {
        SelenideElement element = menuItems.$x(String.format(".//li[@class='dropdown-submenu'][a[contains(.,'%s')]]", menuLabel));
        return element.$$x("./div/ul/li/a").stream().map(x -> x.innerText().trim()).collect(Collectors.toList());
    }

    private SelenideElement openDropDownMenuSubItem(String menuLabel) {
        return menuItem.$x(String.format(".//ul[contains(@class, 'dropdown-items')]/ul[@class='sub-list']/li[contains(@class, 'dropdown-item')]/a[contains(.,'%s')]", menuLabel));
    }

    private SelenideElement openDropDownMenuItem(String menuLabel) {
        menuItem = menuItems.$x(String.format("//ul[contains(@class, 'dropdown-items')]/li[contains(@class, 'dropdown-item')][a[contains(.,'%s')]]", menuLabel));
        return menuItem.scrollIntoView(false).$("a");
    }

    private SelenideElement openDropDownMenu(String menuLabel) {
        SelenideElement menuItem = menuItems.$x(String.format(".//li[@class='dropdown-submenu'][a[contains(.,'%s')]]", menuLabel));
        return menuItem.$("a");
    }


//endregion

}
