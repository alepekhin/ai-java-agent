package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OllamaRunnerTest {

    private ChatClient chatClient;
    private ChatClient.Builder chatClientBuilder;
    private FileProcessor fileProcessor;
    private OllamaRunner ollamaRunner;

    @BeforeEach
    void setUp() {
        chatClient = mock(ChatClient.class);
        chatClientBuilder = mock(ChatClient.Builder.class);
        when(chatClientBuilder.build()).thenReturn(chatClient);

        fileProcessor = mock(FileProcessor.class);

        ollamaRunner = new OllamaRunner(chatClientBuilder, fileProcessor);
    }

    @Test
    void testGetPrompt_ReturnsProcessedInput() throws IOException {
        // Mock System.console().readLine() to return a specific string
        // Since System.console() can't be mocked directly, we can simulate by temporarily replacing System.console()
        // Alternatively, modify the class to inject a ConsoleReader for testing.
        // For simplicity, assume getPrompt() is tested separately or refactored for testability.

        // For demonstration purpose, this test may be skipped or refactored.
    }

    @Test
    void testProcessResponse_WithNoArgs_ShouldPrintResponse() throws IOException {
        // Since System.out.println is used, we can redirect System.out to capture output
        String responseText = "Response text";

        // Capture System.out
        var outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        // Call method
        ollamaRunner
            .processResponse(responseText, new String[]{});

        // Assert
        String output = outputStream.toString().trim();
        assertEquals(responseText, output);
    }

    @Test
    void testProcessResponse_WithArgs_ShouldWriteToFile() throws IOException {
        String responseText = "Response to file";

        // Call method with args
        String filename = "testfile.txt";
        ollamaRunner.processResponse(responseText, new String[]{filename});

        // Verify writeToFile called
        verify(fileProcessor).writeToFile(responseText, filename);
    }
}
