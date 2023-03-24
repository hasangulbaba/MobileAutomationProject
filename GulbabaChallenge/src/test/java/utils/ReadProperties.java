package utils;

import driver.DriverCreater;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ReadProperties {
    public static ResourceBundle readProp(String systemSourcesDir) {

        ResourceBundle bundle = null;

        try {
            String propertyDir = DriverCreater.userDir + "/src/test/resources/properties/" + systemSourcesDir;
            InputStream propertiesStream = new FileInputStream(propertyDir);
            bundle = new PropertyResourceBundle(new InputStreamReader(propertiesStream, StandardCharsets.UTF_8));
            propertiesStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
