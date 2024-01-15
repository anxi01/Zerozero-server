FROM openjdk:17-jdk

COPY build/libs/*.jar zerozero.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /zerozero.jar"]