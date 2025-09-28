package pl.Nifena;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("pol");

        Path inputPath = Paths.get("Input");
        Path outputPath = Paths.get("Output");
        createFolders(inputPath, outputPath);

        inputPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                Path filePath = (Path) event.context();
                System.out.println("Event: " + event.kind() + " | File affected: " + filePath);
                String fileNameLower = filePath.toString().toLowerCase();
                if (fileNameLower.endsWith(".jpg") || fileNameLower.endsWith(".jpeg")) {
                    processFile(tesseract, inputPath, outputPath, filePath);
                } else {
                    System.out.println("File format not supported yet");
                }
            }
            key.reset();
        }
    }

    private static void createFolders(Path... paths) throws IOException {
        for (Path path : paths) {
            Files.createDirectories(path);
            System.out.println("Folder created: " + path);
        }
    }

    private static void processFile(Tesseract tesseract, Path inputPath, Path outputPath, Path filePath) {
        File file = inputPath.resolve(filePath).toFile();
        try {
            String result = tesseract.doOCR(file);
            Path outputFile = outputPath.resolve(filePath.toString().replaceAll("(?i)\\.jpe?g$", ".txt"));
            Files.write(outputFile, result.getBytes(), StandardOpenOption.CREATE);
            System.out.println("Text successfully extracted to " + outputFile);
        } catch (TesseractException | IOException e) {
            System.out.println("OCR failed: " + e.getMessage());
        }
    }
}
