package swagger_scanner.util.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static swagger_scanner.util.Log.log;
import static swagger_scanner.util.reader.PropertyReader.getResourcesPath;

public class TemplateReader {
    public String getTemplate(String fileName) {
        var stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(getResourcesPath() + fileName + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            log().error(e.getStackTrace());
        }
        return String.valueOf(stringBuilder);
    }
}
