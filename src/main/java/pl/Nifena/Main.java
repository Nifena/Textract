package pl.Nifena;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        OcrLanguageService ocrLanguageService = new OcrLanguageService();

        System.setProperty("jna.library.path", "/opt/homebrew/lib");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Languages available:");
        ArrayList<String> languages = ocrLanguageService.getAvailableLanguages();
        languages.forEach(l -> System.out.println(" - " + l));

        System.out.println("Enter the language you want to use for OCR:");
        String lang = scanner.nextLine();

        OcrConfig config = new OcrConfig(lang);

        DirectoryManager directoryManager =
                new DirectoryManager(config.getInputDir(), config.getOutputDir(), config.getTessdataDir());
        directoryManager.prepareDirectories();

        TrainedDataManager trainedDataManager =
                new TrainedDataManager(config.getTessdataDir());
        trainedDataManager.ensureTrainedDataExists(lang);

        TesseractInitializer initializer = new TesseractInitializer();
        var tesseract = initializer.initialize(config.getTessdataDir(), lang);

        OcrProcessor processor = new OcrProcessor(tesseract, config.getOutputDir());

        FileWatcher watcher = new FileWatcher(config.getInputDir(), processor);
        watcher.startWatching();
    }
}
