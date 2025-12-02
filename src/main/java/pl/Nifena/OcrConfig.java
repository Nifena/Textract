package pl.Nifena;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OcrConfig {

    private final String language;

    public OcrConfig(String language) {
        this.language = language;
    }

    public Path getTessdataDir() {
        return Paths.get("tessdata");
    }

    public Path getInputDir() {
        return Paths.get("Input");
    }

    public Path getOutputDir() {
        return Paths.get("Output");
    }

    public String getLanguage() {
        return language;
    }

}
