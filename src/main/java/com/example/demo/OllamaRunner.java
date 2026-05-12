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
    private final InputReader inputReader; // Add this field



    /**
     * Конструктор для инициализации зависимостей.
     *
     * @param chatClientBuilder бUILDER для создания экземпляра ChatClient
     * @param fileProcessor процессор файлов
     * @param inputReader   читатель ввода
     */
    public OllamaRunner(ChatClient.Builder chatClientBuilder, FileProcessor fileProcessor, InputReader inputReader) {
        this.chatClient = chatClientBuilder.build();
        this.fileProcessor = fileProcessor;
        this.inputReader = inputReader;
        System.out.println(">>>>>>>>>> "+inputReader.getClass().getName());
    }


    //Ollama models are "stateless" by default, meaning they forget everything the moment a conversation ends. 
    //To enable the ability to recall past interactions, you must implement a "memory" layer 
    //that sends previous chat history back to the model with each new query.

    @Override
    public void run(String[] args) throws IOException {
            while (true) {
                String prompt = getPrompt();
                System.out.println(">>>>>>>>>>>>> prompt:"+prompt+":");
                if (prompt.isBlank()) break;
                processResponse(processPrompt(prompt), args);
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
        String line = inputReader.readLine("Enter prompt> "); // Use the injected input reader
        System.out.println(">>>>>>> line "+line);
        if (line != null && !line.isBlank()) {
            StringBuilder prompt = new StringBuilder();
            for (String arg : line.split(" ")) {
                if (!arg.isBlank()) {
                    fileProcessor.process(arg, prompt);
                }
            }
            return prompt.toString();
        }
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

