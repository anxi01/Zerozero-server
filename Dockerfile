FROM openjdk:17-jdk-alpine

ARG PROFILES
ARG ENCRYPTOR

COPY build/libs/*.jar zerozero.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Djasypt.encryptor.password=${ENCRYPTOR}", "-jar", "/zerozero.jar"]