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
    private final StringBuilder history = new StringBuilder();

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

//Ollama models are "stateless" by default, meaning they forget everything the moment a conversation ends. 
//To enable the ability to recall past interactions, you must implement a "memory" layer 
//that sends previous chat history back to the model with each new query.
    
    @Override
    public void run(String[] args) {
        try {
            String prompt = getPrompt();
            while (prompt.length() != 0) {
                log.info("Generated prompt: \n{}", prompt);
                history.append(prompt);
                fileProcessor.writeToFile(prompt, "prompt.txt");
                System.out.println("Thinking...");
                String response = chatClient.prompt().user(history.toString()).call().content();
                history.append(response);
                process(response, args);
                prompt = getPrompt();
            }
        } catch (IOException e) {
            log.error("Error processing prompt or files", e);
        }
    }

    private String getPrompt() throws IOException {
        String line = System.console().readLine("Enter prompt> ");
        StringBuilder prompt = new StringBuilder();
        for (String arg : line.split(" ")) {
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
    private void process(String text, String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println(text);
        } else {
            fileProcessor.writeToFile(text, args[0]);
        }
    }
}

