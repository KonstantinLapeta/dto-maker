package swagger_scanner;

import swagger_scanner.util.spec.JsonSpec;
import swagger_scanner.util.spec.YamlSpec;

public class Executor {

    public static void main(String[] args) {
         //executeYamlConfig();
        executeJsonConfig();
    }

    static void executeYamlConfig() {
        var creator = new Creator();
        new YamlSpec(Requester.getYamlFromResources("openapi")).getContent().forEach((key, map) -> creator.createContent(creator.createClass(key), map));
    }

    static void executeJsonConfig() {
        var creator = new Creator();
        new JsonSpec(Requester.getSwaggerJson()).getContent().forEach((key, map) -> creator.createContent(creator.createClass(key), map));
    }
}
