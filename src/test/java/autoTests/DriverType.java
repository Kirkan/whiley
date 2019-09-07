package autoTests;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DriverType {
    CHROME("chrome") {
        @Override
        public MutableCapabilities getCapabilities() {
            ChromeOptions chromeOptions = new ChromeOptions();
            //chromeOptions.addArguments("disable-popup-blocking", "true");
            chromeOptions.addArguments("disable-infobars");
            chromeOptions.addArguments("--disable-web-security");
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.default_content_settings.popups", 0);
            chromeOptions.setExperimentalOption("prefs", prefs);
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            chromeOptions.setExperimentalOption("useAutomationExtension", false);
            return chromeOptions;
        }

        @Override
        public WebDriver createWebDriver(MutableCapabilities capabilities) {
            ChromeDriverManager.chromedriver().setup();
            return new ChromeDriver((ChromeOptions) capabilities);
        }
    };

    private String browserName;

    DriverType(String browserName) {
        this.browserName = browserName;
    }

    @Override
    public String toString() {
        return this.browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static DriverType fromString(String browserName) {
        if (browserName.equals(CHROME.toString())) {
            return CHROME;
        } else {
            throw new RuntimeException("Could not determine driver type by text: " + browserName);
        }
    }

    public abstract MutableCapabilities getCapabilities();

    public abstract WebDriver createWebDriver(MutableCapabilities capabilities);
}
