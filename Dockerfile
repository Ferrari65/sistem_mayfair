# --- STAGE 1: build ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests clean package


# --- STAGE 2: run ---
FROM eclipse-temurin:21-jdk

RUN useradd -m app
USER app

WORKDIR /app

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["java","-jar","app.jar"]