package autoTests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.testng.TextReport;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Listeners({TestListener.class, TextReport.class})
public class TestBase {
    private static final Logger LOG = LogManager.getLogger(TestBase.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        LOG.info(String.format("Тестовых методов для запуска %s. Tread: %s", context.getAllTestMethods().length,
                Thread.currentThread().getName()));
    }

    @BeforeMethod(alwaysRun = true)
    public void testMethodInfo(Method method) {
        LOG.info(String.format("Run test method: %s.%s in thread: %s ", method.getDeclaringClass().getName(),
                method.getName(), Thread.currentThread().getName()));
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "testMethodInfo")
    @Parameters({"browser"})
    public void browserLaunch(@Optional("chrome") String browser) {
        LOG.info("BROWSER: " + browser);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.startMaximized = true;
        Configuration.screenshots = false;
        Configuration.fastSetValue = true;
        Configuration.reportsFolder = "target/reports";
        startBrowser(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        LOG.info("Closing browser");
        if (WebDriverRunner.getWebDriver() != null) {
            WebDriverRunner.getWebDriver().quit();
        }
    }

    private void startBrowser(String browserName) {
        DriverType driverType = DriverType.fromString(browserName);
        WebDriverRunner.setWebDriver(getWebDriver(driverType, getCapabilities(driverType)));
    }

    @DataProvider(name = "customDataProvider")
    public Object[] customDataModel(Method m, ITestContext ctx) {
        Parameter[] parameters = m.getParameters();
        return dataModelFromJson(m, parameters[0].getType());
    }

    public Object[] dataModelFromJson(Method m, Class modelClass) {
        String fileName = m.getDeclaredAnnotation(JsonDataFileLocation.class).value();
        Object o;
        try {
            String resource = getClass().getClassLoader().getResource(fileName).getFile();
            o = new Gson().fromJson(new JsonReader(new FileReader(resource)), modelClass);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No json found in " + fileName + ". Check file location");
        }
        return new Object[]{o};
    }

    protected WebDriver getWebDriver(DriverType driverType, MutableCapabilities capabilities) {
        WebDriver driver = driverType.createWebDriver(capabilities);
        driver.manage().window().maximize();
        return driver;
    }

    protected MutableCapabilities getCapabilities(DriverType driverType) {
        return driverType.getCapabilities().merge(Configuration.browserCapabilities);
    }

}