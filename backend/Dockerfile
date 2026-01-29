FROM azul/zulu-openjdk:21
LABEL authors="Sebastian Büge"

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} my-photo-manager.jar

ENTRYPOINT ["java", "-jar", "my-photo-manager.jar"]