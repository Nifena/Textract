package pl.Nifena;

import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path inputPath = Paths.get("Input");
        Path outputPath = Paths.get("Output");

        createFolders(inputPath, outputPath);

        inputPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
        );

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Event: " + event.kind() + " | File affected: " + event.context());
            }
            key.reset();
        }
    }

    public static void createFolders(Path... paths) throws IOException {
        for (Path path : paths) {
            Files.createDirectories(path);
            System.out.println("Folder created: " + path);
        }
    }
}
