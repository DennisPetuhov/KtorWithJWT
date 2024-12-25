## Используем официальный образ Gradle с поддержкой JDK
#FROM gradle:7.5.1-jdk17 AS builder
#
## Устанавливаем рабочую директорию
#WORKDIR /app
#
## Копируем файлы проекта
#COPY . .
#
## Собираем приложение
#RUN gradle fatJar --no-daemon

# Используем более легкий образ для запуска
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл из этапа сборки
COPY --from=builder /app/build/libs/*.jar app.jar

# Указываем порт, который приложение будет слушать (Render требует, чтобы порт был динамическим)
ENV PORT=8080

# Запускаем приложение
CMD ["java", "-jar", "app.jar"]
