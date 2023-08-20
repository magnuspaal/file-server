FROM maven:3.8.1-openjdk-17-slim AS builder

WORKDIR /app

COPY src ./src
COPY pom.xml .
RUN mvn -f /app/pom.xml clean
RUN mvn -f /app/pom.xml install
RUN mvn -f /app/pom.xml package

FROM openjdk:17-alpine
ARG APP_VERSION

COPY --from=builder /app/target/file-server-${APP_VERSION}.jar /usr/local/lib/file-server.jar

EXPOSE 8080

ENTRYPOINT java -jar /usr/local/lib/file-server.jar