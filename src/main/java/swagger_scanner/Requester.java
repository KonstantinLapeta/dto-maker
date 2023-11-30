package swagger_scanner;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static swagger_scanner.util.reader.PropertyReader.getResourcesPath;
import static swagger_scanner.util.reader.PropertyReader.getSwaggerUrl;

public class Requester {

    public static String getSwaggerJson() {
        return fetchSwagger(getSwaggerUrl());
    }

    public static Map<String, ?> getSwaggerYaml() {
        return new Yaml().load(fetchSwagger(getSwaggerUrl()));
    }

    public static Map<String, ?> getYamlFromResources(String yamlName) {
        try {
            return new Yaml().load(new FileInputStream(getResourcesPath() + yamlName + ".yaml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fetchSwagger(String url) {
        var request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
}
