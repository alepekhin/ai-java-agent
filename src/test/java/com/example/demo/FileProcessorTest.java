package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileProcessorTest {

    private FileProcessor fileProcessor;

    @BeforeEach
    public void setUp() {
        fileProcessor = new FileProcessor();
    }

    // Тест для метода process с файлом .java
    @Test
    void testProcessJavaFile(@TempDir Path tempDir) throws IOException {
        String javaContent = "public class Test { }";
        Path javaFilePath = tempDir.resolve("test.java");
        Files.writeString(javaFilePath, javaContent);

        StringBuilder prompt = new StringBuilder();
        fileProcessor.process(javaFilePath.toString(), prompt);

        assertTrue(prompt.toString().contains(javaContent));
    }

    // Тест для метода process с файлом .txt
    @Test
    void testProcessTxtFile(@TempDir Path tempDir) throws IOException {
        String txtContent = "Hello, World!";
        Path txtFilePath = tempDir.resolve("test.md");
        Files.writeString(txtFilePath, txtContent);

        StringBuilder prompt = new StringBuilder();
        fileProcessor.process(txtFilePath.toString(), prompt);

        assertTrue(prompt.toString().contains(txtContent));
    }

    // Тест для метода process с директорией
    @Test
    void testProcessDirectory(@TempDir Path tempDir) throws IOException {
        String javaContent1 = "public class Test1 { }";
        String javaContent2 = "public class Test2 { }";

        Path subDirPath = Files.createDirectories(tempDir.resolve("subdir"));
        Path javaFilePath1 = subDirPath.resolve("test1.java");
        Path javaFilePath2 = subDirPath.resolve("test2.java");

        Files.writeString(javaFilePath1, javaContent1);
        Files.writeString(javaFilePath2, javaContent2);

        StringBuilder prompt = new StringBuilder();
        fileProcessor.process(tempDir.toString(), prompt);

        assertTrue(prompt.toString().contains(javaContent1));
        assertTrue(prompt.toString().contains(javaContent2));
    }

    // Тест для метода process с несуществующим файлом
    @Test
    void testProcessNonExistingFile(@TempDir Path tempDir) throws IOException {
        Path nonExistentFilePath = tempDir.resolve("nonexistent.java");

        StringBuilder prompt = new StringBuilder();
        fileProcessor.process(nonExistentFilePath.toString(), prompt);

        assertEquals(nonExistentFilePath.toString() + " ", prompt.toString());
    }

    // Тест для метода writeToFile
    @Test
    void testWriteToFile(@TempDir Path tempDir) throws IOException {
        String text = "Hello, World!";
        Path outputFile = tempDir.resolve("output.txt");

        fileProcessor.writeToFile(text, outputFile.toString());

        assertTrue(Files.exists(outputFile));
        assertEquals(text, Files.readString(outputFile));
    }
}

