package swagger_scanner.util.spec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class JsonSpec implements Spec {
    private static JsonNode jsonNode = null;

    public JsonSpec(String jsonString) {
        try {
            jsonNode = new ObjectMapper().readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Map<String, String>> getContent() {
        var definitionsPath = "definitions";
        var propertiesPath = "/properties";

        var map = new HashMap<String, Map<String, String>>();

        getKeysByJsonPath(definitionsPath).forEach(name -> {
            var propMap = new HashMap<String, String>();

            var jsonPath = definitionsPath + "/" + name + propertiesPath;
            var propNameList = getKeysByJsonPath(jsonPath);

            propNameList.forEach(propKey -> {
                var propValue = defineArrayValue(jsonPath + "/" + propKey);
                propMap.put(propKey, propValue);
            });
            map.put(name, propMap);
        });

        return map;
    }

    private String defineArrayValue(String basePath) {

        var conditionsToCheck = new ArrayList<>(Arrays.asList("/type", "/$ref"));
        var arrayConditions = new ArrayList<>(Arrays.asList("/items/type", "/items/$ref"));

        var type = conditionsToCheck.stream()
                .map(condition -> getValueByJsonPath(basePath + condition))
                .filter(val -> !val.isEmpty())
                .findFirst()
                .orElse("");


        if (type.equals("array")) {
            return arrayConditions.stream()
                    .map(condition -> getValueByJsonPath(basePath + condition))
                    .filter(value -> !value.isEmpty())
                    .findFirst()
                    .map(val -> val + "[]")
                    .orElse("");
        }

        return type;
    }

    private static String getValueByJsonPath(String jsonPath) {
        return findByJsonPath(jsonPath).asText();
    }

    private static List<String> getKeysByJsonPath(String jsonPath) {
        var keys = new ArrayList<String>();
        var currentJsonNode = findByJsonPath(jsonPath);
        currentJsonNode.fieldNames()
                .forEachRemaining(keys::add);
        return keys;
    }

    private static JsonNode findByJsonPath(String jsonPath) {
        var pathElements = jsonPath.split("/");
        var currentJsonNode = jsonNode;

        for (String pathElement : pathElements) {
            currentJsonNode = currentJsonNode.path(pathElement);
        }
        return currentJsonNode;
    }
}
