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

Приложение выполняет промпт составленный из аргументов командной строки или
содежимого файлов, если аргумент файл.
Среди указанных файлов могут быть каталоги, тогда включаются файлы из них.
Включаются только файлы с расширениями `.java` и `.txt`
Промпт и ответ сохраняются в истории и история включается в следующий промпт
поскольку ollama является stateless


Например, чтобы получить ревью кода, выполнить
```
java --enable-native-access=ALL-UNNAMED -jar build/libs/ai-java-agent-1.0.0.jar 
prompt> Make review of project src/main/java
```
Здесь промпт строится из текста "Make review of prject" и всех файлов *.java, *.txt в каталоге src/main/java

Результат выводится в stdout и может быть переназначен в файл. 

Чтобы получить шутку, выполнить
```
java --enable-native-access=ALL-UNNAMED -jar build/libs/ai-java-agent-1.0.0.jar 
prompt> Расскажи шутку
```
или
```
./run.sh 
prompt> Расскажи шутку
```

Пример использования для написания теста
```
$ rlwrap ./run.sh src/test/java/com/example/demo/FileProcessorTest.java
Enter prompt> написать junit тесты для класса src/main/java/com/example/demo/FileProcessor.java
Thinking...
Enter prompt> 
```
Ответ записывается в файл, занный аргументом.

