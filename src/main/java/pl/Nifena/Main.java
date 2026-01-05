package pl.Nifena;

import pl.Nifena.fs.DirectoryManager;
import pl.Nifena.fs.FileWatcher;
import pl.Nifena.ocr.OcrConfig;
import pl.Nifena.ocr.OcrLanguageService;
import pl.Nifena.ocr.OcrProcessor;
import pl.Nifena.ocr.TesseractInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        OcrLanguageService ocrLanguageService = new OcrLanguageService();

        System.setProperty("jna.library.path", "/opt/homebrew/lib");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Languages available:");
        ArrayList<String> languages = ocrLanguageService.getAvailableLanguages();
        languages.forEach(l -> System.out.println(" - " + l));

        String lang = askForLanguage(scanner, languages);

        OcrConfig config = new OcrConfig(lang);

        DirectoryManager directoryManager =
                new DirectoryManager(config.getInputDir(), config.getOutputDir(), config.getTessdataDir());
        directoryManager.prepareDirectories();

        TrainedDataManager trainedDataManager =
                new TrainedDataManager(config.getTessdataDir());
        trainedDataManager.ensureTrainedDataExists(lang);

        TesseractInitializer initializer = new TesseractInitializer();
        var tesseract = initializer.initialize(config.getTessdataDir(), lang); //ustawia tesseracta

        OcrProcessor processor = new OcrProcessor(tesseract, config.getOutputDir());

        FileWatcher watcher = new FileWatcher(config.getInputDir(), processor);
        watcher.startWatching();
    }
    public static String askForLanguage(Scanner scanner, List<String> languages){
        while (true){
            System.out.println("Enter the language you want to use for OCR:");
            String lang = scanner.nextLine().trim();

            if (languages.contains(lang)){
                return lang;
            }
            System.out.println("Invalid language try again");
        }
    }
}
