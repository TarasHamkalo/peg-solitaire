FROM eclipse-temurin:latest
LABEL authors="taras-hamkalo"

ARG ARTIFACT_PATH="./build/libs/"
ARG ARTIFACT_NAME="app-0.0.1.jar"

EXPOSE 8080

WORKDIR /app
COPY ${ARTIFACT_PATH}${ARTIFACT_NAME} .


ENTRYPOINT ["java", "-jar", "${ARTIFACT_NAME}"]
#ENTRYPOINT ["java", "-jar", "app-0.0.1.jar"]

#ENTRYPOINT ["ls", "-la"]
