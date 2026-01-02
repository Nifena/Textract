package pl.Nifena;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class OcrLanguageService {


    ObjectMapper objectMapper = new ObjectMapper();
    String API_URL = "https://api.github.com/repos/tesseract-ocr/tessdata/contents/";

    public ArrayList<String> getAvailableLanguages(){
        ArrayList<String> languages = new ArrayList<>();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .version(HttpClient.Version.HTTP_2)
                    .GET()
                    .build();
        } catch (
                URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());

        for (JsonNode j:jsonNode){
            String name = j.get("name").asText();
            if (name.endsWith(".traineddata")){
                languages.add(name.replace(".traineddata",""));
            }
        }
        return languages;
    }
}
