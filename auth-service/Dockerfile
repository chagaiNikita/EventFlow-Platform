FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build

# Копируем parent pom
COPY pom.xml .

# Копируем pom конкретного сервиса
COPY auth-service/pom.xml auth-service/pom.xml

# Загружаем зависимости
RUN mvn -pl auth-service -am dependency:go-offline

# Копируем исходники
COPY auth-service/src auth-service/src

# Собираем ТОЛЬКО auth-service
RUN mvn -pl auth-service -am clean package -DskipTests

# ===== runtime =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /build/auth-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
