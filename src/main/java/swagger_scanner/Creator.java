package swagger_scanner;


import swagger_scanner.util.reader.TemplateReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static swagger_scanner.Types.getMatchedValue;
import static swagger_scanner.util.Log.*;
import static swagger_scanner.util.reader.PropertyReader.*;

public class Creator {

    private final String template;

    public Creator(String templateName) {
        this.template = new TemplateReader().getTemplate(templateName);
    }

    public Creator() {
        this.template = new TemplateReader().getTemplate("template");
    }

    public File createClass(String className) {
        createFolderForDto();

        var filePath = getDtoPath() + className + getPrefixClassName() + ".java";

        var newClass = new File(filePath);
        if (!newClass.exists()) {
            try {
                if (newClass.createNewFile()) log().info("Class created successfully: " + filePath);
            } catch (IOException e) {
                log().error("Error creating class: " + e.getMessage());
            }
        } else {
            log().warn("Class already exists: " + filePath);
        }
        return newClass;
    }

    private void createFolderForDto() {
        var folder = new File(getDtoPath());
        if (!folder.exists()) {
            log().info("Folder created " + folder.mkdir() + ": " + folder.getAbsolutePath());
        }
    }

    public void createContent(File newClass, Map<String, String> properties) {
        try {
            var writer = new FileWriter(newClass);
            writer.write(String.format(template, newClass.getName()
                    .replace(".java", ""), getContent(properties)));
            writer.close();
        } catch (IOException e) {
            log().error("Error writing to file: " + e.getMessage());
        }
    }

    private StringBuilder getContent(Map<String, String> properties) {
        var content = new StringBuilder();
        properties.forEach((key, value) -> content.append("    private ")
                .append(getMatchedValue(value))
                .append(" ")
                .append(key)
                .append(";")
                .append("\n"));
        return content;
    }
}
