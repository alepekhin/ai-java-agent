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
## Построить приложение

```
gradle build
```
В результате будет построен исполняемый файл `build/libs/ai-java-agent-1.0.0.jar`

## Использование

Приложение выполняет промпт составленный из одного или нескольких файлов.
Среди указанных файлов могут быть каталоги, тогда включаются файлы из них.
Включаются только файлы с расширениями `.java` и `.txt`

Например, чтобы получить ревью кода, выполнить
```
java --enable-native-access=ALL-UNNAMED -jar build/libs/ai-java-agent-1.0.0.jar prompt-review.txt src/main/java
```
Здесь промпт строится из файла `prompt-review.txt` и всех файлов *.java в каталоге src/main/java

В результате должен появиться файл output.txt содержаший ревью кода данного проекта













