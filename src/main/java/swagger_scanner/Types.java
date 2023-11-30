package swagger_scanner;

import java.util.HashMap;
import java.util.Map;

import static swagger_scanner.util.reader.PropertyReader.getPrefixClassName;

public class Types {

    private static final Map<String, String> map =
            new HashMap<>(Map.of("string", "String", "integer", "int", "boolean", "boolean", "object", "Object"));

    public static String getMatchedValue(String value) {
        return wrapIfArray(splitIfDtoType(value));
    }

    private static String wrapIfArray(String value) {
        var squareBrackets = "[]";
        if (value.contains(squareBrackets)) {
            value = value.replace(squareBrackets, "");

            return map.containsKey(value) ? map.get(value) + squareBrackets :
                    value + getPrefixClassName() + squareBrackets;
        }
        return map.getOrDefault(value, value + getPrefixClassName());
    }

    private static String splitIfDtoType(String value) {
        var splitValues = value.split("/");
        return splitValues[splitValues.length - 1];
    }
}
