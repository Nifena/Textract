package pl.Nifena;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class TrainedDataManager {

    private final Path tessdataDir;

    public TrainedDataManager(Path tessdataDir) {
        this.tessdataDir = tessdataDir;
    }

    public void ensureTrainedDataExists(String lang) {
        Path file = tessdataDir.resolve(lang + ".traineddata");

        if (Files.exists(file)) {
            System.out.println("Traineddata already exists for: " + lang);
            return;
        }

        try (InputStream in = new URL(
                "https://github.com/tesseract-ocr/tessdata/raw/main/" + lang + ".traineddata"
        ).openStream()) {
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Downloaded traineddata for " + lang);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download traineddata: " + lang, e);
        }
    }


}
