package autoTests.Tests;

import autoTests.ApiTestBase;
import autoTests.Config;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

@Epic("API Tests")
public class APITests extends ApiTestBase {

    @Test(description = "1a. Check GET response")
    public void checkGETResponse() {
        JsonPath responseJson = RestAssured.given().param("term", "Java")
                .when().get(Config.getInstance().getBaseUrlApi())
                .then().extract().jsonPath();
        Assert.assertEquals(responseJson.getList("suggestions").size(), 4, "Количество элементов 'suggestions' меньше ожидаемого");
        Assert.assertTrue(responseJson.getList("suggestions.term").stream().map(Object::toString).allMatch(x -> x.contains("<span class=\"search-highlight\">java</span>")), "Элементы 'term' списка 'suggestions' не содержат требуемую строку");
        Assert.assertEquals(responseJson.getList("products").size(), 4, "Количество элементов 'products' меньше ожидаемого");
        Assert.assertTrue(responseJson.getList("products.name").stream().map(Object::toString).allMatch(x -> x.contains("<span class='search-highlight'>Java</span>")), "Элементы 'name' списка 'products' не содержат требуемую строку");
        Assert.assertEquals(responseJson.getList("pages").size(), 4, "Количество элементов 'pages' меньше ожидаемого");
        Assert.assertTrue(responseJson.getList("pages.title").stream().map(Object::toString).allMatch(x -> x.contains("Wiley")), "Элементы 'title' списка 'pages' не содержат требуемую строку");
    }

    @Test(description = "1b. Check GET response. Image")
    public void checkGETResponseImage() throws IOException {
        JsonPath responseJson = RestAssured.given().param("term", "Java")
                .when().get(Config.getInstance().getBaseUrlApi())
                .then().extract().jsonPath();
        Assert.assertEquals(ImageIO.read(new URL(responseJson.get("products[0].images[0].url").toString())).getWidth(), 300, "Размер изображения не соответствует ожидаемому");
    }

}
