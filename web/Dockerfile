# 1. Використовуємо офіційний образ Java 21 (легка версія Alpine)
FROM eclipse-temurin:21-jdk-alpine

# 2. Робоча папка всередині контейнера
WORKDIR /app

# 3. Копіюємо JAR файл (ми його зберемо перед деплоєм)
# Зверни увагу: ми копіюємо файл з папки target
COPY target/*.jar app.jar

# 4. Відкриваємо порт 8080
EXPOSE 8080

# 5. Команда запуску
ENTRYPOINT ["java", "-jar", "app.jar"]