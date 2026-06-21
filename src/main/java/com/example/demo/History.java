package com.example.demo;

public class History {

    private StringBuilder history = new StringBuilder();

    private int SIZE = 64000;

    public String get() {
        return history.toString();
    }

    public void add(String text) {
        String content = history.append(text).toString();
        if (content.length() > SIZE) {
            history = new StringBuilder(content.substring(0, SIZE / 2));
        }
    }

    public int tokens() {
        String[] words = history.toString().trim().split("\\s+");
        // rough number of tokens, see https://docs.spring.io/spring-ai/reference/api/prompt.html
        return words.length * 4 / 3;
    }

}
