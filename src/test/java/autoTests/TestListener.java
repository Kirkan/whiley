package autoTests;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class TestListener extends TestListenerAdapter {
    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("Test class started: " + result.getTestClass().getName());
        LOG.info(String.format("Test started: %s . On thread: %s", result.getName(), Thread.currentThread().getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info(String.format("Test SUCCESS: %s . On thread: %s", result.getName(), Thread.currentThread().getName()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.info(String.format("Test FAILED: %s . On Thread: %s", result.getName(), Thread.currentThread().getName()));
        saveScreenshot();
        saveTextLog(getStackTrace(Reporter.getCurrentTestResult().getThrowable()));
        saveBrowserTextLog(getBrowserLogs().stream().map(String::new).collect(Collectors.joining("\n")));
    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        if (WebDriverRunner.getWebDriver() != null) {
            WebDriverRunner.getWebDriver().quit();
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOG.info(String.format("Test SKIPPED: %s . On thread: %s", iTestResult.getName(), Thread.currentThread().getName()));
        if (WebDriverRunner.getWebDriver() != null) {
            WebDriverRunner.getWebDriver().quit();
        }
    }

    private static List<String> getBrowserLogs() {
        LogEntries logEntries = WebDriverRunner.getWebDriver().manage().logs().get("browser");
        return logEntries.getAll().stream().map(LogEntry::toString).collect(Collectors.toList());
    }

    private byte[] takeScreenshot(WebDriver driver) {
        byte[] imageInBytes = new byte[0];
        BufferedImage screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver).getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenshot, "png", baos);
            imageInBytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInBytes;
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    private byte[] saveScreenshot() {
        return takeScreenshot(getWebDriver());
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Logs", type = "text/plain", fileExtension = ".txt")
    private String saveTextLog(String message) {
        return message;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Browser Logs", type = "text/plain", fileExtension = ".txt")
    private String saveBrowserTextLog(String message) {
        return message;
    }
}


