FROM eclipse-temurin:latest
LABEL authors="taras-hamkalo"

ARG ARTIFACT_PATH="/build/libs/app.jar"
ARG APPLICATION_PORT=8080

EXPOSE ${APPLICATION_PORT}

WORKDIR /app
COPY ${ARTIFACT_PATH} ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]