# Textract 🖼️➡️📄

**Textract** is a Java-based OCR (Optical Character Recognition) application that automatically converts images into LaTeX-ready text files using **Tesseract OCR**.  
The project also includes **experimental integration with a local LLM (Ollama)** for future text post-processing and analysis.

> ⚠️ **IMPORTANT:**  
> This project is **NOT finished** and is **actively under development**.  
> New features, refactors, and architectural changes are planned.

---


## ▶️ How to Run

1. **Clone the repository**  
   ```bash
   git clone https://github.com/Nifena/Textract.git
   cd Textract
   ```
2. **Compile the project**
Make sure you have Java 24 and Maven installed:
    ```bash
    mvn compile
    ```
3. **Run the main OCR application**
This will start the file watcher and prompt you to select an OCR language:
    ```bash
    mvn exec:java -Dexec.mainClass="pl.Nifena.Main"
    ```

---

## 🚧 Project Status

🔨 **Work in progress**  
This repository is a learning & portfolio project and will be **continuously updated**.

Planned improvements include:
- OCR + LLM pipeline integration
- Better error handling
- Configurable output formats
- CLI and/or GUI interface
- Refactoring and modularization
- Tests

---

## ✨ Features

### ✅ Implemented
- OCR using **Tesseract (tess4j)**
- Automatic directory setup (`Input`, `Output`, `tessdata`)
- File system watcher (auto-process new images)
- Dynamic OCR language listing from Tesseract GitHub repository
- Automatic download of missing `.traineddata` files
- LaTeX (`.tex`) output generation
- Experimental **Ollama** integration (local LLM)
- Simple LLM connectivity test and chat example

### 🧪 Experimental
- Local LLM interaction via **Ollama**
- Chat-based example using `llama3.2`

---

## 🗂️ Project Structure

```text
nifena-textract/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── pl/
                └── Nifena/
                    ├── ChatExample.java
                    ├── Main.java
                    ├── OllamaTest.java
                    ├── TrainedDataManager.java
                    ├── fs/
                    │   ├── DirectoryManager.java
                    │   └── FileWatcher.java
                    └── ocr/
                        ├── OcrConfig.java
                        ├── OcrLanguageService.java
                        ├── OcrProcessor.java
                        └── TesseractInitializer.java

```                            
## 📝 OCR → LaTeX Example

**Input file**: `example.jpg` (image containing text)

**Output file**: `example.tex`
```latex
\documentclass{article}
\usepackage[utf8]{inputenc}
\begin{document}
Recognized text from the image...
\end{document}
```

---

## 🤖 Ollama (LLM) Integration

This project includes experimental support for **Ollama**:
- Server availability check (`OllamaTest.java`)
- Model pulling (`llama3.2`)
- Multi-turn conversation example (`ChatExample.java`)

> ✅ Make sure Ollama is running before testing LLM features.

---

## 🛠️ Requirements

- **Java 24**
- **Maven**
- **Tesseract OCR** (with development libraries)
- **Ollama** (optional, for LLM features)

### macOS (using Homebrew)
```bash
# Install Tesseract
brew install tesseract

# Install Ollama (optional)
brew install ollama

# Start Ollama (run any model once to activate the server)
ollama run llama3.2
```

## 📦 Dependencies

- [`tess4j`](https://github.com/nguyenq/tess4j) – Java wrapper for Tesseract OCR  
- [`com.fasterxml.jackson.core:jackson-databind`](https://github.com/FasterXML/jackson) – JSON parsing 
- [`io.github.ollama4j:ollama4j`](https://github.com/ollama4j/ollama4j) – Java client for Ollama API  
- [`ch.qos.logback:logback-classic`](https://logback.qos.ch/) – Logging backend  


## 📌 Notes

- Currently supports only `.jpg` and `.jpeg` input files.
- Output is always a valid LaTeX document (`.tex`).
- Designed as a **learning and portfolio project** — not production-ready.
- Language files are downloaded on-demand from the official Tesseract GitHub repo.

## 🧭 Roadmap

- [ ] Integrate LLM for OCR result refinement
- [ ] Add automatic language detection
- [ ] Support PDF and plain-text output formats
- [ ] Implement CLI flags (e.g., `--lang`, `--input-dir`)
- [ ] Write unit and integration tests
- [ ] Improve documentation and error messages
- [ ] Explore GUI prototype (JavaFX?)

---

## 📜 License

This project is for **educational and experimental purposes only**.  
No license is specified — all rights reserved unless otherwise stated.
