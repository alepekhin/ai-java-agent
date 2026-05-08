package com.example.demo;

import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;

@Component
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
        if (Files.isRegularFile(path) && (filePath.endsWith(".java") || filePath.endsWith(".md"))) {
            prompt.append("\n");
            prompt.append(Files.readString(Path.of(filePath))).append("\n");
            System.out.println("...added to context " + path);
        } else if (Files.isDirectory(path)) {
            Files.walk(path)
                 .filter(Files::isRegularFile)
                 .forEach(child -> {
                     try {
                         process(child.toString(), prompt);
                     } catch (IOException e) {
                         System.err.println("Error processing file: " + child + "\n" + e);
                     }
                 });
        } else {
            System.out.println("Skipping non-file or non-supported file type: " + filePath);
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
    }
}

