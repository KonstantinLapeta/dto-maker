package swagger_scanner.util.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private static String getProperty(String propertyName) {
        var properties = new Properties();

        try {
            properties.load(new FileInputStream(getResourcesPath() + "config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties.getProperty(propertyName);
    }

    public static String getDtoPath() {
        return System.getProperty("user.dir") + getProperty("save.dto.to.path");
    }

    public static String getSwaggerUrl() {
        return getProperty("swagger.json.url");
    }

    public static String getPrefixClassName() {
        return getProperty("prefix.to.dto.class.name");
    }

    public static String getResourcesPath() {
        return System.getProperty("user.dir") + "/src/main/resources/";
    }
}
