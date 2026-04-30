# --- STAGE 1: build ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

# Instalando o JDK 25 manualmente no estágio de build
RUN apt-get update && apt-get install -y wget
RUN wget https://download.oracle.com/java/25/latest/jdk-25_linux-x64_bin.tar.gz
RUN mkdir -p /usr/lib/jvm/jdk-25 && tar -xzf jdk-25_linux-x64_bin.tar.gz -C /usr/lib/jvm/jdk-25 --strip-components=1
ENV JAVA_HOME=/usr/lib/jvm/jdk-25
ENV PATH=$JAVA_HOME/bin:$PATH

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B -DskipTests clean package

# --- STAGE 2: run ---
FROM openjdk:25-ea-jdk-slim
RUN groupadd -r app && useradd -r -g app app
USER app

WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=30"

ENTRYPOINT ["java","-jar","/app/app.jar"]