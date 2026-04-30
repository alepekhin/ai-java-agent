package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OllamaRunner implements CommandLineRunner {

    private final ChatClient chatClient;
    private final FileProcessor fileProcessor;
    private final String OUTPUT_FILE = "output.txt";

    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param chatClientBuilder бUILDER для создания экземпляра ChatClient
     * @param fileProcessor процессор файлов
     */
    public OllamaRunner(ChatClient.Builder chatClientBuilder, FileProcessor fileProcessor) {
        this.chatClient = chatClientBuilder.build();
        this.fileProcessor = fileProcessor;
    }

    @Override
    public void run(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments provided");
        }
        try {
            String prompt = getPrompt(args);
            log.info("Generated prompt: \n{}", prompt);
            fileProcessor.writeToFile(prompt, "prompt.txt");
            String response = chatClient.prompt().user(prompt).call().content();
            process(response);
        } catch (IOException e) {
            log.error("Error processing prompt or files", e);
        }
    }

    private String getPrompt(String[] args) throws IOException {
        StringBuilder prompt = new StringBuilder();
        for (String arg : args) {
            fileProcessor.process(arg, prompt);
        }
        return prompt.toString();
    }

    /**
     * Обрабатывает полученный ответ и записывает его в файл.
     *
     * @param text текст ответа
     * @throws IOException если произошла ошибка при записи файла
     */
    private void process(String text) {
        System.out.println(text);
    }
}

