# Этап сборки
FROM gradle:8.10-alpine AS builder

WORKDIR /app
COPY . .

# Сборка JAR-файла
RUN gradle fatJar --no-daemon

# Этап запуска
FROM openjdk:17-slim

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar server.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/server.jar"]

## Use the base image with the required version of Java
#FROM adoptopenjdk:11-jre-hotspot
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the built JAR file from the project to the container
#COPY build/libs/*.jar /app/server.jar
#
## Specify the port that the Ktor server will listen on
#EXPOSE 8080
#
## Command to run the Ktor server
#CMD ["java", "-jar", "/app/server.jar"]