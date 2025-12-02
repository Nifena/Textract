package pl.Nifena;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {

    private final Path inputDir;
    private final OcrProcessor processor;

    public FileWatcher(Path inputDir, OcrProcessor processor) {
        this.inputDir = inputDir;
        this.processor = processor;
    }

    public void startWatching() throws IOException, InterruptedException {
        WatchService service = FileSystems.getDefault().newWatchService();

        inputDir.register(service, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Watching folder: " + inputDir);

        while (true) {
            WatchKey key = service.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                Path fileName = (Path) event.context();
                String lower = fileName.toString().toLowerCase();

                if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                    System.out.println("Processing " + fileName);
                    processor.process(inputDir, fileName);
                }
            }

            key.reset();
        }
    }
}
