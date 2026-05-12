package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class ConsoleInputReader implements InputReader {
    @Override
    public String readLine(String prompt) {
        return System.console().readLine(prompt);
    }
}

