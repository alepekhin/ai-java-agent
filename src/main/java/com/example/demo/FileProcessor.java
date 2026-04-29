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
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("File not found: {}", filePath);
            throw new IOException("File not found: " + filePath);
        }
        if (file.isFile() && (filePath.endsWith(".java") || filePath.endsWith(".txt"))) {
            prompt.append(Files.readString(Path.of(filePath))).append("\n");
            log.info("Read file: {}", filePath);
        } else if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                process(child.getAbsolutePath(), prompt);
            }
        } else {
            log.warn("Skipping non-file or non-supported file type: {}", filePath);
        }
    }

    /**
     * Записывает текст ответа в файл.
     *
     * @param text текст ответа
     * @throws IOException если произошла ошибка при записи файла
     */
    public void writeResponse(String text, String outputFile) throws IOException {
        Path outputPath = Path.of(outputFile);
        Files.writeString(outputPath, text);
        log.info("Written to output.txt at {}", outputPath);
    }
}

