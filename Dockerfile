FROM openjdk:17-jdk-alpine

ARG PROFILES

COPY build/libs/*.jar zerozero.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "/zerozero.jar"]