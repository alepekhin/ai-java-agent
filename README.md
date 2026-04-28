# ai-java-agent
Simple AI agent on Spring AI

Простейший ИИ агент, построенный с помощью Spring AI https://spring.io/projects/spring-ai

## Требования

На компьютере должно быть установлено

- Java 25
- gradle 9
- ollama 0.21.2
- LLM qwen2.5-coder:7b

Проверить версии

```
java -version
  java version "25.0.3" 2026-04-21 LTS
gradle -version
  Gradle 9.2.0
ollama -v
  ollama version is 0.21.2
ollama run qwen2.5-coder:7b
  >>> Send a message (/? for help)
  Ctrl/D to exit
```
## Построить и запустить приложение

```
gradle bootRun
```

Должен появиться файл output.txt 
содержаший результат выполнения файла prompt.txt













