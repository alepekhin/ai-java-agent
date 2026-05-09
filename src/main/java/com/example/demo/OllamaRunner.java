package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OllamaRunner implements CommandLineRunner {

    private final ChatClient chatClient;
    private final FileProcessor fileProcessor;
    private StringBuilder history = new StringBuilder();

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
    public void run(String[] args) throws IOException {
            while (true) {
                processResponse(processPrompt(getPrompt()), args);
            }
    }

    protected String processPrompt(String prompt) throws IOException {
        history.append(prompt);
        fileProcessor.writeToFile(history.toString(), "prompt.txt");
        System.out.println("Thinking...");
        String response = chatClient.prompt().user(history.toString()).call().content();
        history.append(response);
        return response;
    }

    private String getPrompt() throws IOException {
        String line = System.console().readLine("Enter prompt> ");
        if (line != null && !line.isBlank()) {
            StringBuilder prompt = new StringBuilder();
            for (String arg : line.split(" ")) {
                if (!arg.isBlank()) {
                    fileProcessor.process(arg, prompt);
                }
            }
            return prompt.toString();
        }
        System.exit(0);
        return "";
        
    }

    /**
     * Обрабатывает полученный ответ и записывает его в файл.
     *
     * @param text текст ответа
     * @throws IOException если произошла ошибка при записи файла
     */
    protected void processResponse(String text, String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println(text);
        } else {
            fileProcessor.writeToFile(text, args[0]);
        }
    }
}

