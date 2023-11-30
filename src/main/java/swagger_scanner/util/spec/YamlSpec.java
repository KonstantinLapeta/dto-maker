package swagger_scanner.util.spec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlSpec implements Spec {

    private static Map<String, ?> yamlContent;

    public YamlSpec(Map<String, ?> yamlSting) {
        yamlContent = yamlSting;
    }

    private static String getValueByYamlPath(String yamlPath) {
        var pathElements = yamlPath.split("/");
        var currentMap = yamlContent;

        for (String pathElement : pathElements) {
            Object nextElement = currentMap.get(pathElement);

            if (nextElement instanceof Map) {
                currentMap = (Map<String, ?>) nextElement;
            } else {
                return String.valueOf(nextElement);
            }
        }

        return null;
    }

    private static List<?> getKeysByYamlPath(String yamlPath) {
        var pathElements = yamlPath.split("/");
        var currentMap = yamlContent;

        for (String pathElement : pathElements) {
            Object nextElement = currentMap.get(pathElement);

            if (nextElement instanceof Map) {
                currentMap = (Map<String, ?>) nextElement;
            } else {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>(currentMap.keySet());
    }


    public Map<String, Map<String, String>> getContent() {
        var pathToSchema = "components/schemas/";
        var schemaNamesList = getKeysByYamlPath(pathToSchema);
        Map<String, Map<String, String>> map = new HashMap<>();

        schemaNamesList.forEach(schemaName -> {
            var propMap = extractProperties(pathToSchema, schemaName.toString());
            map.put(schemaName.toString(), propMap);
        });

        System.out.println(map);
        return map;
    }

    private Map<String, String> extractProperties(String pathToSchema, String schemaName) {
        Map<String, String> propMap = new HashMap<>();
        var propPath = pathToSchema + schemaName + "/properties";
        var propNameList = getKeysByYamlPath(propPath);

        propNameList.forEach(propName -> {
            var propValue = getValueByYamlPath(propPath + "/" + propName + "/type");

            if ("null".equals(propValue)) {
                propValue = getValueByYamlPath(propPath + "/" + propName + "/$ref");
            } else if ("array".equals(propValue)) {
                propValue = extractArrayType(pathToSchema, schemaName, propName.toString());
            }

            propMap.put(propName.toString(), propValue);
        });

        return propMap;
    }

    private String extractArrayType(String pathToSchema, String schemaName, String propName) {
        var itemsRef = getValueByYamlPath(pathToSchema + schemaName + "/properties/" + propName + "/items/$ref");

        if (!"null".equals(itemsRef)) {
            return itemsRef + "[]";
        } else {
            return getValueByYamlPath(pathToSchema + schemaName + "/properties/" + propName + "/items/type") + "[]";
        }
    }
}
