# --- STAGE 1: Build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Cache de dependências para acelerar builds futuros
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests dependency:go-offline

# Compilação do projeto
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests clean package

# --- STAGE 2: Run ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S -G app -D app
USER app

WORKDIR /app

# O comando abaixo busca qualquer JAR gerado no target e o renomeia para app.jar
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=30"

ENTRYPOINT ["java","-jar","/app/app.jar"]
