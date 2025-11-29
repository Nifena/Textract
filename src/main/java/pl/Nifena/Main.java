package pl.Nifena;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("jna.library.path", "/opt/homebrew/lib");
        Scanner scanner = new Scanner(System.in);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Tesseract tesseract = new Tesseract();

        System.out.println("Enter the language you want to use for OCR:");
        String lang = scanner.nextLine();

        Path tessdataDir = Paths.get("tessdata");
        Path trainedDataPath = pathCreator(lang);

        fileChecker(trainedDataPath, lang);

        tesseract.setDatapath(tessdataDir.toAbsolutePath().toString());
        tesseract.setLanguage(lang);

        Path inputPath = Paths.get("Input");
        Path outputPath = Paths.get("Output");

        checkIfPathExists(inputPath, outputPath);

        inputPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        checkWatchKeys(watchService, tesseract, inputPath, outputPath);
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

    private static Path pathCreator(String lang) {
        Path tessdataDir = Paths.get("tessdata");
        try {
            Files.createDirectories(tessdataDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tessdataDir.resolve(lang + ".traineddata");
    }

    private static void fileChecker(Path trainedDataPath, String lang) {
        if (!Files.exists(trainedDataPath)) {
            try (InputStream in = new URL("https://github.com/tesseract-ocr/tessdata/raw/refs/heads/main/" + lang + ".traineddata").openStream()) {
                Files.copy(in, trainedDataPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded traineddata for language: " + lang);
            } catch (IOException e) {
                System.out.println("Failed to download traineddata file: " + e.getMessage());
            }
        } else {
            System.out.println("File " + lang + " already exists");
        }
    }

    private static void checkIfPathExists(Path inputPath, Path outputPath) {
        if (!Files.exists(inputPath)) {
            try {
                createFolders(inputPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Folder Input already exists, you can paste an image there");
        }
        if (!Files.exists(outputPath)) {
            try {
                createFolders(outputPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Folder Output already exists and there will be your text after conversion");
        }
    }

    private static void checkWatchKeys(WatchService watchService, Tesseract tesseract, Path inputPath, Path outputPath) {
        WatchKey key;
        while (true) {
            try {
                if ((key = watchService.take()) == null) break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
}
