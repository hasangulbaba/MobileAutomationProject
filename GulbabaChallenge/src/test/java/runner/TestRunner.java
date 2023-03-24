package runner;

import com.thoughtworks.gauge.*;
import driver.DriverCreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestRunner {
    DriverCreater driverCreater;
    public static String testCaseName = "";
    public static String testClassName = "";
    public static String userDir = System.getProperty("user.dir");
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    public TestRunner() {
        driverCreater = new DriverCreater();

    }

    @BeforeSuite
    public void beforeSuite() {

        logger.info("*************************************************************************");
        logger.info("------------------------TEST PLAN-------------------------");
        System.out.println("\r\n");
        driverCreater.beforePlan();
    }


    @BeforeSpec
    public void beforeSpec(ExecutionContext executionContext) {

        logger.info("=========================================================================");
        logger.info("------------------------SPEC-------------------------");
        String fileName = executionContext.getCurrentSpecification().getFileName();
        testClassName = fileName.replace(userDir, "");
        logger.info("SPEC FILE NAME: " + fileName);
        logger.info("SPEC NAME: " + executionContext.getCurrentSpecification().getName());
        logger.info("SPEC TAGS: " + executionContext.getCurrentSpecification().getTags());
        System.out.println("\r\n");
    }

    @BeforeScenario
    public void beforeScenario(ExecutionContext executionContext) {


        logger.info("_________________________________________________________________________");
        logger.info("------------------------SCENARIO-------------------------");
        testCaseName = executionContext.getCurrentScenario().getName();
        logger.info("SCENARIO NAME: " + testCaseName);
        logger.info("SCENARIO TAG: " + executionContext.getCurrentScenario().getTags().toString());
        System.out.println("\r\n");
        driverCreater.beforeTest(executionContext);
    }


    @BeforeStep
    public void beforeStep(ExecutionContext executionContext) {

        logger.info("═════════  " + executionContext.getCurrentStep().getDynamicText() + "  ═════════");
    }

    @AfterStep
    public void afterStep(ExecutionContext executionContext) {

        if (executionContext.getCurrentStep().getIsFailing()) {

            logger.error(executionContext.getCurrentSpecification().getFileName());
            logger.error("Message: " + executionContext.getCurrentStep().getErrorMessage() + "\r\n"
                    + executionContext.getCurrentStep().getStackTrace());
        }
        logger.info("═════════════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("\r\n");
    }

    @AfterScenario
    public void afterScenario(ExecutionContext executionContext) throws IOException {
        logger.info("_________________________________________________________________________");

        driverCreater.afterTest(executionContext);
        if (executionContext.getCurrentScenario().getIsFailing()) {

            logger.info("TEST BAŞARISIZ");
        } else {

            logger.info("TEST BAŞARILI");
        }


        logger.info("_________________________________________________________________________");
        System.out.println("\r\n");
    }

    @AfterSpec
    public void afterSpec() {

        logger.info("=========================================================================");


        logger.info("=========================================================================");
        System.out.println("\r\n");
    }

    @AfterSuite
    public void afterSuite() {
        driverCreater.afterPlan();
        logger.info("*************************************************************************");
        System.out.println("\r\n");
    }
}
