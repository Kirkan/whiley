package autoTests.PageObjects;

import autoTests.ProductType;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$x;

public class SearchResults {

    //region Поля
    private SelenideElement mainContainer = $x(".//div[@id='search-result-page-row']");
    private SelenideElement searchResultContainer = mainContainer.$x(".//div[@class='search-result-tabs-wrapper']");
    private SelenideElement productListContainer = searchResultContainer.$x(".//div[@class='product-list-wrapper']");
    private SelenideElement productsList = productListContainer.$x(".//div[@class='products-list']");
    private ElementsCollection productsListItems = productsList.$$x("./section[@class='product-item']");
    //    private String productTypeLocator = "//div[@class='product-content']//section[@id='productTableBodySection']//div[contains(@id, '%s')]/div[@class='product-table-row'][.//span[@class='product-type-name'][normalize-space(text())='%s']]";
    private String productTypeLocator = "//div[@class='product-table-row'][.//span[@class='product-type-name'][normalize-space(text())='%s']]";
    //endregion

    public SearchResults() {
        mainContainer.shouldBe(Condition.visible.because("Панель с результатами поиска должна отображаться"));
    }

    //region Методы
    @Step("Проверка видимости кнопок для типа продукта {types}")
    public void checkActionButtonForProductType(ProductType... types) {
        SoftAssert soft = new SoftAssert();
        for (ProductType productType : types) {
            for (SelenideElement productItem : productsListItems) {
                SelenideElement productTypeRow = productItem.scrollIntoView(false).$x(String.format(productTypeLocator, productType.getType()));
                boolean actionButtonIsDisplayed = productTypeRow.$x(String.format(".//div[@class='product-button']%s", productType.getLocator())).isDisplayed();
                soft.assertTrue(actionButtonIsDisplayed, String.format("Кнопка '%s' не отображается для типа '%s'", productType.getLabel(), productType.getType()));
            }
        }
        soft.assertAll();
    }

    @Step("Получение заголовков из списка продуктов")
    public List<String> getProductsTitles() {
        return productsListItems.stream().map(a -> a.$x("./div[@class='product-content']/h3[@class='product-title']/a[.]").innerText().trim()).collect(Collectors.toList());
    }

    //endregion

}
