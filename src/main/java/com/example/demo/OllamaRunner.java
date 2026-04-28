package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

/**
 * Компонента спринга для взаимодействия с LLM
 */
@Component
public class OllamaRunner implements CommandLineRunner {

    private final ChatClient chatClient;

    /**
     * Конструктор инициализирующий LLM клиента
     */
    public OllamaRunner(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Метод интерфейса CommandLineRunner выполняющий всю работу
     */
    @Override
    public void run(String[] args) throws Exception {
        // Промпт можно сформировать по разному - читая файлы, базу данных, выполняя curl и т.д.
        String prompt = getPrompt();
        // выполняет промпт
        String response = chatClient.prompt().user(getPrompt()).call().content();
        // Обрабатываем результат выполнения. При необходимости можно продолжить диалог с LLM
        process(response);
    }

    /**
     * Формирует промпт
     */
    private String getPrompt() throws IOException {
        return Files.readString(Path.of("prompt.txt"));
    }

    /**
     * Обрабатывает результат выполения промпта
     */
    private void process(String text) throws IOException {
        Files.writeString(Path.of("output.txt"), text);
    }

}

