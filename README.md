# ai-java-agent
Simple AI agent on Spring AI

Простейший ИИ агент, построенный с помощью Spring AI https://spring.io/projects/spring-ai

## Требования

На компьютере должно быть установлено

- Java 25
- ollama 0.21.2
- LLM qwen2.5-coder:7b

Проверить версии

```
java -version
  java version "25.0.3" 2026-04-21 LTS
ollama -v
  ollama version is 0.21.2
ollama run qwen2.5-coder:7b
  >>> Send a message (/? for help)
  Ctrl/D to exit
```
## Построить приложение

```
./mvnw clean package
```
В результате будет построен файл `target/ai-java-agent-1.0.0.jar`

## Использование

Приложение выполняет промпт составленный из аргументов командной строки или
содежимого файлов, если аргумент файл.
Среди указанных файлов могут быть каталоги, тогда включаются файлы из них.
Включаются только файлы с расширениями `.java` и `.md`
Промпт и ответ сохраняются в истории и история включается в следующий промпт
поскольку ollama является stateless


Например, чтобы получить ревью кода, выполнить
```
java --enable-native-access=ALL-UNNAMED -jar target/ai-java-agent-1.0.0.jar 
Enter prompt>
```

Например, чтобы получить ревью проекта, задать промпт "Make review of prоject src/main/java"
В результате в промпт будут добавлены все файл *.java в каталоге src/main/java и ниже.

Результат выводится в stdout и может быть переназначен в файл. 

Чтобы получить шутку, выполнить
```
java --enable-native-access=ALL-UNNAMED -jar target/ai-java-agent-1.0.0.jar 
Enter prompt> Расскажи шутку
```
или
```
./run.sh 
Enter prompt> Расскажи шутку
```

Пример использования для написания теста
```
$ rlwrap ./run.sh src/test/java/com/example/demo/FileProcessorTest.java
Enter prompt> написать junit тесты для класса src/main/java/com/example/demo/FileProcessor.java
Thinking...`
Enter prompt> 
```
Ответ записывается в файл 'src/test/java/com/example/demo/FileProcessorTest.java', занный аргументом.



