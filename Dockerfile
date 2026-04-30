# --- STAGE 1: build ---
FROM maven:3-openjdk-25-slim AS build
WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests clean package

# --- STAGE 2: run ---
FROM openjdk:25-slim
RUN addgroup -S app && adduser -S -G app -D app
USER app

WORKDIR /app

# Busca o JAR gerado e renomeia para app.jar
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=30"

ENTRYPOINT ["java","-jar","/app/app.jar"]