package pl.Nifena.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class OcrProcessor {

    private final Tesseract tesseract;
    private final Path outputDir;

    public OcrProcessor(Tesseract tesseract, Path outputDir) {
        this.tesseract = tesseract;
        this.outputDir = outputDir;
    }

    public void process(Path inputDir, Path fileName) {

        File file = inputDir.resolve(fileName).toFile();
        try {
            String textInput = tesseract.doOCR(file);

            String result = build(textInput);

            Path outputFile = outputDir.resolve(
                    fileName.toString().replaceAll("(?i)\\.jpe?g$", ".tex")
            );

            Files.write(outputFile, result.getBytes(), StandardOpenOption.CREATE);
            System.out.println("Extracted text to " + outputFile);

        } catch (TesseractException | IOException e) {
            System.err.println("OCR failed for " + fileName + ": " + e.getMessage());
        }
    }


    public String build(String content){
        return """
                \\documentclass{article}
                \\usepackage[utf8]{inputenc}
                \\begin{document}
                %s
                \\end{document}
                """.formatted(escape(content));
    }

    private String escape(String t) {
        return t.replace("\\", "\\textbackslash{}")
                .replace("_", "\\_")
                .replace("%", "\\%")
                .replace("&", "\\&")
                .replace("$", "\\$")
                .replace("#", "\\#");
    }
}
