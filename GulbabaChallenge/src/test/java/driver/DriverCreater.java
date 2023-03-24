package driver;

import com.thoughtworks.gauge.ExecutionContext;
import io.appium.java_client.AppiumDriver;
import junit.textui.TestRunner;
import methods.Methods;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.SessionNotCreatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import step.StepImplementation;
import utils.ReadProperties;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.log4j.Logger.getLogger;
import static org.apache.log4j.Logger.getRootLogger;

public class DriverCreater {
    private static Logger logger = LoggerFactory.getLogger(DriverCreater.class);
    public static AppiumDriver appiumDriver;

    public static boolean isPlatform =true;
    public static String userDir = System.getProperty("user.dir");
    public static ResourceBundle appEnvProperties;
    public static ResourceBundle baseProperties = ReadProperties.readProp("base.properties");
    public static String desktopPlatformName;
    public static String osName = FindOS.getOperationSystemName();
    public static String slash = osName.equals("WINDOWS") ? "\\" : "/";
    public static String env = "";

    public static ConcurrentHashMap<String, Object> TestMap;

    public void setAppEnvironment() {
        logger.info("═════════════════════════Mobil Platform════════════════════════════════");

        appEnvProperties = ReadProperties.readProp("test.properties");

        System.setProperty("packageId", appEnvProperties.getString("AppPackageId"));

    }


    public void setEnv() {

        env = "local";
    }

    public void beforePlan() {
        setEnv();
        setAppEnvironment();

    }

    public void beforeTest(ExecutionContext executionContext) {
        TestMap = new ConcurrentHashMap<String, Object>();
        setLoggerConfig();

        try {
            createDriver();
        } catch (Throwable e) {

            StackTraceElement[] stackTraceElements = e.getStackTrace();
            String error = e.toString() + "\r\n";
            for (int i = 0; i < stackTraceElements.length; i++) {

                error = error + "\r\n" + stackTraceElements[i].toString();
                logger.info("App Failure " + error);
            }
            throw new SessionNotCreatedException(error);

        }
    }


    public void createDriver() throws MalformedURLException {


        desktopPlatformName = FindOS.getOperationSystemNameExpanded();

        appiumDriver = LocalAppExec.localAppDriver();

        logger.info("Mobile uygulama ayağa kaldırıldı.");


    }


    public void afterTest(ExecutionContext executionContext) throws IOException {

        if (Boolean.parseBoolean(baseProperties.getString("localQuitDriverActive"))) {
            quitDriver();
        }
    }



    public void afterPlan() {

        System.out.println("");
    }


    public void quitDriver() {

        appiumDriver.quit();
        logger.info("DriverCreater kapatıldı.");
    }


    public void setLoggerConfig() {
        String dir = "/src/test/resources/properties/log4j.properties";
        if (!slash.equals("/")) {
            dir = dir.replace("/", "\\");
        }
        PropertyConfigurator.configure(userDir + dir);
        String logLevel = baseProperties.getString("logLevel");
        getRootLogger().setLevel(Level.toLevel(logLevel));

        if (!logLevel.equals("ALL")) {
            String methodsClassLogLevel = baseProperties.getString("methodsClassLogLevel");

            getLogger(DriverCreater.class).setLevel(Level.ALL);
            getLogger(FindOS.class).setLevel(Level.ALL);
            getLogger(LocalAppExec.class).setLevel(Level.ALL);
            getLogger(Methods.class).setLevel(Level.toLevel(methodsClassLogLevel));
            getLogger(TestRunner.class).setLevel(Level.ALL);
            getLogger(StepImplementation.class).setLevel(Level.toLevel(methodsClassLogLevel));
            getLogger(ReadProperties.class).setLevel(Level.toLevel(methodsClassLogLevel));
            getLogger(TestRunner.class).setLevel(Level.toLevel(methodsClassLogLevel));
        }
    }
}
