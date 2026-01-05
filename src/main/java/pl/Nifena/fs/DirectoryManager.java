package pl.Nifena.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryManager {

    private final Path inputDir;
    private final Path outputDir;
    private final Path tessdataDir;

    public DirectoryManager(Path inputDir, Path outputDir, Path tessdataDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.tessdataDir = tessdataDir;
    }

    private void createIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            System.out.println("Created directory: " + path);
        }
    }

    public void prepareDirectories() throws IOException {
        createIfNotExists(inputDir);
        createIfNotExists(outputDir);
        createIfNotExists(tessdataDir);
    }


}
