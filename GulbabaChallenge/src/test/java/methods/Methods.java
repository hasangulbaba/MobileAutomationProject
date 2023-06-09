package methods;

import driver.DriverCreater;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import static java.time.Duration.ofMillis;
import static org.openqa.selenium.interactions.PointerInput.Kind.TOUCH;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;


public class Methods {

    private static Logger logger = LoggerFactory.getLogger(Methods.class);
    private AppiumDriver appiumDriver;
    private FluentWait wait;

    private By by;
    private long pollingMillis;

    private final static PointerInput FINGER = new PointerInput(TOUCH, "finger");


    public Methods() {
        this.appiumDriver = DriverCreater.appiumDriver;
        wait = setFluentWait(30);
        pollingMillis = 300;

    }

    public FluentWait setFluentWait(long timeout) {

        FluentWait fluentWait = new FluentWait(appiumDriver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public By getById(String key){
        return By.id(key);
    }

    public By getByclass(String key){
        return By.className(key);
    }

    public By getByXpath(String key){
        return By.xpath(key);
    }

    public WebElement findElement(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> findElements(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (List<WebElement>) wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public String getText(By by) {
        return findElement(by).getText();
    }

    public void clickElementIndex(By by, int index) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        findElements(by).get(index).click();
    }

    public void clearToElement(By by) {

        findElement(by).clear();
    }

    public boolean isElementVisible(By by, int timeout) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        try {
            setFluentWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementInvisible(By by, int timeout) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        try {
            setFluentWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTextToElement(By by) {
        return findElement(by).getText();
    }

    public String getValueInTestMap(String key) {

        return DriverCreater.TestMap.get(key).toString();
    }

    public void putValueInTestMap(String key, Object object) {

        DriverCreater.TestMap.put(key, object);
    }

    public void putValueInTestMapElementGetTextId(String key, String parameter) {
        putValueInTestMap(parameter, getTextToElement(getById(key)));
        logger.info("Map değerine paramtere: " + parameter + " değeri " + getTextToElement(getById(key)) + " eklendi");
    }

    public void putValueInTestMapElementGetTextandAttributeXpath(String key, String parameter) {
        putValueInTestMap(parameter, getTextToElement(getByXpath(key))+getAttribute(getByXpath(key),"index"));
    }

    public String getAttribute(By by, String attribute) {
        return findElement(by).getAttribute(attribute);
    }

    public void sendKeysToElement(By by, String text) {
        findElement(by).clear();
        findElement(by).sendKeys(text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public void clickElement(By by) {
        findElement(by).click();
        logger.info("Elemente tıklandı.");
    }

    public void waitByMilliSeconds(long milliSeconds) {

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds) {

        logger.info(seconds + " saniye bekleniyor...");
        waitByMilliSeconds(seconds * 1000);
    }

    public WebElement getElementList(By by, String value, String option, Boolean scroolTxt) {
        WebElement element = null;
        while (element == null) {
            element = getElementLists(by, value, option, scroolTxt);
            if (element == null) {
                if (element.isDisplayed() == false) {
                    return null;
                }
            }
        }
        return element;
    }

    private WebElement getElementLists(By by, String value, String option, Boolean scroolTxt) {

        int rowCount = 0;

        List<WebElement> allRows = findElements(by);

        rowCount = allRows.size();


        if (rowCount == 0)

            return null;

        WebElement elem = null;

        for (WebElement row : allRows) {
            try {
                elem = row;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (check(elem, value, option)) {
                logger.info(value + " değeri taşıyan element satırı bulundu. Kontrolü başarılı!");
                return row;
            }
        }

        throw new java.util.NoSuchElementException("Elementi alınmak istenen Listenin içerisindeki satırın elementi bulunamadı! Kontrol ediniz.");
    }

    private boolean check(WebElement element, String value, String option) {
        boolean exit = false;

        switch (option) {
            case "text":
                exit = element.getText().contains(value);
                break;
            case "value":
                exit = element.getAttribute(option).contains(value);
                break;

        }
        return exit;
    }

    public void getElementTextFindScrollTextClick(By by, String expectedText) {
        waitByMilliSeconds(250);
        WebElement element = getElementList(by, expectedText, "text", true);
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + element.getText());
        Assert.assertTrue("Beklenen değer kontrolü başarılı", element.getText().contains(expectedText) );
        element.click();
        logger.info(expectedText + " değerine tıklandı.");
    }


    public void horizontalScroll(By by) {
        WebElement element = findElement(by);
        int centerY = element.getRect().y + (element.getSize().height / 2);
        double startX = element.getRect().x + (element.getSize().width * 0.9);
        double endX = element.getRect().x + (element.getSize().width * 0.1);
        PointerInput finger = new PointerInput(TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofSeconds(0), viewport(), (int) startX, centerY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(ofMillis(700), viewport(), (int) endX, centerY));
        swipe.addAction(finger.createPointerUp(0));
        appiumDriver.perform(Arrays.asList(swipe));
    }

    public void doSwipe() {
        Dimension dimension = appiumDriver.manage().window().getSize();
        Point start = new Point((int) (dimension.width * 0.5), (int) (dimension.height * 0.5));
        Point end = new Point((int) (dimension.width * 0.2), (int) (dimension.height * 0.1));
        Sequence swipe = new Sequence(FINGER, 1)
                .addAction(FINGER.createPointerMove(ofMillis(0), viewport(), start.getX(), start.getY()))
                .addAction(FINGER.createPointerDown(LEFT.asArg()))
                .addAction(FINGER.createPointerMove(ofMillis(1000), viewport(), end.getX(), end.getY()))
                .addAction(FINGER.createPointerUp(LEFT.asArg()));
        appiumDriver.perform(Arrays.asList(swipe));
    }

    public boolean getElementTextVisibleCheckTextControl(String key, String text) {
        waitByMilliSeconds(250);
        if (isElementVisible(getById(key), 2)) {
            List<WebElement> elements = findElements(getById(key));
            for (WebElement element : elements) {
                if (element.getText().contains(text)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
