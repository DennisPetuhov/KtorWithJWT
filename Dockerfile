# Используем базовый образ с нужной версией Java
FROM adoptopenjdk:11-jre-hotspot

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл из проекта в контейнер
COPY build/libs/*.jar /app/server.jar

# Указываем порт, который будет слушать сервер Ktor
EXPOSE 8080

# Команда для запуска Ktor-сервера
CMD ["java", "-jar", "/app/server.jar"]