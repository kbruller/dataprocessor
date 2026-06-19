# I use a lightweight Java runtime image as the base image.
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/dataprocessor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]