FROM openjdk:17-jdk-alpine

COPY build/libs/*.jar zerozero.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /zerozero.jar"]