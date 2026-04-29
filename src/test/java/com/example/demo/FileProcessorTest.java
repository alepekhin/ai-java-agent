// Generate by deepai.org for prompt-test.txt
// qwen2.5-coder:7b generates not working tests
package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessorTest {

    @InjectMocks
    private FileProcessor fileProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcess_FileExistsAndIsSupportedType(@TempDir Path tempDir) throws IOException {
        // Создаем временный файл с расширением .txt
        Path tempFile = Files.createFile(tempDir.resolve("test.txt"));
        String content = "Hello, world!";
        Files.writeString(tempFile, content);

        StringBuilder prompt = new StringBuilder();

        fileProcessor.process(tempFile.toString(), prompt);

        assertTrue(prompt.toString().contains(content));
    }

    @Test
    void testProcess_DirectoryRecursion(@TempDir Path tempDir) throws IOException {
        // Создаем директорию и вложенные файлы
        Path subDir = Files.createDirectory(tempDir.resolve("subdir"));
        Path file1 = Files.createFile(tempDir.resolve("file1.java"));
        Path file2 = Files.createFile(subDir.resolve("file2.txt"));

        Files.writeString(file1, "Java content");
        Files.writeString(file2, "Text content");

        StringBuilder prompt = new StringBuilder();

        fileProcessor.process(tempDir.toString(), prompt);

        String result = prompt.toString();
        assertTrue(result.contains("Java content"));
        assertTrue(result.contains("Text content"));
    }

    @Test
    void testProcess_FileDoesNotExist() {
        String invalidPath = "nonexistent.file";
        StringBuilder prompt = new StringBuilder();

        IOException thrown = assertThrows(IOException.class, () -> {
            fileProcessor.process(invalidPath, prompt);
        });

        assertTrue(thrown.getMessage().contains("File not found"));
    }

    @Test
    void testProcess_SkipNonSupportedFileType(@TempDir Path tempDir) throws IOException {
        Path file = Files.createFile(tempDir.resolve("unsupported.bin"));
        StringBuilder prompt = new StringBuilder();

        fileProcessor.process(file.toString(), prompt);

        // Ожидается, что содержимое не добавится, так как тип файла не поддерживается
        assertEquals("", prompt.toString());
    }

    @Test
    void testWriteResponse_Success(@TempDir Path tempDir) throws IOException {
        String outputFile = tempDir.resolve("output.txt").toString();
        String text = "Response text";

        fileProcessor.writeResponse(text, outputFile);

        String fileContent = Files.readString(Path.of(outputFile));
        assertEquals(text, fileContent);
    }
}
