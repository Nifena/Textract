package pl.Nifena;

import io.github.ollama4j.Ollama;
import io.github.ollama4j.exceptions.OllamaException;

public class OllamaTest {
    public static void main(String[] args) throws OllamaException {
        String host = "http://localhost:11434/";
        Ollama ollama = new Ollama(host);
        boolean isOllamaServerReachable = ollama.ping();
        System.out.println("Is Ollama server running: " + isOllamaServerReachable);
    }
}
