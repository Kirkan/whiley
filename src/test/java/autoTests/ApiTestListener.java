package autoTests;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApiTestListener extends TestListenerAdapter {
    private static final Logger LOG = LogManager.getLogger(ApiTestListener.class);

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
        saveTextLog(getStackTrace(Reporter.getCurrentTestResult().getThrowable()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        LOG.info(String.format("Test SKIPPED: %s . On thread: %s", iTestResult.getName(), Thread.currentThread().getName()));
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Logs", type = "text/plain", fileExtension = ".txt")
    private String saveTextLog(String message) {
        return message;
    }

}


