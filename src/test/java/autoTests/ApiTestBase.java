package autoTests;

import com.codeborne.selenide.testng.TextReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

@Listeners({ApiTestListener.class, TextReport.class})
public class ApiTestBase {
    private static final Logger LOG = LogManager.getLogger(ApiTestBase.class);

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

}