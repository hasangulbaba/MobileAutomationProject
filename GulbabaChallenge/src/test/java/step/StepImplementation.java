package step;

import com.thoughtworks.gauge.Step;
import driver.DriverCreater;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import methods.Methods;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.Duration;
import static io.appium.java_client.touch.LongPressOptions.longPressOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepImplementation {

    private static Logger logger = LoggerFactory.getLogger(StepImplementation.class);
    private final AppiumDriver appiumDriver;
    private Methods methods;

    public StepImplementation() {
        this.appiumDriver = DriverCreater.appiumDriver;
        methods = new Methods();
    }

    @Step({"<seconds> saniye bekle",
            "<seconds> wait"})
    public void waitBySeconds(long seconds) {
        methods.waitBySeconds(seconds);
    }

    @Step("<key> id li elemente tıkla")
    public void clickElementId(String key) {
        methods.clickElement(methods.getById(key));

    }

    @Step("<key> xpath li elemente tıkla")
    public void clickElementXpath(String key) {
        methods.clickElement(methods.getByXpath(key));

    }

    @Step({"<key> elementininde <expectedText> text değerini taşıyan elementi bul tıkla",
            "On <key> element find the element with <expectedText> value and click"})
    public void getElementTextFindScrollTextClick(String key, String expectedText) {
        methods.getElementTextFindScrollTextClick(methods.getById(key), expectedText);
    }

    @Step({"<key> xpath li elementininde <expectedText> text değerini taşıyan elementi bul tıkla",
            "On <key> element find the element with <expectedText> value and click"})
    public void getElementTextFindScrollTextClickXpath(String key, String expectedText) {
        methods.getElementTextFindScrollTextClick(methods.getByXpath(key), expectedText);
    }

    @Step("notificationları kontrol et")
    public void checkForNotifications() {
        AndroidDriver driver = ((AndroidDriver) appiumDriver);
        driver.openNotifications();

    }

    @Step("Apk sil ve yükle")
    public void deleteAndReinstallApp() {
        AndroidDriver driver = ((AndroidDriver) appiumDriver);
        // Remove the app
        driver.removeApp("com.hmh.api");
    }

    @Step({"<key> elementinin text değeri <expectedText> değerine eşit mi",
            "Is the text value of <key> element is equal to <expectedText>"})
    public void getElementText(String key, String expectedText) {
        methods.waitByMilliSeconds(250);
        String actualText = methods.getText(methods.getById(key)).trim()
                .replace("\r", "").replace("\n", "");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        assertEquals(expectedText.replace(" ", "")
                , actualText.replace(" ", ""), "Text değerleri eşit değil");
        logger.info("Text değerleri eşit");
    }

    @Step({"<key> elementinin text değeri <expectedText> değerini içeriyor mu",
            "Does the text value of <key> element contains <expectedText>"})
    public void getElementTextContain(String key, String expectedText) {
        methods.waitByMilliSeconds(250);
        String actualText = methods.getTextToElement(methods.getByXpath(key)).trim()
                .replace("\r", "").replace("\n", "");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        Assertions.assertTrue(actualText.replace(" ", "")
                .contains(expectedText.replace(" ", "")), "Text değerleri eşit değil");
        logger.info("Text değerleri eşit");
    }

    @Step({"<key> elementine <text> değerini yaz",
            "<key> element send <text> text"})
    public void sendKeysElement(String key, String text) {
        methods.sendKeysToElement(methods.getById(key), text);
    }

    @Step({"<key> elementinde <index> sırasındaki değere tıkla"})
    public void clickElement(String key, int index) {
        methods.clickElementIndex(methods.getById(key), index);
    }

    @Step({"<key> elementin text alanını temizle",
            "clear text <key> element"})
    public void clearElementText(String key) {
        methods.clearToElement(methods.getById(key));
    }

    @Step("<key> id li elementini değerini <value> map ekle")
    public void getTextValuePutMapId(String key, String value) {
        methods.waitByMilliSeconds(250);
        methods.putValueInTestMapElementGetTextId(key, value);
    }

    @Step("<key> xpath li elementin değerlerini <value> map ekle")
    public void getTextValuePutMapXpath(String key, String value) {
        methods.waitByMilliSeconds(250);
        methods.putValueInTestMapElementGetTextandAttributeXpath(key, value);
    }

    @Step("<key> elementinin text değeri ile map <mapKey> gelen değer eşit mi")
    public void getElementTextEqualMapText(String key, String mapKey) {
        String actualText = methods.getTextToElement(methods.getById(key)).replace("\r", "").replace("\n", "");
        logger.info("Alınan text: " + actualText);
        String value = methods.getValueInTestMap(mapKey);
        logger.info("Beklenen text: " + value);
        Assert.assertEquals("Text değerleri eşit değil", value, actualText);
        logger.info("Text değerleri eşit");
    }

    @Step("<key> xpath li elementinin text değeri ile map <mapKey> gelen değer eşit mi")
    public void getElementTextEqualMapTextXpath(String key, String mapKey) {
        String index;
        String commandText;
        String newActualText;
        int temp;
        String actualText = methods.getTextToElement(methods.getByXpath(key)).replace("\r", "").replace("\n", "");

        String[] splitText;
        splitText = actualText.trim().split(",");

        commandText = splitText[1];
        String[] splitText2 = splitText[0].split(":");
        index = splitText2[1];
        temp = Integer.parseInt(index);
        temp = temp+1;
        index = String.valueOf(temp);

        newActualText = commandText+index;
        System.out.println(newActualText);

        logger.info("Alınan text: " + actualText);
        logger.info("Oluşturulan text: " + newActualText);
        String value = methods.getValueInTestMap(mapKey);
        logger.info("Beklenen text: " + value);
        Assert.assertEquals("Text değerleri eşit değil", value, newActualText);
        logger.info("Text değerleri eşit");

        /*Açıklama : Command Two seçildi
        *         command two map e eklenir
        *         selected:1 ,CommandTwo bilgilendirilmesi verildi
        *         Bilgilendirme de gelen textten command two ve index bilgisi alınıp yeni text oluşturulur -> commandTwo1
        *         map e eklenen değer ile karşılaştırıldı
        * */
    }

    @Step("<key> id li elementinin görünür olduğunu kontrol et")
    public void checkElementVisibleId(String key) throws IOException {

        assertTrue("Element görünür değil", methods.isElementVisible(methods.getById(key), 30));
    }

    @Step("<key> xpath li elementinin görünür olduğunu kontrol et")
    public void checkElementVisibleXpath(String key) throws IOException {

        assertTrue("Element görünür değil", methods.isElementVisible(methods.getByXpath(key), 30));
    }

    @Step("<key> id li elementinin görünür olmadığını kontrol et")
    public void checkElementInvisibleId(String key) {

        assertTrue("Element görünür", methods.isElementInvisible(methods.getById(key), 30));
    }

    @Step("<key> xpath li elementinin görünür olmadığını kontrol et")
    public void checkElementInvisibleXpath(String key) {

        assertTrue("Element görünür", methods.isElementInvisible(methods.getByXpath(key), 30));
    }

    @Step("<x>,<y> koordinatlarındaki elemente basılı tut <mils> kadar bekle")
    public void performPressAndHoldAction(int x,int y,long mils) {
        new TouchAction((AndroidDriver)appiumDriver).longPress(longPressOptions().withPosition(point(x,y)).withDuration(Duration.ofMillis(mils))).release().perform();

    }

    @Step("Sağa kaydır <key> id li elementini bulana kadar")
    public void swipeRightUntilFoundId(String key) {
        methods.horizontalScroll(methods.getById(key));

    }

    @Step("Sağa kaydır <key> xpath li elementini bulana kadar")
    public void swipeRightUntilFoundXpath(String key) {
        methods.horizontalScroll(methods.getByXpath(key));

    }

    @Step("Swipedown <key> element <text> bulana kadar")
    public void swipeDownUntilFoundText(String key, String text) {
        while (!methods.getElementTextVisibleCheckTextControl(key, text)) {
            methods.doSwipe();
        }
    }

}
