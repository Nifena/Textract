package pl.Nifena;

import net.sourceforge.tess4j.Tesseract;

import java.nio.file.Path;

public class TesseractInitializer {

    public Tesseract initialize(Path tessdataDir, String language) {
        Tesseract t = new Tesseract();
        t.setDatapath(tessdataDir.toAbsolutePath().toString());
        t.setLanguage(language);
        return t;
    }
}
