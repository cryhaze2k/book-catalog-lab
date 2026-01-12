# Етап 1: Збірка (Build)
# Використовуємо образ з Maven для компіляції проекту
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Копіюємо весь проект (core, persistence, web, pom.xml)
COPY . .

# Збираємо проект (пропускаючи тести для швидкості)
RUN mvn clean package -DskipTests

# Етап 2: Запуск (Run)
# Беремо чистий JDK для запуску
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Копіюємо тільки готовий JAR файл з Етапу 1
# Шлях до jar у модулі web
COPY --from=build /app/web/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]