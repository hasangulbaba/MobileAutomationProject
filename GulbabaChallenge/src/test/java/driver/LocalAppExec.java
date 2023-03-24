package driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class LocalAppExec {

    private static AppiumDriver appiumDriver = null;
    private static Logger logger = LoggerFactory.getLogger(LocalAppExec.class);

    public static AppiumDriver localAppDriver() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        String appiumHub = DriverCreater.baseProperties.getString("localAppiumHub");
        String platform = DriverCreater.baseProperties.getString("Platform");

        switch (platform) {
            case "Android":
                appiumDriver = androidDriver(appiumHub, platform, desiredCapabilities);
                DriverCreater.isPlatform = true;
                break;
            case "Ios":
                appiumDriver = iosDriver(appiumHub, platform, desiredCapabilities);
                DriverCreater.isPlatform = false;
                break;

        }

        return appiumDriver;

    }


    private static AppiumDriver androidDriver(String appiumHub, String platform, DesiredCapabilities desiredCapabilities) throws MalformedURLException {
        logger.info("platform: Android");

        String version = DriverCreater.baseProperties.getString("Version");
        String udid = DriverCreater.baseProperties.getString("UDID");
        String appPackage = DriverCreater.appEnvProperties.getString("AppPackage");
        String appActivity = DriverCreater.appEnvProperties.getString("AppActivity");

        desiredCapabilities.setCapability("appium:app","C:/Github/GulbabaChallenge/apk/presentation-beta-debug (4).apk");
        desiredCapabilities.setCapability("appium:platformName", "Android");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, udid);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
        desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
        desiredCapabilities.setCapability("enableMultiWindows", true);
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);

        desiredCapabilities.setCapability("no-reset", "true"); // (noReset=true)
        desiredCapabilities.setCapability("full-reset", "false");
        desiredCapabilities.setCapability("unicodeKeyboard", "true");
        desiredCapabilities.setCapability("resetKeyboard", "true");
        URL url = new URL(appiumHub);
        return new AndroidDriver(url, desiredCapabilities);



    }


    private static AppiumDriver iosDriver(String appiumHub, String platform, DesiredCapabilities desiredCapabilities) throws MalformedURLException {
        logger.info("platform: iOS");

        String browserName = DriverCreater.baseProperties.getString("iosBrowserName");
        String UDID = DriverCreater.baseProperties.getString("iosUDID");
        String deviceName = DriverCreater.baseProperties.getString("iosDeviceName");
        String iosVersion = DriverCreater.baseProperties.getString("iosVersion");

        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platform);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosVersion);
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        desiredCapabilities.setCapability(MobileCapabilityType.UDID, UDID);
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);

        return new IOSDriver(new URL(appiumHub), desiredCapabilities);

    }

}
