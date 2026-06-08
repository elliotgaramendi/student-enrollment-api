# syntax=docker/dockerfile:1.7

FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/student-enrollment-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
