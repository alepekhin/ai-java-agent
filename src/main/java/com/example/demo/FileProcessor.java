package com.example.demo;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;

@Component
@Slf4j
public class FileProcessor {

    /**
     * Обрабатывает файлы и добавляет их содержимое в строку запроса.
     *
     * @param filePath путь к файлу
     * @param prompt строитель строки запроса
     * @throws IOException если произошла ошибка при чтении файла
     */
    public void process(String filePath, StringBuilder prompt) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            // предполагаем что это уже промпт
            prompt.append(filePath);
            prompt.append(" ");
        }
        if (Files.isRegularFile(path) && (filePath.endsWith(".java") || filePath.endsWith(".txt"))) {
            prompt.append("\n");
            prompt.append(Files.readString(Path.of(filePath))).append("\n");
            log.info("Read file: {}", filePath);
        } else if (Files.isDirectory(path)) {
            Files.walk(path)
                 .filter(Files::isRegularFile)
                 .forEach(child -> {
                     try {
                         process(child.toString(), prompt);
                     } catch (IOException e) {
                         log.error("Error processing file: {}", child, e);
                     }
                 });
        } else {
            log.warn("Skipping non-file or non-supported file type: {}", filePath);
        }
    }

    /**
     * Записывает текст в файл.
     *
     * @param text текст ответа
     * @throws IOException если произошла ошибка при записи файла
     */
    public void writeToFile(String text, String outputFile) throws IOException {
        Path outputPath = Path.of(outputFile);
        try (var writer = Files.newBufferedWriter(outputPath)) {
            writer.write(text);
        }
        log.info("Written to output.txt at {}", outputPath);
    }
}

