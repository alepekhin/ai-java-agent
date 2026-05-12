java --enable-native-access=ALL-UNNAMED -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  -jar target/ai-java-agent-1.0.0.jar $*
