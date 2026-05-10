package com.example.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.client.ChatClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OllamaRunnerTest {

    private final PrintStream originalOut = System.out;

    private ChatClient chatClient;
    private ChatClient.ChatClientRequestSpec requestSpec;
    private ChatClient.CallResponseSpec callResponseSpec;
    private FileProcessor fileProcessor;
    private OllamaRunner ollamaRunner;

    @BeforeEach
    void setUp() {
        ChatClient.Builder chatClientBuilder = mock(ChatClient.Builder.class);
        chatClient = mock(ChatClient.class);
        requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        callResponseSpec = mock(ChatClient.CallResponseSpec.class);
        fileProcessor = mock(FileProcessor.class);

        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);

        ollamaRunner = new OllamaRunner(chatClientBuilder, fileProcessor);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void processPromptWritesPromptHistoryAndReturnsChatResponse() throws IOException {
        when(callResponseSpec.content()).thenReturn("model response");

        String response = ollamaRunner.processPrompt("first prompt");

        assertEquals("model response", response);
        verify(fileProcessor).writeToFile("first prompt", "prompt.txt");
        verify(requestSpec).user("first prompt");

        var orderedCalls = inOrder(fileProcessor, chatClient, requestSpec, callResponseSpec);
        orderedCalls.verify(fileProcessor).writeToFile("first prompt", "prompt.txt");
        orderedCalls.verify(chatClient).prompt();
        orderedCalls.verify(requestSpec).user("first prompt");
        orderedCalls.verify(requestSpec).call();
        orderedCalls.verify(callResponseSpec).content();
    }

    @Test
    void processPromptIncludesPreviousConversationInNextPrompt() throws IOException {
        when(callResponseSpec.content()).thenReturn("first response", "second response");

        ollamaRunner.processPrompt("first prompt");
        ollamaRunner.processPrompt("second prompt");

        ArgumentCaptor<String> userPrompt = ArgumentCaptor.forClass(String.class);
        verify(requestSpec, org.mockito.Mockito.times(2)).user(userPrompt.capture());

        assertEquals("first prompt", userPrompt.getAllValues().get(0));
        assertEquals("first promptfirst responsesecond prompt", userPrompt.getAllValues().get(1));
        verify(fileProcessor).writeToFile("first promptfirst responsesecond prompt", "prompt.txt");
    }

    @Test
    void processResponseWithNoArgsPrintsResponse() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        ollamaRunner.processResponse("Response text", new String[] {});

        assertEquals("Response text" + System.lineSeparator(), outputStream.toString());
        verifyNoInteractions(fileProcessor);
    }

    @Test
    void processResponseWithArgsWritesResponseToFirstArgFile() throws IOException {
        ollamaRunner.processResponse("Response to file", new String[] {"testfile.txt", "ignored.txt"});

        verify(fileProcessor).writeToFile("Response to file", "testfile.txt");
    }
}
