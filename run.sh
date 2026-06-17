export OLLAMA_MODEL=carstenuhlig/omnicoder-2-9b:q4_k_m
rlwrap java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar target/ai-java-agent-1.0.0.jar $*
