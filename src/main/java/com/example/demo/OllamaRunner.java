package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class OllamaRunner implements CommandLineRunner {

    private final ChatClient chatClient;
    private final FileProcessor fileProcessor;
    private final InputReader inputReader;
    private final StringBuilder history = new StringBuilder();
    private static final int MAX_HISTORY_TOKENS = 64000;

    /**
     * Constructor for dependency initialization.
     *
     * @param chatClientBuilder builder for creating ChatClient instance
     * @param fileProcessor file processor
     * @param inputReader input reader
     */
    public OllamaRunner(ChatClient.Builder chatClientBuilder, FileProcessor fileProcessor, InputReader inputReader) {
        this.chatClient = chatClientBuilder.build();
        this.fileProcessor = fileProcessor;
        this.inputReader = inputReader;
        history.setLength(0);
    }

    /**
     * Ensures history stays within limits.
     */
    private void ensureHistoryLimit() {
        String[] words = history.toString().split("\\s+");
        System.out.println("context length " + words.length);
        if (words.length > MAX_HISTORY_TOKENS) {
            history.setLength(0);
            System.out.println("context truncated");
        }
    }

    /**
     * Ollama models are "stateless" by default, forgetting previous interactions.
     * We implement a memory layer that sends previous chat history with each query.
     */
    @Override
    public void run(String[] args) throws IOException {
        while (true) {
            String prompt = getPrompt();
            if (prompt.isBlank()) break;
            processResponse(processPrompt(prompt), args);
        }
    }

    protected String processPrompt(String prompt) throws IOException {
        history.append(prompt);
        System.out.println("Thinking...");
        String response = chatClient.prompt().user(history.toString()).call().content();
        ensureHistoryLimit();
        history.append(response);
        return response;
    }

    private String getPrompt() throws IOException {
        String line = inputReader.readLine("Enter prompt> ");
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
     * Processes the received response and writes it to file.
     *
     * @param text response text
     * @param args output file path if provided
     * @throws IOException if error occurs
     */
    protected void processResponse(String text, String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println(text);
        } else {
            fileProcessor.writeToFile(text, args[0]);
        }
    }
}

